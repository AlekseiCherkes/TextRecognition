package neuron_net;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
     * @param precision     Size of max accepable difference between input and output
     *                          when output is considered right.
     * @return              True if network recognize test image correctly and false otherwise.
     */
    private boolean teachTransaction( String test_path, int reaction, double precision )
            throws IOException {
        // Read input from file.
        Scanner test = new Scanner( new File(test_path) );
        Matrix input_x = new Matrix( input_size, 1 );
        for ( int i = 0; i < input_size; i++ ){
            input_x.set( 1, i, test.nextDouble() );
        }
        // Run test through the net.
        ArrayList< Matrix > recognition_trace =  traceRecognize( input_x.transpose() );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix E = new Matrix( output_size, 1);
        E.set( reaction, 0, 1. );
        E = E.minus( y );
        if ( E.maxNomr() <= precision ){
            return true;
        }
        // Correct weights.
        else{

            // Corrections weigths for all layers.
            LinkedList< Matrix > layers_dw = new LinkedList< Matrix >();
            // Calculate corrections weights of output layer.
            Layer out_layer = layers.get( getLayersCount() - 1 );
            Matrix dF = new Matrix( output_size, 1 );
            for ( int i = 0; i < output_size; i++ ){
                dF.set( i, 0, out_layer.getActiveFunc().getDerivative( y.get( i, 0 ) ) );
            }
            Matrix delta = dF.arrayTimes( E );
            Matrix dw = delta.arrayTimes( y );
            dw = dw.times( teaching_speed );
            layers_dw.addFirst( dw );

            // Calculate corrections weights of inner layers.
            for ( int i = 1; i < recognition_trace.size() ; i++ ){
                Layer senior_layer = layers.get( getLayersCount() - i );
                Layer junior_layer = layers.get( getLayersCount() - i - 1 );
                Matrix x = recognition_trace.get( recognition_trace.size() - i - 1 );
                Matrix F = junior_layer.activateLayer( x );
                dF = new Matrix( junior_layer.size(), 1 );
                for ( int j = 0; j < junior_layer.size(); j++ ){
                    dF.set( j, 0, junior_layer.getActiveFunc().getDerivative( F.get( j, 0 ) ) );
                }
                for ( int j = 0; j < junior_layer.size(); j++ ){
                    double sum = 0.;
                    for ( int k = 0; k < layers.get( getLayersCount() - i ).size(); k++ ){
                        sum += delta.get( k, 0 ) * senior_layer.getW( j, k );
                    }
                    delta.set( j, 0, sum * dF.get( j, 0 ));
                }
                dw = delta.arrayTimes( F );
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
        input_size = layers.get( 0 ).size();
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
     * @param precision           Size of max accepable difference between input and output
     *      when output is considered right.
     */
    public void train( String learning_path, double precision )
            throws Exception{
        // get names of all classes
        String[] classes = new File( learning_path ).list();

        // Check correctness of learning test structure.
        // Associate classes with outputs that correspond them.
        // Count number of all tests.
        int tests_count = 0;
        if ( classes == null){
            throw new Exception( "Wrong learning directory." );
        }
        for ( int i = 0; i < classes.length; i++ ){
            // Associate.
            output_types.put( classes[i], i );
            String class_name = learning_path + "\\" + classes[i];
            // get all tests for each class
            String[] tests = new File( class_name ).list();
            tests_count += tests.length;
            if ( tests == null ){
                throw new Exception("Learning directory consist strange files.");
            }
            if ( tests.length > getOutputSize() ){
                throw new Exception("Learning directory consist " +
                        Integer.toString( tests.length ) + " classes, but network can recognize only " +
                        Integer.toString( getOutputSize() ) + ". Reduce number of classes." );
            }
            for ( int j = 0; j < classes.length; j++ ){
                // Size of double is 8 bytes.
                if ( new File( tests[j] ).length() != getInputSize() * 8 ){
                    throw new Exception("Invalid size of training file.");
                }
            }
        }

        // Use tests for teaching one by one in cycle until net recognize all of it.
        while( true ){
            // Count tests from all training set that were correctly recognized by net.
            int positive_result = 0;
            for ( int i = 0; i < classes.length; i++ ){
                // get all tests for each class;
                String class_name = learning_path + "\\" + classes[i];
                String[] tests = new File( class_name ).list();
                for ( int j = 0; j < tests.length; j++ ){
                    String test_name = class_name + "\\" + tests[i];
                    if ( teachTransaction( test_name, output_types.get( tests[i] ) , precision ) ){
                        positive_result++;
                    }
                }
            }
            // Teaching is finished when all tests are correct.
            if ( positive_result == tests_count ){
                break;
            }
        }
    }

    public Matrix recognize( Matrix x ){
        return null;
    }

    /**Recognize input image. Get trace with each layer reconition.
     * @param x     Input image.
     * @return      Trace with each layer reconition.
     */
    public ArrayList< Matrix > traceRecognize( Matrix x ){
        ArrayList< Matrix > trace = new ArrayList< Matrix >();
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
