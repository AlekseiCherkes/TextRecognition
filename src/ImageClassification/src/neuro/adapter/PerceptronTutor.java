package neuro.adapter;

import neuro.net.RecognizedType;
import neuro.net.Perceptron;
import neuro.layer.ActiveLayer;
import neuro.io.DataProvider;
import neuro.io.ServiceFileFilter;
import neuro.io.BufferedImageCodec;
import jblas.matrices.Matrix;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import algorithm.ErrorBackPropagation;

/**
 * @author Vadim Shpakovsky.
 */

// Apply for teaching net of perceptron type.
public class PerceptronTutor {
    private Perceptron net;

    // Path where are all teaching tests.
    private String teaching_path;
    // Path where are all controlling tests.
    private String control_path;
    // Adress of log file for brief teaching history.
    private String brief_log;
    // Adress of log file for detailed teaching history.
    private String detailed_log;
    // Size of max accepable difference between input and output when they are considered equal.
    private double output_accuracy;
    // Size of min accepable difference between old output and output of corrected net when is considered
    // that teach iteration was "useful" ( not idle ).
    private double idle_accuracy;
    // Count of numbers in fractional part.
    private int print_accuracy;
    private double teaching_speed;

    /**Empty constructor.*/
    public                      PerceptronTutor(){}

    /**Empty constructor.*/
    public                      PerceptronTutor( Perceptron net )
                                    throws CloneNotSupportedException{
        this.net = net.clone();
    }

    /** Initialize net from file.
     * @param storage       File for initializing.
     * @throws Exception    If were problems with file reading or file had wrong data.
     */
    public void                 initNet( String storage ) throws Exception{
        net = new Perceptron();
        ObjectInputStream input_stream = null;
        try{
            input_stream = new ObjectInputStream( new FileInputStream( storage ) );
            String read_type = ( String )input_stream.readObject();
            if ( !net.getType().equals( read_type ) ){
                throw new Exception( "Mismatch of net's types. " );
            }
            ArrayList<ActiveLayer> layers = ( ArrayList<ActiveLayer> )input_stream.readObject();
            int input_height = ( Integer )input_stream.readObject();
            int input_width = ( Integer )input_stream.readObject();
            net.setStructure( layers, input_height, input_width );
            ArrayList< String > output_types = ( ArrayList< String > )input_stream.readObject();
            net.setRecognizingTypes( output_types );
        }
        finally{
            if ( input_stream != null ){
                input_stream.close();
            }
        }
    }

    /** Save net in file.
     * @param storage       File for saving.
     * @throws Exception    If save error occured.
     */
    public void                 save( String storage ) throws Exception{
        net.save( storage );
    }

    /**Recognize class for input image.
     * @param x             Input image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    If problems with reading image from file occured.
     */
	public RecognizedType       recognize( Matrix x ) throws Exception{
        return net.recognizeClass( x );
    }

    /**Recognize class for input image.
     * @param path          File which consists image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    If problems with reading image from file occured.
     */
	public RecognizedType       recognize( String path ) throws Exception{
        // Read input from file.
        Matrix input_x = null;
        Scanner scanner = null;
        try{
            scanner = new Scanner( new File( path ) );
            input_x = new Matrix( net.getInputSize() , 1 );
            for ( int i = 0; i < net.getInputSize(); i++ ){
                double d =  scanner.nextDouble();
                input_x.set( i, 0, d );
            }
        }
        catch( Exception e){
           throw new Exception( "Problem with reading analysis.image from file." + e.getMessage() );
        }
        finally{
            if(  scanner != null ){
                scanner.close();
            }
        }

        return net.recognizeClass( input_x );
    }

