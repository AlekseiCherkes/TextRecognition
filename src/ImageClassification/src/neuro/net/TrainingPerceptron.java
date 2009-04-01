package neuro.net;

import jblas.matrices.Matrix;
import neuro.exception.StopTeachingProgressException;
import neuro.layer.ActiveLayer;
import neuro.layer.Layer;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
import java.awt.image.BufferedImage;

import com.trolltech.qt.gui.QImage;
import processing.Binarization;

/** @author    Vadim Shpakovsky. */

// Variable multilayer perceptron.
public class TrainingPerceptron extends StaticPerceptron implements ITrainingNet {
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
    private double teaching_speed;

    /**Pass test image from network. If result is wrong correct weights.
     * IMPORTANT! This correction bring closer network's reaction to right answer on THIS test image,
     * but you need use this function several times for steady result.
     * @param input_x           Matrix of input image.
     * @param reaction          Number of output that is assosiated with test image.
     * @param log               Log stream for teaching history.
     * @return                  True if network recognize test image correctly and false otherwise.
     * @throws StopTeachingProgressException        When teaching iteration not change output.
     */
    private boolean             teachTransaction( Matrix input_x, PrintWriter log, int reaction )
                                    throws StopTeachingProgressException, Exception{

        // Run test through the net.
        ArrayList< Matrix > recognition_trace =  this.traceRecognize( input_x );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix target = new Matrix( output_size, 1);
        target.set( reaction, 0, 1. );
        Matrix E = target.minus( y );
//        for ( int i = 0; i < layers.size(); i++ ){
//            log.printf("w_%d =\n", i);
//            layers.get( i ).print( log, print_accuracy );
//        }
//        for ( int i = 0; i < layers.size(); i++ ){
//            log.printf("w_%d * x_%d =\n", i, i);
//            Matrix x_i = recognition_trace.get( i );
//            Matrix w_i = layers.get( i ).getW();
//            x_i.transpose().times( w_i ).print( log, 2, print_accuracy );
//        }
        if ( log != null ){
//            log.print("Input:\n");
//            input_x.transpose().print( log, 2, print_accuracy );
            log.print("Output:\n");
            y.transpose().print( log, 2, print_accuracy );
            log.print("Target:\n");
            target.transpose().print( log, 2, print_accuracy );
            log.print("E:\n");
            E.transpose().print( log, 2, print_accuracy );
       }

        if ( E.maxNomr() <= output_accuracy ){
            if ( log != null ){
                log.print("OK\n\n");
            }
            return true;
        }
        // Correct weights.
        else{
            if ( log != null ){
                log.print("Correct...\n\n");
            }
            // Corrections weigths for all layers.
            LinkedList< Matrix > layers_dw = new LinkedList< Matrix >();
            // Calculate corrections weights of output layer.
            ActiveLayer out_layer = layers.get( getLayersCount() - 1 );
            Matrix dF = new Matrix( output_size, 1 );
            for ( int i = 0; i < output_size; i++ ){
                dF.set( i, 0, out_layer.getActiveFunc().getDerivative( y.get( i, 0 ) ) );
            }
            Matrix delta = dF.arrayTimes( E );
            if ( log != null ){
                log.print("dF:\n");
                dF.transpose().print( log, 2, print_accuracy );
                log.print("Output delta:\n");
                delta.transpose().print( log, 2, print_accuracy );
            }
            Matrix F = recognition_trace.get( recognition_trace.size() - 2 );
            Matrix dw = F.times( delta.transpose() );

            dw = dw.times( teaching_speed );
            layers_dw.addFirst( dw );

            // Calculate corrections weights of inner layers.
            for ( int i = 1; i < layers.size() ; i++ ){
                ActiveLayer senior_layer = layers.get( getLayersCount() - i );
                ActiveLayer junior_layer = layers.get( getLayersCount() - i - 1 );
                F = recognition_trace.get( recognition_trace.size() - i - 1 );
                dF = new Matrix( junior_layer.size(), 1 );
                for ( int j = 0; j < junior_layer.size(); j++ ){
                    dF.set( j, 0, junior_layer.getActiveFunc().getDerivative( F.get( j, 0 ) ) );
                }
                Matrix inner_delta = new Matrix( junior_layer.size(), 1 );
                for ( int j = 0; j < junior_layer.size(); j++ ){
                    double sum = 0.;
                    for ( int k = 0; k < layers.get( getLayersCount() - i ).size(); k++ ){
                        sum += delta.get( k, 0 ) * senior_layer.getW( j, k );
                    }
                    inner_delta.set( j, 0, sum * dF.get( j, 0 ));
                }
//                if ( log != null ){
//                    log.printf("Delta_%d:\n", i);
//                    inner_delta.transpose().print( log, 2, 4 );
//                }
                F = recognition_trace.get( recognition_trace.size() - i - 2 );
                dw = F.times( inner_delta.transpose() );
                dw = dw.times( teaching_speed );
                layers_dw.addFirst( dw );
            }

            // Correct all weights.
            for ( int i = 0; i < layers.size(); i++ ){
                layers.get( i ).addW( layers_dw.get( i ) );
            }

            // Check if correction gives improvement.
            Matrix new_y = this.recognize( input_x );
            Matrix dy = new_y.minus( y );
            double max = dy.maxNomr();
            if ( max < idle_accuracy ){
                if ( log != null ){
                    log.print("Old output:\n");
                    y.transpose().print( log, 2, print_accuracy );
                    log.print("New output:\n");
                    new_y.transpose().print( log, 2, print_accuracy );
                }
                throw new StopTeachingProgressException();
            }
            else{
                if ( log != null ){
                    log.print("New output:\n");
                    new_y.transpose().print( log, 2, print_accuracy );
                }
            }
        }
        return false;
    }

