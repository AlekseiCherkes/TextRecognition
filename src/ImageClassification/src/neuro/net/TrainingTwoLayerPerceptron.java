package neuro.net;

import neuro.Matrix;
import neuro.layer.ActiveLayer;
import neuro.layer.Layer;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

/** @author    Vadim Shpakovsky. */

public class TrainingTwoLayerPerceptron extends StaticTwoLayerPerceptron implements ITrainingNet {
    private double teaching_speed;

     /**Pass test image from network. If result is wrong correct weights.
     * IMPORTANT! This correction bring closer network's reaction to right answer on THIS test image,
     * but you need use this function several times for steady result.
     * @param test_path     Absolute path to file with test image.
     * @param reaction      Number of output that is assosiated with test image.
     * @param log           Log stream for teaching history.
     * @param precision     Size of max accepable difference between input and output
     *                          when output is considered right.
     * @return              True if network recognize test image correctly and false otherwise.
     * @throws Exception
     */
    private boolean teachTransaction( String test_path, PrintWriter log, int reaction, double precision )
            throws Exception {
        // Read input from file.
        Matrix input_x = null;
        Scanner test = null;
        try{
            test = new Scanner( new File(test_path) );
            input_x = new Matrix( getInputSize(), 1 );
            for ( int i = 0; i < getInputSize(); i++ ){
                double d =  test.nextDouble();
                input_x.set( i, 0, d );
            }
        }
        catch( Exception e){
            throw new Exception( "Error. --TwoLayerPerceptron.teachTransaction()-- " +
                    "Problem with reading image from file." + e.getMessage() );
        }
        finally{
            if(  test != null ){
                try{
                    test.close();
                }
                catch( Exception e ){
                    throw e;
                }
            }
        }

        // Run test through the net.
        ArrayList< Matrix > recognition_trace =  traceRecognize( input_x );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix target = new Matrix( output_size, 1);
        target.set( reaction, 0, 1. );
        Matrix E = target.minus( y );
//        for ( int i = 0; i < layers.size(); i++ ){
//            log.printf("w_%d =\n", i);
//            layers.get( i ).print( log, 4 );
//        }
       if ( log != null ){
            log.print("Output:\n");
            y.transpose().print( log, 2, 4 );
            log.print("Target:\n");
            target.transpose().print( log, 2, 4 );
            log.print("E:\n");
            E.transpose().print( log, 2, 4 );
       }

        if ( E.maxNomr() <= precision ){
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
                F = recognition_trace.get( recognition_trace.size() - i - 2 );
                dw = F.times( inner_delta.transpose() );
                dw = dw.times( teaching_speed );
                layers_dw.addFirst( dw );
            }

            // Correct all weights.
            for ( int i = 0; i < layers.size(); i++ ){
                layers.get( i ).addW( layers_dw.get( i ) );
            }

        }
        return false;
    }

    /**Construct training two layer perceptron.
     * @param layers            Layers of net.
     * @param teaching_speed    Coefficient for speed of teaching.
     */
    public TrainingTwoLayerPerceptron( ArrayList<ActiveLayer> layers, int height, int width,
                                       double teaching_speed ) throws Exception{
        super( layers, height, width );
        this.teaching_speed = teaching_speed;
    }

    /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public TrainingTwoLayerPerceptron copy()
        throws Exception{
        return new TrainingTwoLayerPerceptron( layers, input_height, input_width, teaching_speed );
    }


    /**
     * Learn the net.
     * @param  learning_path      Absolute path where are all learning tests.
     *      On this path must be subdirectory for each class of recognition objects.
     *      This subdirectory contain all objects belong certain class.
     *      The name of subdirectory identify the name of class.
     * IMPORTANT!!! All files and directories begin with '.' are ignored.
     * @param log_file            Log file for teaching history.
     * @param precision           Size of max accepable difference between input and output
     *      when output is considered right.
     */
    public void train( String learning_path, String log_file, double precision )
            throws Exception{

        // 1) Get input tests for teaching.
        //      a) Get names of all classes.
        //      b) Associate each class with one output of net.
        //      c) Count number of all teaching tests ( for all classes ).

        // a) Get names of all classes.
        String[] all_dir = new File( learning_path ).list();
        if ( all_dir == null ){
             throw new Exception( "Error --TwoLayerPerceptron.train()-- Wrong learning directory or I/O error occurs." );
        }
        ArrayList< String > classes = new ArrayList< String >();
        // Ignore directories begin with '.'.
        for ( String dir: all_dir ){
            String class_name = learning_path + "\\" + dir;
            File class_file = new File( class_name );
            if ( dir.charAt( 0 ) != '.' && class_file.isDirectory() ){
                classes.add( dir );
            }
        }
        // Number of classes must be <= number of types, that net can recognize.
        if ( classes.size() > getOutputSize() ){
            throw new Exception("Error --TwoLayerPerceptron.train()-- Learning directory consist " +
                    Integer.toString( classes.size() ) + " classes, but net can recognize only " +
                    Integer.toString( getOutputSize() ) + ". Reduce number of classes or rebild net." );
        }
        if ( classes.size() == 0 ){
            throw new Exception( "Error --TwoLayerPerceptron.train()-- Learning directory doesn't contain classes subdirectories." );
        }

        // Count of tests for all classes.
        int tests_count = 0;

        // b) Associate classes with outputs that correspond them.
        for ( int i = 0; i < classes.size(); i++ ){
            boolean isInList = false;
            for ( String type: output_types ){
                if ( type.equals( classes.get( i ) ) ){
                    isInList = true;
                    break;
                }
            }
            if ( !isInList ){
                output_types.add( classes.get( i ) );
            }

            // c) Count number of tests for this class.

            String full_class_name = learning_path + "\\" + classes.get( i );
            // get all tests for each class
            String[] all_files = new File( full_class_name ).list();
            if ( all_files == null ){
                continue;
            }
            //ArrayList< String > tests = new ArrayList< String >();
            for ( String file: all_files ){
                String file_name = learning_path + "\\" + classes.get( i ) + "\\" + file;
                File test = new File( file_name );
                if ( file.charAt( 0 ) != '.' && test.isFile() ){
                    tests_count++;
                }
            }
        }

        // 2) Teach net.

        PrintWriter log = null;
        try{
            // Open log file.
            if ( log_file != null ){
                log = new PrintWriter( log_file );
            }
            int iteration = 1;
            // Use tests for teaching one by one in cycle until net recognize all of it.
            while( true ){
                if ( log_file != null ){
                    log.printf( "==========iteration №%d:==========\n", iteration );
                }
                // Count tests from all training set that were correctly recognized by net.
                int positive_result = 0;
                // Go over all classes.
                for ( int i = 0; i < classes.size(); i++ ){
                    // get all tests for each class;
                    String full_class_name = learning_path + "\\" + classes.get( i );
                    String[] all_files = new File( full_class_name ).list();
                    if ( all_files == null ){
                        continue;
                    }
                    // Go over all teaching tests of current class.
                    for ( String file: all_files ){
                        String file_name = learning_path + "\\" + classes.get( i ) + "\\" + file;
                        File test = new File( file_name );
                        if ( test == null ){
                            throw new Exception("Error --TwoLayerPerceptron.train()-- I/O error occurs.");
                        }
                        if ( file.charAt( 0 ) != '.' && test.isFile() ){
                            if ( log_file != null ){
                                log.printf( "-----test \"%s\"-----\n", file );
                            }
                            if ( teachTransaction( file_name, log, i , precision ) ){
                                positive_result++;
                            }
                        }
                    }
                }
                // Teaching is finished when all tests are correct.
                if ( positive_result == tests_count ){
                    break;
                }
                else{
                    iteration++;
                }
            }
        }
        catch( FileNotFoundException e){
            throw e;
        }
        finally{
             if ( log != null ){
                    try{
                        log.close();
                    }
                    catch( Exception e ){
                        e.getMessage();
                    }
                }
        }
    }

    /** Initialize all layers with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void randomInit( double max_val ){
        for ( Layer layer : layers )
            layer.randomInit( max_val );
    }

     /** Save net in file.
     * @param storage       File for saving.
     * @throws Exception
     */
    public void save( String storage ) throws Exception{
        ObjectOutputStream outstream = null;
        try{
            outstream = new ObjectOutputStream( new FileOutputStream( storage ) );
            outstream.writeObject( input_height );
            outstream.writeObject( input_width );
            outstream.writeObject( type );
            outstream.writeObject( output_types );
            outstream.writeObject( layers );
        }
        catch( Exception e){
            throw new Exception( "Error --Recognizer.save()-- " + e.getMessage() );
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
}
