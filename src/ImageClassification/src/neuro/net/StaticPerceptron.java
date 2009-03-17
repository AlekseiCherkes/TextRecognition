package neuro.net;

import neuro.layer.ActiveLayer;
import neuro.Matrix;

import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/** @author    Vadim Shpakovsky. */

// Invariable multilayer perceptron.
public class StaticPerceptron implements IStaticNet{
    protected int input_height;
    protected int input_width;
    protected int output_size;
    protected String type;
     // Contains all classes that this network can recognize.
    // Position's number in this collection indicate number of output for it class in network.
    //private TreeMap< Integer, String > output_types;
    protected ArrayList< String > output_types;
    protected ArrayList< ActiveLayer > layers;

    /**Construct static perceptron.
     * @param layers            Layers of net.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @throws Exception        When real size of image mismatch with wanted.
     */
    public StaticPerceptron( ArrayList<ActiveLayer> layers, int height, int width )
            throws Exception{
        if ( layers.get( 0 ).prevSize() != height * width )
        {
            throw new Exception("Error --" +
                    "StaticPerceptron.StaticPerceptron( ArrayList<ActiveLayer>, int int )-- " +
                    "Input size != input_width * input_height." );
        }
        this.layers = new ArrayList<ActiveLayer>();
        for ( ActiveLayer layer : layers ){
            this.layers.add( layer.copy() );
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
    public StaticPerceptron copy()
        throws Exception{
        return new StaticPerceptron( layers, input_height, input_width );
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
        for ( String type : output_types ){
            types.add( type );
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
    public Matrix recognize( Matrix x )
        throws Exception{
        if ( x.getRowDimension() != layers.get( 0 ).prevSize() ){
            throw new Exception( "--StaticPerceptron.recognize( Matrix )-- Dimentions of net's input " +
                    "and recognizable object are different." );
        }
        for ( ActiveLayer layer : layers ){
            x = layer.activateLayer( x );
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
        for ( ActiveLayer layer : layers ){
            x = layer.activateLayer( x );
            trace.add( x );
        }
        return trace;
    }

    /**Recognize class for input image.
     * @param x     Input image.
     * @return      Class of image. If net coudn't classificate image return 'null'.
     */
    public RecognizeType recognizeClass( Matrix x )
        throws Exception{
        try{
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
        catch ( Exception e ){
            throw new Exception("--StaticPerceptron.recognizeClass( Matrix )-- " + e.getMessage());
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
            String read_type = ( String )input_stream.readObject();
            if ( !type.equals( read_type ) ){
                throw new Exception( "Error --StaticPerceptron.init( String )-- " +
                        "Mismatch of net's types. " );
            }
            output_types = ( ArrayList< String > )input_stream.readObject();
            layers = ( ArrayList< ActiveLayer > )input_stream.readObject();
            output_size = layers.get( layers.size() - 1 ).size();
        }
        catch( Exception e){
            throw new Exception( "Error --StaticPerceptron.init( String )-- " + e.getMessage() );
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