    /**Construct training perceptron.
     * @param layers            Layers of net.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @param teaching_speed    Coefficient for speed of teaching.
     * @throws Exception        When real size of image mismatch with wanted.
     */
    public                      TrainingPerceptron( ArrayList<ActiveLayer> layers, int height, int width,
                                                    double teaching_speed ) throws Exception{
        super( layers, height, width );
        this.teaching_path = null;
        this.control_path = null;
        this.brief_log = null;
        this.detailed_log = null;
        this.output_accuracy = 0.;
        this.idle_accuracy = 0.;
        this.print_accuracy = 0;
        this.teaching_speed = teaching_speed;
    }

    /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public TrainingPerceptron   copy()
        throws Exception{
        TrainingPerceptron copy_net = new TrainingPerceptron( layers, input_height, input_width, teaching_speed );
        copy_net.setTeachingPath( teaching_path );
        copy_net.setControlPath( control_path );
        copy_net.setBriefLog( brief_log );
        copy_net.setDetailedLog( detailed_log );
        copy_net.setOutputAccuracy( output_accuracy );
        copy_net.setIdleAccuracy( idle_accuracy );
        copy_net.setPrintAccuracy( print_accuracy );
        return copy_net;
    }

    /**
     * @param path          Root directory where are all teaching images.
     * @param image_size    Count of pixels in image.
     * @return              Data necessary for net teaching.
     */
    static public TeachingData  getTeachingData( String path, int image_size )
        throws Exception{
        TeachingData data = new TeachingData();

        // Get names of all types.
        String[] all_dir = new File( path ).list();
        if ( all_dir == null ){
             return data;
        }
        // Ignore directories begin with '.'.
        for ( String dir: all_dir ){
            String type_name = path + "\\" + dir;
            File type_file = new File( type_name );
            if ( dir.charAt( 0 ) != '.' && type_file.isDirectory() ){
                data.addType( dir );
            }
        }

        // Get names of images for each type.
        TreeSet< String > types = data.getTypes();
        for ( String type_name : types ){
            String full_type_name = path + "\\" + type_name;
            String[] all_images = new File( full_type_name ).list();
            if ( all_images == null ){
                continue;
            }
            for ( String image_name: all_images ){
                String full_image_name = path + "\\" + type_name + "\\" + image_name;
                File image = new File( full_image_name );
                if ( image_name.charAt( 0 ) != '.' && image.isFile() ){
                    data.addImage( type_name, image_name );
                    // Read input from file.
                    Matrix input = readImage( full_image_name );
                    if ( input.getRowDimension() != image_size ){
                        throw new Exception( "Invalid count of pixels in image." );
                    }
                    data.addNetInput( type_name, image_name, input );
                }
            }
        }
        return data;
    }