    /** Print the weights of all layers in net to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void                 printNet( int precision ){
        net.print( precision );
    }

    /**
     * Teach the net.
     */
    public void                 train() throws Exception{
        // 1) Get analysis.data for teaching.

        DataProvider data_provider = new DataProvider();
        data_provider.setFileFilter( new ServiceFileFilter() );
        data_provider.setRootDir( new File( teaching_path ) );
        data_provider.setCodec( new BufferedImageCodec() );
        data_provider.loadData( net.getOutputSize() );

        // Associate each type with one output of net.
        net.setRecognizingTypes( data_provider.getTypes() );

        // 2) Teach net.

        PrintWriter brief_writer = null;
        PrintWriter detailed_writer = null;
        try{
            // Open log files.
            if ( brief_log != null ){
                brief_writer = new PrintWriter( brief_log );
            }
            if ( detailed_log != null ){
                detailed_writer = new PrintWriter( detailed_log );
            }
            long start_time = System.currentTimeMillis();
            long iteration = 1;
            DataProvider.TeachingData data_iterator = data_provider.provideTeachingData();
            //Iterator<TeachingCase<Matrix, String>> data_iterator = teaching_data.iterator();

            // Use tests for teaching one by one in cycle until net recognize all of it.
            // Use strategy: for teaching use one image of each type at one iteration
            // ( e.g. when for teaching was used image of current type, next teaching image
            // will find for next type ) .
            while( true ){
                if ( detailed_writer != null ){
                    detailed_writer.printf( "==========iteration №%d:==========\n\n", iteration );
                }
                // Count tests from all training set that were correctly recognized by net.
                int positive_results = 0;


                do{
                    while( data_iterator.hasNext() ){
                        TeachingCase<Matrix, String> sample = data_iterator.next();
                        Matrix x = sample.getInput();
                        Matrix y = net.recognize( x );
                        Matrix target_output = net.getOutputByType( sample.getExpectedOutput() );
                        if ( target_output == null ){
                            throw new Exception( "Net can't recognize images of \"" + sample.getExpectedOutput() + "\" type." );
                        }

                        Matrix E = target_output.minus( y );

                        if ( detailed_writer != null ){
//                            for ( int i = 0; i < net.getLayersCount(); i++ ){
//                                ActiveLayer layer = net.getLayer( i );
//                                detailed_writer.printf( "Layer_%d:\n", i );
//                                layer.getW().print( detailed_writer, 2, print_accuracy );
//                            }
                            detailed_writer.print("Input:\n");
                            x.transpose().print( detailed_writer, 2, print_accuracy );
                            detailed_writer.print("Output:\n");
                            y.transpose().print( detailed_writer, 2, print_accuracy );
                            detailed_writer.print("Target:\n");
                            target_output.transpose().print( detailed_writer, 2, print_accuracy );
                            detailed_writer.print("E:\n");
                            E.transpose().print( detailed_writer, 2, print_accuracy );
                        }

                        if ( E.maxNomr() <= output_accuracy ){
                            positive_results++;
                            if ( detailed_writer != null ){
                                detailed_writer.print("OK\n\n");
                            }
                            // Check next image from current type.
                            continue;
                        }

                        ErrorBackPropagation.teach( net, x, target_output, teaching_speed );

                        // Check if correction gives improvement.
                        Matrix new_y = net.recognize( x );
                        Matrix dy = new_y.minus( y );
                        double max = dy.maxNomr();
                        if ( max < idle_accuracy ){
                            // The training progress ends.
                            if ( detailed_writer != null ){
                                detailed_writer.print("Old output:\n");
                                y.transpose().print( detailed_writer, 2, print_accuracy );
                                detailed_writer.print("New output:\n");
                                new_y.transpose().print( detailed_writer, 2, print_accuracy );
                            }
                            if ( brief_writer != null ){
                                brief_writer.printf( "The teaching progress ended. Training was stopped. Net aren't trained.\n" );
                                brief_writer.printf( "Type: '%s'.\n", sample.getExpectedOutput() );
                                //TODO Print image name.
                                //brief_writer.printf( "Image : '%s'.\n", image );
                                brief_writer.printf( "Number of iteration: %d.\n", iteration );
                                brief_writer.printf( "Image size: %dx%d.\n", net.getInputHeight(), net.getInputWidth() );
                                brief_writer.printf( "Idle accuracy: %.2e.\n", idle_accuracy );
                                brief_writer.printf( "Teaching time: %s\n", this.getDeltaTime( start_time ) );
                            }
                            return;
                        }
                        else{
                            if ( detailed_writer != null ){
                                detailed_writer.print("New output:\n");
                                new_y.transpose().print( detailed_writer, 2, print_accuracy );
                            }
                            // Proceed to next group.
                            break;
                        }
                    }
                }while( data_iterator.proceedToNextGroup() );
                
                // Use control checking for escape too detailed teaching.
                DataProvider control_data_provider = new DataProvider();
                control_data_provider.setFileFilter( new ServiceFileFilter() );
                control_data_provider.setRootDir( new File( control_path ) );
                control_data_provider.setCodec( new BufferedImageCodec() );
                control_data_provider.loadData( net.getOutputSize() );
                DataProvider.TeachingData control_data_iterator = control_data_provider.provideTeachingData();
                int control_verified = 0;
                int all_control_count = 0;
                while( control_data_iterator.proceedToNextGroup() ){
                    while( control_data_iterator.hasNext() ){
                        all_control_count++;
                        TeachingCase<Matrix, String> control_sample = control_data_iterator.next();
                        Matrix control_x = control_sample.getInput();
                        String type = control_sample.getExpectedOutput();
                        if ( type.equals( net.recognizeClass( control_x ).getType() ) ){
                            control_verified++;
                        }
                    }
                }

                if ( detailed_writer != null && all_control_count != 0 ){
                    detailed_writer.printf( "_____Control checking result: verify %d  from %d._____\n",
                            control_verified, all_control_count );
                }
                if ( positive_results != data_provider.getDataCount() ){
                    // Too detailed teaching.
                    if ( all_control_count > 0 && control_verified == all_control_count ){
                        if ( brief_writer != null ){
                            brief_writer.printf( "Too detailed teaching ( %d/%d ). Training was stopped.\n",
                                    positive_results, all_control_count );
                        }
                    }
                    // Not all teaching and control images pass verification.
                    else{
                        iteration++;
                        positive_results = 0;
                        data_iterator.restart();
                        continue;
                    }
                }
                else{
                    // Teaching set are not representative.
                    if ( control_verified != all_control_count ){
                        if ( brief_writer != null ){
                            brief_writer.printf( "Teachign set are not representative ( control set didn't " +
                                    "pass verification %d/%d).\n", control_verified, all_control_count );
                        }
                    }
                    // Success teaching.
                    if ( control_verified == all_control_count ){
                        if ( brief_writer != null ){
                            brief_writer.println( "Success teaching." );
                        }
                    }
                }
                
                if ( brief_writer != null ){
                    brief_writer.printf( "Count of teaching tests: %d.\n", data_provider.getDataCount() );
                    brief_writer.printf( "Count of teaching iterations: %d.\n", iteration );
                    brief_writer.printf( "Image size: %dx%d.\n", net.getInputHeight(), net.getInputWidth() );
                    brief_writer.printf( "Precision: %.2e.\n", output_accuracy );
                    brief_writer.printf( "Net was teaching: %s\n", this.getDeltaTime( start_time ) );
                }
                // End teaching.                          
                break;
            }
        }
        finally{
            if ( brief_writer != null ){
                brief_writer.close();
            }
            if ( detailed_writer != null ){
                detailed_writer.close();
            }
        }
    }

