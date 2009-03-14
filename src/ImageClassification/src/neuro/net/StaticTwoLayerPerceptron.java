package neuro.net;

import neuro.layer.ActiveLayer;
import neuro.Matrix;
import neuro.net.RecognizeType;

import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/** @author    Vadim Shpakovsky. */

public class StaticTwoLayerPerceptron implements IStaticNet{      
    protected int input_height;
    protected int input_width;
    protected int output_size;
    protected String type;
     // Contains all classes that this network can recognize.
    // Position's number in this collection indicate number of output for it class in network.
    //private TreeMap< Integer, String > output_types;
    protected ArrayList< String > output_types;
    protected ArrayList< ActiveLayer > layers;

    /**Construct static two layer perceptron.
     * @param layers            Layers of net.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @throws Exception        When real size of image mismatch with wanted.
     */
    public StaticTwoLayerPerceptron( ArrayList<ActiveLayer> layers, int height, int width )
            throws Exception{
        if ( layers.get( 0 ).prevSize() != height * width )
        {
            throw new Exception("Error --" +
                    "StaticTwoLayerPerceptron.StaticTwoLayerPerceptron( ArrayList<ActiveLayer>, int int )-- " +
                    "Input size != input_width * input_height." );
        }
        this.layers = new ArrayList<ActiveLayer>();
        for (int i = 0; i < layers.size(); i++ ){
            this.layers.add( layers.get( i ).copy() );
        }
        type = "TwoLayerPerceptron";
        input_width = width;
        input_height = height;
        output_size = layers.get( layers.size() - 1 ).size();
        //output_types = new TreeMap< Integer, String >();
        output_types = new ArrayList< String >();
    }

     /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public StaticTwoLayerPerceptron copy()
        throws Exception{
        return new StaticTwoLayerPerceptron( layers, input_height, input_width );
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

    /** Get net's type.
        @return      String that consist net's type.
     */
    public  String getType(){
        return type;
    }

    /**
     * @return      Number of neurons in input layer.
     */
    public int getInputSize(){
        return input_height * input_width;
    }

    /**
     * @return      Wight of input image.
     */
    public int getInputWidth(){
        return input_width;    
    }

    /**
     * @return      Height of input image.
     */
    public int getInputHeight(){
        return input_height;
    }

    /**
     * @return      Number of neurons in output layer.
     */
    public int getOutputSize(){
        return output_size;
    }

    /**Get list of types, that net can recognize.
     * @return      List of types.
     */
    public ArrayList< String > getRecognizingTypes(){
        ArrayList< String > types = new ArrayList< String >();
        for ( int i = 0; i < output_types.size(); i++ ){
            types.add( output_types.get( i ) );
        }
        return types;
    }

    /**
        * @return      Count of layers in net.
        */
       public int getLayersCount(){
           return layers.size();
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

    /**Recognize class for input image.
     * @param x     Input image.
     * @return      Class of image. If net coudn't classificate image return 'null'.
     */
    public RecognizeType recognizeClass( Matrix x ){
        Matrix y = this.recognize( x );
        // Get number of max output. It identificate image's class.
        int output_num = 0;
        double max = y.get( 0, 0 );
        for ( int i = 0; i < y.getRowDimension(); i++ ){
            if ( y.get( i, 0 ) > max ){
                max = y.get( i, 0 );
                output_num = i;
            }
        }
        if ( output_num >= output_types.size() ){
            return null;
        }
        else{
            double prob_sum = y.norm1();
            return  new RecognizeType( output_types.get( output_num ), max / prob_sum );
        }
    }

    /** Initialize net from file.
     * @param storage       File for loading.
     * @throws Exception
     */
	public void init( String storage ) throws Exception{
        ObjectInputStream input_stream = null;
        try{
            input_stream = new ObjectInputStream( new FileInputStream( storage ) );
            input_height = ( Integer )input_stream.readObject();
            input_width = ( Integer )input_stream.readObject();
            if ( type != ( String )input_stream.readObject() ){
                throw new Exception( "Error --StaticTwoLayerPerceptron.init( String )-- " +
                        "Mismatch of net's types. " );
            }
            output_types = ( ArrayList< String > )input_stream.readObject();
            layers = ( ArrayList< ActiveLayer > )input_stream.readObject();
            output_size = layers.get( layers.size() - 1 ).size();
        }
        catch( Exception e){
            throw new Exception( "Error --StaticTwoLayerPerceptron.init( String )-- " + e.getMessage() );
        }
        finally{
            if ( input_stream != null ){
                    try{
                        input_stream.close();
                    }
                    catch( Exception e ){
                        e.getMessage();
                    }
                }
        }
    }
}