    /** Learn the net.
     */
    public void                 train() throws Exception{

        // 1) Get data for teaching.

        TeachingData teaching_data = getTeachingData( teaching_path, input_height * input_width );
        TeachingData control_data = getTeachingData( control_path, input_height * input_width );

        if ( teaching_data.getImagesCount() == 0 ){
            throw new Exception( "Teaching set is empty." );
        }
        // Number of types in teaching directory must be <= number of types, that net can recognize.
        if ( teaching_data.getTypes().size() > getOutputSize() ){
            throw new Exception("Teaching directory consist " +
                    Integer.toString( teaching_data.getTypes().size() ) + " types, but net can recognize only " +
                    Integer.toString( getOutputSize() ) + ". Reduce number of types or rebild net." );
        }
        // Number of types in control directory must be <= number of types, that contain teaching directory.
        if ( control_data.getTypes().size() > teaching_data.getTypes().size() ){
            throw new Exception("Control directory consist " +
                    Integer.toString( control_data.getTypes().size() ) + " types, but teaching directory contain only " +
                    Integer.toString( teaching_data.getTypes().size() ) + "." );
        }

        // Associate each type with one output of net.
        output_types = new ArrayList< String >();
        TreeSet< String > types = teaching_data.getTypes();
        for ( String type : types ){
            output_types.add( type );
        }

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

            // Use tests for teaching one by one in cycle until net recognize all of it.
            // Follow strategy: for teaching use one image of each type at one iteration
            // ( e.g. when for teaching was used image of current type, next teaching image
            // will find for next type ) .
            while( true ){
                if ( detailed_writer != null ){
                    detailed_writer.printf( "==========iteration №%d:==========\n\n", iteration );
                }
                // Count tests from all training set that were correctly recognized by net.
                int positive_result = 0;

                for( String type : teaching_data.getTypes() ){
                    for ( String image : teaching_data.getImages( type ) ){
                        try{
                            if ( detailed_writer != null ){
                                detailed_writer.printf( "------type '%s', test '%s':------\n\n", type, image );
                            }
                            Matrix x_input = teaching_data.getNetInput( type, image );
                            if ( x_input == null ){
                                throw new Exception( "Image can't be received on net's input." );
                            }
                            Integer reaction = null;
                            for ( int i = 0; i < output_types.size(); i++ ){
                                if ( type.equals( output_types.get( i ) ) ){
                                    reaction = i;
                                    break;
                                }
                            }
                            if ( reaction == null ){
                                throw new Exception( "Net can't recognize images such type." );
                            }
                            if ( teachTransaction( x_input, detailed_writer, reaction ) ){
                                    positive_result++;
                            }
                            // Follow strategy: for teaching use one image of each type at one iteration.
                            else{
                                break;
                            }
                        }
                        // The training progress ends.
                        catch( StopTeachingProgressException e ){
                            if ( brief_writer != null ){
                                brief_writer.printf( "The teaching progress ended. Training was stopped. Net aren't trained.\n" );
                                brief_writer.printf( "Type: '%s'.\n", type );
                                brief_writer.printf( "Image : '%s'.\n", image );
                                brief_writer.printf( "Number of iterations: %d.\n", iteration );
                                brief_writer.printf( "Image size: %dx%d.\n", input_height, input_width );
                                brief_writer.printf( "Idle accuracy: %.2e.\n", idle_accuracy );
                                brief_writer.printf( "Net was teaching until error: %s\n", this.getDeltaTime( start_time ) );
                            }
                            return;
                        }
                    }
                }

                // Use control checking for escape too detailed teaching.
                int control_verified = 0;
                for( String type : control_data.getTypes() ){
                    for ( String image : control_data.getImages( type ) ){
                        Matrix x_input = control_data.getNetInput( type, image );
                        if ( x_input == null ){
                            throw new Exception( "Image can't be received on net's input." );
                        }
                        if ( recognizeClass( x_input ).getType().equals( type ) ){
                                control_verified++;
                        }
                    }
                }
                if ( detailed_writer != null && control_data.getImagesCount() != 0 ){
                    detailed_writer.printf( "_____Control checking result: verify %d  from %d._____\n",
                            control_verified, control_data.getImagesCount() );
                }

                if ( positive_result != teaching_data.getImagesCount() ){
                    // Too detailed teaching.
                    if ( control_data.getImagesCount() > 0 && control_verified == control_data.getImagesCount() ){
                        if ( brief_writer != null ){
                            brief_writer.printf( "Too detailed teaching ( %d/%d ). Training was stopped.\n",
                                    positive_result, teaching_data.getImagesCount() );
                        }
                    }
                    // Not all teaching and control images pass verification.
                    else{
                        iteration++;
                        positive_result = 0;
                        continue;
                    }
                }
                else if ( positive_result == teaching_data.getImagesCount() ){
                    // Teaching set are not representative.
                    if ( control_verified != control_data.getImagesCount() ){
                        if ( brief_writer != null ){
                            brief_writer.printf( "Teachign set are not representative ( control set didn't " +
                                    "pass verification %d/%d).\n", control_verified, control_data.getImagesCount() );
                        }
                    }
                    // Success teaching.
                    if ( control_verified == control_data.getImagesCount() ){
                        if ( brief_writer != null ){
                            brief_writer.println( "Success teaching." );
                        }
                    }
                }

                if ( brief_writer != null ){
                    brief_writer.printf( "Count of teaching tests: %d.\n", teaching_data.getImagesCount() );
                    brief_writer.printf( "Count of teaching iterations: %d.\n", iteration );
                    brief_writer.printf( "Image size: %dx%d.\n", input_height, input_width );
                    brief_writer.printf( "Precision: %.2e.\n", output_accuracy );
                    brief_writer.printf( "Net was teaching: %s\n", this.getDeltaTime( start_time ) );
                }
                // End teaching.
                break;
            }
        }
        finally{
            try{
                if ( brief_writer != null ){
                    brief_writer.close();
                }
                if ( detailed_writer != null ){
                    detailed_writer.close();
                }
            }
            catch( Exception e ){
                e.getMessage();
            }
        }
    }

    /** Initialize all layers with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void                 randomInit( double max_val ){
        for ( Layer layer : layers )
            layer.randomInit( max_val );
    }

     /** Save net in file.
     * @param storage       File for saving.
     * @throws Exception
     */
    public void                 save( String storage ) throws Exception{
        ObjectOutputStream outstream = null;
        try{
            outstream = new ObjectOutputStream( new FileOutputStream( storage ) );
            outstream.writeObject( input_height );
            outstream.writeObject( input_width );
            outstream.writeObject( print_accuracy );
            outstream.writeObject( type );
            outstream.writeObject( output_types );
            outstream.writeObject( layers );
        }
        finally{
            if ( outstream != null ){
                try{
                    outstream.close();
                }
                catch( Exception e ){
                    e.getMessage();
                }
            }
        }
    }

     /**Convert image to input data for net.
     * @param image_path        Absolute path of image.
     * @return                  Input matrix for net.
     */
    public static Matrix        readImage( String image_path ) throws Exception{
        File image_file = new File( image_path );
        BufferedImage bi_image = Binarization.work( ImageIO.read( image_file ) );
        int h = bi_image.getHeight();
        int w = bi_image.getWidth();
        Matrix x = new Matrix ( h * w, 1 );
        for( int j = 0; j < h; ++j ){
            for( int i = 0; i < w; ++i ){
                int rgb = bi_image.getRGB( i, j ) & 0xffffff;
                if ( rgb == 0){
                    x.set( i + j * h, 0, 1 );
                }
                else{
                    x.set( i + j * h, 0, 0 );
                }
            }
        }
        return x;
    }

     /**Convert image to input data for net.
     * @param image        Qt image object.
     * @return             Input matrix for net.
     */
    public static Matrix        readImage( QImage image ) throws Exception{

        int h = image.height();
        int w = image.width();
        Matrix x = new Matrix ( h * w, 1 );
        for( int j = 0; j < h; ++j ){
            for( int i = 0; i < w; ++i ){
                int rgb = image.pixel( i, j ) & 0xffffff;
//                double val = min_input + rgb * ( max_input - min_input ) / 0xffffff;
//                x.set( i + j * h, 0, val );
                int threshold = 0xffffff / 2;
                if ( rgb >= threshold){
                    x.set( i + j * h, 0, 0 );
                }
                else{
                    x.set( i + j * h, 0, 1 );
                }
            }
        }
        return x;
    }
    
    /**
     * @param start_time    Start time in msec.
     * @return              String with time passed since 'start_time'.
     */
    String getDeltaTime( long start_time ){
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

}