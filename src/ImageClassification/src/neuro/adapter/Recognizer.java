package neuro.adapter;

import neuro.net.IStaticNet;
import neuro.net.RecognizeType;
import neuro.net.TrainingPerceptron;
import jblas.matrices.Matrix;

import java.util.ArrayList;

/** @author    Vadim Shpakovsky. */

// Incapsulate work with net.
public class Recognizer{
    private IStaticNet net;

    /**Construct recognizer with net.
    @param net        Static neuron net.
    */
    public Recognizer( IStaticNet net )
        throws Exception{
        if ( net != null ){
            this.net = net.copy();
        }
        else{
            net = null;
        }
    }

    /** Initialize net from file.
     * @param storage       File for initializing.
     * @throws Exception
     */
	public void initNet( String storage ) throws Exception{
        if ( net == null ){
            throw new Exception( "Net not found." );
        }
        net.init( storage );
    }

    /**Recognize class for input image.
     * @param x     Input image.
     * @return      Class of image. If net coudn't classificate image return 'null'.
     */
	public RecognizeType recognize( Matrix x )
        throws Exception{
        return net.recognizeClass( x );
    }

    /**Recognize class for input image.
     * @param path      File which consists image.
     * @return          Class of image. If net coudn't classificate image return 'null'.
     */
	public RecognizeType recognize( String path ) throws Exception{
        Matrix input_x = TrainingPerceptron.readImage( path, net.getMinInput(), net.getMaxInput() );
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

    /**Get list of all  types that net can recognize.
     *
     * @return      List with types names.
     */
    public ArrayList< String > getRecognizingTypes(){
        return net.getRecognizingTypes();
    }
}
