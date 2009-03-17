package neuro.net;

import neuro.Matrix;
import neuro.exception.StopTeachingProgressException;
import neuro.layer.ActiveLayer;
import neuro.layer.Layer;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.image.BufferedImage;

/** @author    Vadim Shpakovsky. */

// Variable multilayer perceptron.
public class TrainingPerceptron extends StaticPerceptron implements ITrainingNet {
    private double teaching_speed;

    /**Pass test image from network. If result is wrong correct weights.
     * IMPORTANT! This correction bring closer network's reaction to right answer on THIS test image,
     * but you need use this function several times for steady result.
     * @param input_x           Matrix of input image.
     * @param reaction          Number of output that is assosiated with test image.
     * @param log               Log stream for teaching history.
     * @param inaccuracy        Size of max accepable difference between input and output
     *                              when output is considered right.
     * @param idle_accuracy    Size of min accepable difference between old output and output of corrected net
     *                              when is considered that teach iteration was "useful" ( not idle ).
     * @return                  True if network recognize test image correctly and false otherwise.
     * @throws StopTeachingProgressException        When teaching iteration not change output.
     */
    private boolean teachTransaction( Matrix input_x, PrintWriter log, int reaction, double inaccuracy, double idle_accuracy )
        throws StopTeachingProgressException, Exception{

        // Run test through the net.
        ArrayList< Matrix > recognition_trace =  this.traceRecognize( input_x );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix target = new Matrix( output_size, 1);
        target.set( reaction, 0, 1. );
        Matrix E = target.minus( y );
//        for ( int i = 0; i < layers.size(); i++ ){
//            log.printf("w_%d =\n", i);
//            layers.get( i ).print( log, 4 );
//        }
//        for ( int i = 0; i < layers.size(); i++ ){
//            log.printf("w_%d * x_%d =\n", i, i);
//            Matrix x_i = recognition_trace.get( i );
//            Matrix w_i = layers.get( i ).getW();
//            x_i.transpose().times( w_i ).print( log, 2, 4 );
//        }
        if ( log != null ){
            log.print("Input:\n");
            input_x.transpose().print( log, 2, 4 );
            log.print("Output:\n");
            y.transpose().print( log, 2, 4 );
            log.print("Target:\n");
            target.transpose().print( log, 2, 4 );
            log.print("E:\n");
            E.transpose().print( log, 2, 4 );
       }

        if ( E.maxNomr() <= inaccuracy ){
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
                log.print("Delta:\n");
                delta.transpose().print( log, 2, 4 );
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
                    y.transpose().print( log, 2, 4 );
                    log.print("New output:\n");
                    new_y.transpose().print( log, 2, 4 );
                }
                throw new StopTeachingProgressException("Error in --TrainingPerceptron.teachTransaction()--");
            }
            else{
                if ( log != null ){
                    log.print("New output:\n");
                    new_y.transpose().print( log, 2, 4 );  
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
    public TrainingPerceptron( ArrayList<ActiveLayer> layers, int height, int width,
                                       double teaching_speed ) throws Exception{
        super( layers, height, width );
        this.teaching_speed = teaching_speed;
    }

    /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public TrainingPerceptron copy()
        throws Exception{
        return new TrainingPerceptron( layers, input_height, input_width, teaching_speed );
    }

    /**
     * Learn the net.
     * @param  learning_path      Absolute path where are all learning tests.
     *      On this path must be subdirectory for each class of recognition objects.
     *      This subdirectory contain all objects belong certain class.
     *      The name of subdirectory identify the name of class.
     * IMPORTANT!!! All files and directories begin with '.' are ignored.
     * @param brief_log_path        Log file for brief teaching history.
     * @param detailed_log_path     Log file for brief teaching history. 
     * @param inaccuracy            Size of max accepable difference between input and output
     *                                  when output is considered right.
     * @param idle_accuracy         Size of min accepable difference between old output and output of corrected net
     *                                  when is considered that teach iteration was "useful" ( not idle ).
     */
    public void train( String learning_path, String brief_log_path, String detailed_log_path,
                       double inaccuracy, double idle_accuracy )
            throws Exception{

        // 1) Get input tests for teaching.
        //      a) Get names of all classes.
        //      b) Associate each class with one output of net.
        //      c) Count number of all teaching tests ( for all classes ).
        //      d) Read all teaching tests in memory.

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
        // Contain all tests.
        ArrayList< ArrayList< Matrix > > inputs = new  ArrayList< ArrayList< Matrix > >();

        // b) Associate classes with outputs that correspond them.
        for ( String class_name : classes ){
            boolean isInList = false;
            for ( String type: output_types ){
                if ( type.equals( class_name ) ){
                    isInList = true;
                    break;
                }
            }
            if ( !isInList ){
                output_types.add( class_name );
            }

            // Contain all tests for current class.
            ArrayList< Matrix > class_inputs = new ArrayList< Matrix >();

            // c) Count number of tests for this class.
            // d) Read teaching tests in memory.

            String full_class_name = learning_path + "\\" + class_name;
            // get all tests for each class
            String[] all_files = new File( full_class_name ).list();
            if ( all_files == null ){
                continue;
            }
            for ( String file_name: all_files ){
                String full_file_name = learning_path + "\\" + class_name + "\\" + file_name;
                File test = new File( full_file_name );
                if ( file_name.charAt( 0 ) != '.' && test.isFile() ){
                    // Read input from file.
                    Matrix input_x = readImage( full_file_name );
                    class_inputs.add( input_x );
                    if ( input_x.getRowDimension() != input_height * input_width ){
                    throw new Exception( "Error --TrainingPerceptron.train( ... )-- " +
                        "Count of pixels in picture != input size of net." );
            }
                    tests_count++;
                }
            }
            inputs.add( class_inputs );
        }

        // 2) Teach net.

        PrintWriter brief_log = null;
        PrintWriter detailed_log = null;
        try{
            // Open log files.
            if ( brief_log_path != null ){
                brief_log = new PrintWriter( brief_log_path );
            }
            if ( detailed_log_path != null ){
                detailed_log = new PrintWriter( detailed_log_path );
            }
            long begin_time = System.currentTimeMillis();
            long iteration = 1;
            // Use tests for teaching one by one in cycle until net recognize all of it.
            while( true ){
                if ( detailed_log != null ){
                    detailed_log.printf( "==========iteration №%d:==========\n", iteration );
                }
                // Count tests from all training set that were correctly recognized by net.
                int positive_result = 0;

                for( int i = 0; i < inputs.size(); i++ ){
                    for( int j = 0; j < inputs.get( i ).size(); j ++ ){
                        try{
                            if ( detailed_log != null ){
                                detailed_log.printf( "------class №%d, test №%d:------\n", i + 1, j + 1 );
                            }
                            Matrix x_input = inputs.get( i ).get( j );
                            if ( teachTransaction( x_input, detailed_log, i , inaccuracy, idle_accuracy ) ){
                                    positive_result++;
                                }
                        }
                        catch( StopTeachingProgressException e ){
                            if ( brief_log != null ){
                                brief_log.printf( "Teaching error!\n" );
                                brief_log.printf( "Number of type: %d.\n", i + 1 );
                                brief_log.printf( "Number of test inside type : %d.\n", j + 1 );
                                brief_log.printf( "Number of iterations: %d.\n", iteration );
                                brief_log.printf( "Image size: %dx%d.\n", input_height, input_width );
                                brief_log.printf( "Idle accuracy: %.2e.\n", idle_accuracy );
                                long delta_time = System.currentTimeMillis() - begin_time;
                                long delta_time_sec = delta_time / 1000;
                                long hours = delta_time_sec / 3600;
                                long min = ( delta_time_sec - hours * 3600  ) / 60;
                                long sec = delta_time_sec - hours * 3600 - min * 60;
                                long msec = delta_time - delta_time_sec * 1000;
                                brief_log.printf( "Net was teaching until error: %d hours, %d min, %d sec, %d msec.\n", hours, min, sec, msec );
                            }
                            return;
                        }
                    }
                }

                // Teaching is finished when all tests are correct.
                if ( positive_result == tests_count ){
                    if ( brief_log != null ){
                        brief_log.printf( "Count of teaching tests: %d.\n", tests_count );
                        brief_log.printf( "Count of teaching iterations: %d.\n", iteration );
                        brief_log.printf( "Image size: %dx%d.\n", input_height, input_width );
                        brief_log.printf( "Precision: %.2e.\n", inaccuracy );
                        long delta_time = System.currentTimeMillis() - begin_time;
                        long delta_time_sec = delta_time / 1000;
                        long hours = delta_time_sec / 3600;
                        long min = ( delta_time_sec - hours * 3600  ) / 60;
                        long sec = delta_time_sec - hours * 3600 - min * 60;
                        long msec = delta_time - delta_time_sec * 1000;
                        brief_log.printf( "Net was teaching: %d hours, %d min, %d sec, %d msec.\n", hours, min, sec, msec );
                    }
                    break;
                }
                else{
                    iteration++;
                }
            }
        }
        catch( FileNotFoundException e){
            throw new Exception("Error --TwoLayerPerceptron.train()-- File not exist." );
        }
        finally{
            try{
                if ( brief_log != null ){
                    brief_log.close();
                }
                if ( detailed_log != null ){
                    detailed_log.close();
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

    /**Convert image to input data for net.
     * @param image_path        Absolute path of image.
     * @return                  Input matrix for net.
     */
//    public Matrix readImage( String image_path )
//        throws Exception{
//        Matrix input_x = new Matrix( input_height * input_width, 1 );
//        File image_file = new File( image_path );
//        try{
//            BufferedImage image = ImageIO.read( image_file );
//            int h = image.getHeight();
//            int w = image.getWidth();
//            if ( h * w != input_height * input_width ){
//                throw new Exception( "Error --TrainingPerceptron.readImage( String )-- " +
//                        "Pixel's number in picture != input size of net." );
//            }
//            // If color not 'black' then he is considered 'white'.
//            for( int j = 0; j < h; ++j ){
//                for( int i = 0; i < w; ++i ){
//                    int rgb = image.getRGB( i, j ) & 0xffffff;
//                    int threshold = 0xffffff / 2;
//                    if ( rgb >= threshold){
//                        input_x.set( i + j * h, 0, 0 );
//                    }
//                    else{
//                        input_x.set( i + j * h, 0, 1 );
//                    }
//
//                }
//            }
//            return input_x;
//        }
//        catch( Exception e ){
//            throw new Exception( "Error --TrainingPerceptron.readImage( String )-- " + e.getMessage() );
//        }
//    }




    /**Convert image to input data for net.
     * @param image_path        Absolute path of image.
     * @return                  Input matrix for net.
     */
    public static Matrix readImage( String image_path )
        throws Exception{

        File image_file = new File( image_path );
        try{
            BufferedImage image = ImageIO.read( image_file );
            int h = image.getHeight();
            int w = image.getWidth();
            Matrix x = new Matrix ( h * w, 1 );
            // If color not 'black' then he is considered 'white'.
            for( int j = 0; j < h; ++j ){
                for( int i = 0; i < w; ++i ){
                    int rgb = image.getRGB( i, j ) & 0xffffff;
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
        catch( Exception e ){
            throw new Exception( "Error --TrainingPerceptron.readImage( String )-- " + e.getMessage() );
        }
    }
}