    /** Initialize all wights of net by random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void                 resetNet( double max_val ){
        net.randomInit( max_val );
    }

    /**
     * @param start_time    Start time in msec.
     * @return              String with time passed since 'start_time'.
     */
    String                      getDeltaTime( long start_time ){
        long delta_time = System.currentTimeMillis() - start_time;
        long delta_time_sec = delta_time / 1000;
        long hours = delta_time_sec / 3600;
        long min = ( delta_time_sec - hours * 3600  ) / 60;
        long sec = delta_time_sec - hours * 3600 - min * 60;
        long msec = delta_time - delta_time_sec * 1000;
        return Long.toString( hours ) + " hours, " + Long.toString( min ) + " min, " +  Long.toString( sec ) +
                " sec, " + Long.toString( msec ) + " msec.";
    }

    /** Set teaching path.
     * On this path must be subdirectory for each type of recognition objects.
     * This subdirectory contain all objects belong certain type.
     * The name of subdirectory identify the name of type.
     * IMPORTANT!!! All files and directories begin with '.' are ignored.
     * @param path      Path where are all teaching tests.
     */
    public void                 setTeachingPath( String path ){
        teaching_path = path;
    }

    /** Get net's type.
        @return      String that consist net's type.
     */
    public String               getNetType(){
        return net.getType();
    }

    /**Get list of all  types that net can recognize.
     *
     * @return      List with types names.
     */
    public ArrayList< String >  getRecognizingTypes(){
        return net.getRecognizingTypes();
    }

    /**@return      Teaching path.*/
    public String               getTeachingPath(){
        return teaching_path;
    }

    /** Set controlling path.
     *  Path recstrictions coincides with teaching path ones.
     * @param path      Path where are all controlling tests.
     */
    public void                 setControlPath( String path ){
        control_path = path;
    }

    /**@return      Control path.*/
    public String               getControlPath(){
        return control_path;
    }

    /** Set brief log.
     * @param log    Adress of log file for brief teaching history.
     */
    public void                 setBriefLog( String log ){
        brief_log = log;
    }

    /**@return      Address of brief log file.*/
    public String               getBriefLog(){
        return brief_log;
    }

    /** Set detailed log.
    * @param log    Adress of log file for detailed teaching history.
    */
    public void                 setDetailedLog( String log ){
        detailed_log = log;
    }

    /**@return      Address of detailed log file.*/
    public String               getDetailedLog(){
        return detailed_log;
    }

    /** Set output accuracy.
     * @param accuracy      Size of max accepable difference between input and output when they are considered equal.
     */
    public void                 setOutputAccuracy( double accuracy ){
        output_accuracy = accuracy;
    }

    /**@return      Output accuracy.*/
    public double               getOutputAccuracy(){
        return output_accuracy;
    }

    /** Set idle accuracy.
     * @param accuracy      Size of min accepable difference between old output and output of corrected net
     *                                  when is considered that teach iteration was "useful" ( not idle ).
     */
    public void                 setIdleAccuracy( double accuracy ){
        idle_accuracy = accuracy;
    }

    /**@return      Idle accuracy.*/
    public double               getIdleAccuracy(){
        return idle_accuracy;
    }

    /** Set print accuracy.
     * @param accuracy      Count of numbers in fractional part.
     */
    public void                 setPrintAccuracy( int accuracy ){
        print_accuracy = accuracy;
    }

    /**@return      Print accuracy..*/
    public int                  getPrintAccuracy(){
        return print_accuracy;
    }

    /** @param speed     Teaching speed. */
    public void                 setTeachingSpeed( double speed ){
        teaching_speed = speed;
    }

    /**@return      Teaching speed.*/
    public double               getTeachingSpeed(){
        return teaching_speed;
    }
}
