package neuron_net;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 17:07:15
 * To change this template use File | Settings | File Templates.
 */
public class TwoLayerPerceptron extends Net{
    private ArrayList< Layer > layers;
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
     *          when output is considered right.
     * @return              True if network recognize test image correctly and false otherwise.
     */
    private boolean teachTransaction( String test_path, int reaction, double precision ){
        
        return false;    
    }

    public TwoLayerPerceptron( ArrayList<Layer> layers ){
        this.layers = new ArrayList<Layer>();
        for (int i = 0; i < layers.size(); i++ ){
            this.layers.add( layers.get( i ).copy() );
        }
        type = "TwoLayerPerceptron";
        input_size = layers.get( 0 ).size();
        output_size = layers.get( layers.size() - 1 ).size();
        output_types = new TreeMap< String, Integer >();
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
                // Size of double is 64 bytes.
                if ( new File( tests[j] ).length() != getInputSize() * 64 ){
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
        return new TwoLayerPerceptron( this.layers );
    }

    /** Initialize all layers with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void randomInit( double max_val ){
        for ( Layer layer : layers )
            layer.randomInit( max_val );
    }
}
