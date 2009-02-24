package neuro;

import java.util.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 17:07:15
 * To change this template use File | Settings | File Templates.
 */
public class TwoLayerPerceptron extends Net{
    private ArrayList< Layer > layers;
    private double teaching_speed;
    private int input_size;
    private int output_size;
    // Contains all classes that this network can recognize.
    // Position's number in this collection indicate number of output for it class in network.
    private TreeMap< String, Integer > output_types;

    /**Pass test image from network. If result is wrong correct weights.
     * IMPORTANT! This correction bring closer network's reaction to right answer on THIS test image,
     * but you need use this function several times for steady result.
     * @param test_path     Absolute path to file with test image.
     * @param reaction      Number of output that is assosiated with test image.
     * @param log           Log stream for teaching history.
     * @param precision     Size of max accepable difference between input and output
     *                          when output is considered right.
     * @return              True if network recognize test image correctly and false otherwise.
     */
    private boolean teachTransaction( String test_path, PrintWriter log, int reaction, double precision )
            throws IOException {
        // Read input from file.
        Scanner test = new Scanner( new File(test_path) );
        Matrix input_x = new Matrix( input_size, 1 );
        for ( int i = 0; i < input_size; i++ ){
            double d =  test.nextDouble();
            input_x.set( i, 0, d );
        }
        // Run test through the net.
        ArrayList< Matrix > recognition_trace =  traceRecognize( input_x );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix target = new Matrix( output_size, 1);
        target.set( reaction, 0, 1. );
        Matrix E = target.minus( y );
        for ( int i = 0; i < layers.size(); i++ ){
            log.printf("w_%d =\n", i);
            layers.get( i ).print( log, 4 );
        }
        log.print("Output:\n");
        y.transpose().print( log, 2, 4 );
        log.print("Target:\n");
        target.transpose().print( log, 2, 4 );
        log.print("E:\n");
        E.transpose().print( log, 2, 4 );

        if ( E.maxNomr() <= precision ){
            log.print("OK\n");
            return true;
        }
        // Correct weights.
        else{
            log.print("Correct...\n");
            // Corrections weigths for all layers.
            LinkedList< Matrix > layers_dw = new LinkedList< Matrix >();
            // Calculate corrections weights of output layer.
            Layer out_layer = layers.get( getLayersCount() - 1 );
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
                Layer senior_layer = layers.get( getLayersCount() - i );
                Layer junior_layer = layers.get( getLayersCount() - i - 1 );
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

    public TwoLayerPerceptron( ArrayList<Layer> layers, double teaching_speed ){
        this.layers = new ArrayList<Layer>();
        for (int i = 0; i < layers.size(); i++ ){
            this.layers.add( layers.get( i ).copy() );
        }
        type = "TwoLayerPerceptron";
        input_size = layers.get( 0 ).prevSize();
        output_size = layers.get( layers.size() - 1 ).size();
        output_types = new TreeMap< String, Integer >();
        this.teaching_speed = teaching_speed;
    }

    /**
     * @return      Number of neurons in input layer.
     */
    public int getInputSize(){
        return input_size;
    }

    /**
     * @return      Number of neurons in output layer.
     */
    public int getOutputSize(){
        return output_size;
    }

    /**
     * @return      Count of layers in net.
     */
    public int getLayersCount(){
        return layers.size();
    }

    /**
     * Learn the net.
     * @param  learning_path      Absolute path where are all learning tests.
     *      On this path must be subdirectory for each class of recognition objects.
     *      This subdirectory contain all objects belong certain class.
     *      The name of subdirectory identify the name of class.
     * @param log_file            Log file for teaching history.
     * @param precision           Size of max accepable difference between input and output
     *      when output is considered right.
     */
    public void train( String learning_path, String log_file, double precision )
            throws Exception{       
        // Get names of all classes..
        // Ignore directories begin with '.'.
        String[] all_dir = new File( learning_path ).list();
        if ( all_dir == null ){
             throw new Exception( "Error --TwoLayerPerceptron.train()-- Wrong learning directory or I/O error occurs." );
        }
        ArrayList< String > classes = new ArrayList< String >();
        for ( String dir: all_dir ){
            String class_name = learning_path + "\\" + dir;
            File class_file = new File( class_name );
            if ( dir.charAt( 0 ) != '.' && class_file.isDirectory() ){
                classes.add( dir );
            }
        }
        if ( classes.size() > getOutputSize() ){
            throw new Exception("Error --TwoLayerPerceptron.train()-- Learning directory consist " +
                    Integer.toString( classes.size() ) + " classes, but net can recognize only " +
                    Integer.toString( getOutputSize() ) + ". Reduce number of classes or rebild net." );
        }

        // Check correctness of learning test structure.
        // Associate classes with outputs that correspond them.
        if ( classes.size() == 0 ){
            throw new Exception( "Error --TwoLayerPerceptron.train()-- Learning directory doesn't contain classes subdirectories." );
        }
        int tests_count = 0;
        for ( int i = 0; i < classes.size(); i++ ){
            // Associate.
            output_types.put( classes.get( i ), i );
            String full_class_name = learning_path + "\\" + classes.get( i );
            // get all tests for each class
            String[] all_files = new File( full_class_name ).list();
            if ( all_files == null ){
                //throw new Exception("I/O error occurs.");
                continue;
            }
            ArrayList< String > tests = new ArrayList< String >();
            for ( String file: all_files ){
                String file_name = learning_path + "\\" + classes.get( i ) + "\\" + file;
                File test = new File( file_name );
                if ( file.charAt( 0 ) != '.' && test.isFile() ){
                    tests_count++;
                    // Check file's size.
//                    if ( test.length() != getInputSize() * 8 ){
//                        throw new Exception("Error --TwoLayerPerceptron.train()-- Invalid size of training file.");
//                    }
                }
            }
        }

        PrintWriter log = null;
        try{
            // Open log file.
            log = new PrintWriter( log_file );
            int iteration = 1;
            // Use tests for teaching one by one in cycle until net recognize all of it.
            while( true ){
                log.printf( "===iteration №%d:===\n", iteration );
                // Count tests from all training set that were correctly recognized by net.
                int positive_result = 0;
                for ( int i = 0; i < classes.size(); i++ ){
                    // get all tests for each class;
                    String full_class_name = learning_path + "\\" + classes.get( i );
                    String[] all_files = new File( full_class_name ).list();
                    if ( all_files == null ){
                        //throw new Exception("I/O error occurs.");
                        continue;
                    }
                    for ( String file: all_files ){
                        String file_name = learning_path + "\\" + classes.get( i ) + "\\" + file;
                        File test = new File( file_name );
                        if ( test == null ){
                            throw new Exception("Error --TwoLayerPerceptron.train()-- I/O error occurs.");
                        }
                        if ( file.charAt( 0 ) != '.' && test.isFile() ){
                            log.printf( "test \"%s\":\n", file );
                            if ( teachTransaction( file_name, log, output_types.get( classes.get( i ) ) , precision ) ){
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

    /**Recognize input image.
     * @param x     Input image.
     * @return      Output result.
     */
    public Matrix recognize( Matrix x ){
        for ( int i = 0; i < layers.size(); i++ ){
            x = layers.get( i ).activateLayer( x );
        }
        return x;
    }

    /**Recognize input image. Get trace with each layer reconition.
     * @param x     Input image.
     * @return      Trace with output of each layer. Begin with input and end with output.
     */
    public ArrayList< Matrix > traceRecognize( Matrix x ){
        ArrayList< Matrix > trace = new ArrayList< Matrix >();
        trace.add( x );
        for ( int i = 0; i < layers.size(); i++ ){
            x = layers.get( i ).activateLayer( x );
            trace.add( x );
        }
        return trace;
    }

    /** Print all layers in network to stdout..
     *
     * @param precision     Number of digits after the decimal.
     */
    public void print( int precision ){
         for( int i = 0; i < layers.size(); i++ ){
             System.out.printf( "Layer %d:\n", i );
             layers.get( i ).print( precision );
         }
    }

    /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public TwoLayerPerceptron copy(){
        return new TwoLayerPerceptron( this.layers, teaching_speed );
    }

    /** Initialize all layers with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void randomInit( double max_val ){
        for ( Layer layer : layers )
            layer.randomInit( max_val );
    }
}
