package neuro.adapter;

import neuro.net.IStaticNet;
import neuro.net.RecognizedType;
import neuro.io.BufferedImageCodec;
import jblas.matrices.Matrix;

import java.util.ArrayList;
import java.io.File;

/** @author    Vadim Shpakovsky. */

// Incapsulate work with net.
public class Recognizer{
    private IStaticNet net;

    /**Construct recognizer with net.
    @param local_net        Static neuron net.
    */
    public Recognizer( IStaticNet local_net ){
        this.net = local_net.copy();
        
    }

    /** Initialize net from file.
     * @param storage       File for initializing.
     * @throws Exception    When net is absent ( we can't initialize net without knowing about it's parameters ).
     */
	public void initNet( String storage ) throws Exception{
        if ( net == null ){
            throw new Exception( "Net not found." );
        }
        net.init( storage );
    }

    /**Recognize class for input image.
     * @param x             Input image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    When net can't recognize image because of it's size.
     */
	public RecognizedType recognize( Matrix x )
        throws Exception{
        return net.recognizeClass( x );
    }

    /**Recognize class for input image.
     * @param path          File which consists image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    When net can't recognize image because of it's size. Or are problems with read input.
     */
	public RecognizedType recognize( String path ) throws Exception{
        File image = new File ( path );
        BufferedImageCodec image_codec = new BufferedImageCodec();
        Matrix input_x = image_codec.convert( image_codec.loadImage( image ) );
        return net.recognizeClass( input_x );
    }

    /** Print the weights of all layers in net to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void printNet( int precision ){
        net.print( precision );
    }

    /** Get net's type.
        @return      String that consist net's type.
     */
    public String getNetType(){
        return net.getType();
    }

    public int getInputHeight(){
        return net.getInputHeight();
    }

    public int getInputWidth(){
        return net.getInputWidth();
    }

    /**Get list of all  types that net can recognize.
     *
     * @return      List with types names.
     */
    public ArrayList< String > getRecognizingTypes(){
        return net.getRecognizingTypes();
    }
}
