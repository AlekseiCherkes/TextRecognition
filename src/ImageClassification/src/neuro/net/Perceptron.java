package neuro.net;

import jblas.matrices.Matrix;

import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import neuro.layer.ActiveLayer;
import neuro.layer.Layer;
import neuro.activation_func.Sigmoid;

/**
 * @author Vadim Shpakovsky.
 */

// Variable multilayer perceptron.
public class Perceptron implements INet{
    private ArrayList< ActiveLayer > layers;
    private int input_height;
    private int input_width;
    private int output_size;
    private String type;
     // Contains all classes that this network can recognize.
    // Position's number in this collection indicate number of output for it class in network.
    //private TreeMap< Integer, String > output_types;
    private ArrayList< String > output_types;

    /**Construct empty static perceptron.*/
    public                      Perceptron(){
        layers = new ArrayList<ActiveLayer>();
        type = "Perceptron";
        output_types = new ArrayList< String >();
    }

    /**Set the structure of net.
     * @param layers            Layers of net.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @throws Exception        When real dimensions of input mismatch with wanted.
     */
    public void                 setStructure( ArrayList<ActiveLayer> layers, int height, int width )
                                     throws Exception{
        if ( layers.get( 0 ).prevSize() != height * width )
        {
            throw new Exception( "Input size != input_width * input_height." );
        }
        this.layers = new ArrayList<ActiveLayer>();
        for ( ActiveLayer layer : layers ){
            this.layers.add( layer.copy() );
        }
        input_width = width;
        input_height = height;
        output_size = layers.get( layers.size() - 1 ).size();
        output_types = new ArrayList< String >();
    }

    /**Set the structure of net. Size of inner layers are ( input + output ) / 2.
     * All weights are initialized by 0.
     * @param height            Height of input image.
     * @param width             Width of input image.
     * @param output_size       Size of network outputs.
     * @param layers_count      Count of layers in network.
     */
    public void                 setStructure( int height, int width, int output_size, int layers_count ){
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
        this.input_width = width;
        this.input_height = height;
        this.output_size = output_size;
    }

    /**Recognize input image.
     * @param x             Input image.
     * @return              Output result.
     * @throws Exception    When net can't recognize image because of image's size.
     */
    public Matrix               recognize( Matrix x ) throws Exception{
        if ( x.getRowDimension() != layers.get( 0 ).prevSize() ){
            throw new Exception( "Net can't recognize image because of it's size." );
        }
        for ( ActiveLayer layer : layers ){
            x = layer.activateLayer( x );
        }
        return x;
    }

    /**Recognize class for input image.
     * @param x             Matrix of input image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    When net can't recognize image because of it's size.
     */
    public RecognizedType       recognizeClass( Matrix x ) throws Exception{
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
            return  new RecognizedType( output_types.get( output_num ), max / prob_sum );
        }
    }

    /**Recognize input image. Get trace with each layer recognition.
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
            outstream.writeObject( type );
            outstream.writeObject( layers );
            outstream.writeObject( input_height );
            outstream.writeObject( input_width );
            outstream.writeObject( output_types );
        }
        finally{
            if ( outstream != null ){
                outstream.close();
            }
        }
    }

    /**Make independent clone of itself.
     * @return          Clone of itself.
     */
    public Perceptron           clone() throws CloneNotSupportedException{
        super.clone();
        Perceptron net = new Perceptron();
        ArrayList<ActiveLayer> clone_layers = new ArrayList<ActiveLayer>();
        for ( ActiveLayer layer : layers ){
            clone_layers.add( layer.copy() );
        }
        try{
            net.setStructure( clone_layers, input_height, input_width );
        }
        // In this case exception can't raised in no way.
        // Because clonable net has right structure on default.
        catch ( Exception e ){
        }
        net.setRecognizingTypes( output_types );
        return net;
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

    /**Set types, that net can recognize.
     * @param types      List of types.
     */
    public void  setRecognizingTypes( ArrayList< String > types ){
        output_types = new ArrayList< String >();
        for ( String type : types ){
            output_types.add( type );
        }
    }

    /**
     * @param type  String discribing type.
     * @return      Output for wanted type. If net dosn't recognize such type return 'null'.
     */
    public Matrix getOutputByType( String type ){
        Matrix output = new Matrix( output_size, 1);
        for ( int i = 0; i < output_types.size(); i++ ){
            if ( type.equals( output_types.get( i ) ) ){
                output.set( i, 0, 1. );
                return output;
            }
        }
        return null;        
    }

    /**
        * @return      Count of layers in net.
        */
    public int                  getLayersCount(){
           return layers.size();
       }

    /**Get copy of layer.
    * @param layer_number   Number of layer ( numeration begins with 0 ).
    * @return               Copy of layer.
    */
    public ActiveLayer          getLayer( int layer_number ){
       return layers.get( layer_number ).copy();
    }

    /**Set layer.
    * @param layer          Setting layer.
    * @param layer_number   Number of layer ( numeration begins with 0 ).
    */
    public void                 setLayer( ActiveLayer layer, int layer_number ){
        layers.set( layer_number, layer );
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
}
