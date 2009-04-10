package neuro.net;

import neuro.layer.ActiveLayer;
import neuro.activation_func.Sigmoid;
import jblas.matrices.Matrix;

import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/** @author    Vadim Shpakovsky. */

// Invariable multilayer perceptron.
public class StaticPerceptron implements IStaticNet{
    protected int input_height;
    protected int input_width;
    protected int output_size;
    // Number of digits after the decimal for printing real number.
    protected int print_accuracy;
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
    public                      StaticPerceptron( ArrayList<ActiveLayer> layers, int height, int width )
                                    throws Exception{
        if ( layers.get( 0 ).prevSize() != height * width )
        {
            throw new Exception( "Input size != input_width * input_height." );
        }
        this.layers = new ArrayList<ActiveLayer>();
        for ( ActiveLayer layer : layers ){
            this.layers.add( layer.copy() );
        }
        type = "TwoLayerPerceptron";
        this.print_accuracy = 4;
        input_width = width;
        input_height = height;
        output_size = layers.get( layers.size() - 1 ).size();
        output_types = new ArrayList< String >();
    }

    /**Construct static perceptron. Size of inner layers are ( input + output ) / 2.
     * All weights are initialized by 0.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @param output_size       Size of networt outputs.
     * @param layers_count      Count of layers in network.
     */
    public                      StaticPerceptron( int height, int width, int output_size, int layers_count ){
        int inner_layer_size = ( height * width + output_size ) / 2;
        ArrayList<ActiveLayer> layers_list = new ArrayList<ActiveLayer>();        
        Sigmoid s = new Sigmoid();
        // First layer.
        Matrix w = new Matrix( height * width, inner_layer_size );
        ActiveLayer layer = new ActiveLayer( w, s );
        layers_list.add( layer.copy() );
        // Inner layers.
        for ( int i = 0; i < layers_count - 2; i++ ){
            w = new Matrix( inner_layer_size, inner_layer_size );
            layer = new ActiveLayer( w, s);
            layers_list.add( layer.copy() );
        }
        // Last layer.
        w = new Matrix( inner_layer_size, output_size  );
        layer = new ActiveLayer( w, s);
        layers_list.add( layer.copy() );

        this.layers = new ArrayList<ActiveLayer>();
        for ( ActiveLayer next_layer : layers_list ){
            this.layers.add( next_layer.copy() );
        }
        this.type = "TwoLayerPerceptron";
        this.print_accuracy = 4;
        this.input_width = width;
        this.input_height = height;
        this.output_size = output_size;
        this.output_types = new ArrayList< String >();
    }

     /**Construct empty static perceptron.
     */
    public                      StaticPerceptron( ){
        this.layers = new ArrayList<ActiveLayer>();
        this.type = "TwoLayerPerceptron";
        this.print_accuracy = 0;
        this.input_width = 0;
        this.input_height = 0;
        this.output_size = 0;
        this.output_types = new ArrayList< String >();
    }

    /**Make independent copy of itself.
     * @return          Copy of itself.
     */
    public StaticPerceptron     copy() throws Exception{
        StaticPerceptron net = new StaticPerceptron( layers, input_height, input_width );
        net.print_accuracy = this.print_accuracy;
        for ( int i = 0; i < this.output_types.size(); i++ ){
            net.output_types.add( this.output_types.get( i )  );    
        }
        return net;
    }

    /** Print all layers in network to stdout..
     *
     * @param precision     Number of digits after the decimal.
     */
    public void                 print( int precision ){
         for( int i = 0; i < layers.size(); i++ ){
             System.out.printf( "Layer %d:\n", i );
             layers.get( i ).print( precision );
         }
    }

    /** Get net's type.
        @return      String that consist net's type.
     */
    public  String              getType(){
        return type;
    }

    /**
     * @return      Number of neurons in input layer.
     */
    public int                  getInputSize(){
        return input_height * input_width;
    }

    /**
     * @return      Wight of input image.
     */
    public int                  getInputWidth(){
        return input_width;    
    }

    /**
     * @return      Height of input image.
     */
    public int                  getInputHeight(){
        return input_height;
    }

    /**
     * @return      Number of neurons in output layer.
     */
    public int                  getOutputSize(){
        return output_size;
    }

    /**Get list of types, that net can recognize.
     * @return      List of types.
     */
    public ArrayList< String >  getRecognizingTypes(){
        ArrayList< String > types = new ArrayList< String >();
        for ( String type : output_types ){
            types.add( type );
        }
        return types;
    }

    /**
        * @return      Count of layers in net.
        */
    public int                  getLayersCount(){
           return layers.size();
       }

    /**Recognize input image.
     * @param x     Input image.
     * @return      Output result.
     */
    public Matrix               recognize( Matrix x ) throws Exception{
        if ( x.getRowDimension() != layers.get( 0 ).prevSize() ){
            throw new Exception( "Dimentions of net's input and recognizable object are different." );
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
    public ArrayList< Matrix >  traceRecognize( Matrix x ){
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
    public RecognizeType        recognizeClass( Matrix x ) throws Exception{
        Matrix y = this.recognize( x );
        // Get number of max output. It identificate analysis.image's class.
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
	public void                 init( String storage ) throws Exception{
        ObjectInputStream input_stream = null;
        try{
            input_stream = new ObjectInputStream( new FileInputStream( storage ) );
            input_height = ( Integer )input_stream.readObject();
            input_width = ( Integer )input_stream.readObject();
            print_accuracy = ( Integer )input_stream.readObject();
            String read_type = ( String )input_stream.readObject();
            if ( !type.equals( read_type ) ){
                throw new Exception( "Mismatch of net's types. " );
            }
            output_types = ( ArrayList< String > )input_stream.readObject();
            layers = ( ArrayList< ActiveLayer > )input_stream.readObject();
            output_size = layers.get( layers.size() - 1 ).size();
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

    /** Set print accuracy.
     * @param accuracy      Number of digits after the decimal for printing real number.
     */
    public void                 setPrintAccuracy( int accuracy ){
        print_accuracy = accuracy;
    }

    /**@return      Print accuracy.*/
    public int                  getPrintAccuracy(){
        return print_accuracy;
    }
}
