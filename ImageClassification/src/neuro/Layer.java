package neuro;

import java.util.Random;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 0:14:57
 * To change this template use File | Settings | File Templates.
 */
// @author Vadim Shpakovsky.

// Describe layer of neurons in neuron  network.
public class Layer implements Serializable {
    // Matrix of sinapse's weights.
    private Matrix w;
    // Activation function for each neuron in layer.
    private ActiveFunc activation_func;

    /**Construct layer with weight matrix and activation function.
    @param w        Data for initialization weight matrix.
    @param f        Activation function.
    */
    public Layer( Matrix w, ActiveFunc f){
        this.w = w.copy();
        activation_func = f;
    }

    /**Construct layer with weight matrix full by zeros and activation function.
    @param height    Number of rows in weight matrix 'w'.
    @param width     Number of columns in weight matrix 'w'.
    @param f         Activation function.
    */
    public Layer( int height, int width, ActiveFunc f ){
        this.w = new Matrix( height, width, 0.);
        // TODO How realize copy() or clone() for class consists only methods?
        activation_func = f;
    }

    /**
     * @return      The number of neurons in layer.
     * */
    public int size(){
        return w.getColumnDimension();
    }

    /**
     * @return      The size of previous layer.
     * */
    public int prevSize(){
        return w.getRowDimension();
    }

    /**
     * @return      The function of activation.
     */
    public ActiveFunc getActiveFunc(){
        return activation_func;
    }

    /**Get input signals, calculate input signal for each neuron and output theirs reactions.
       @param x          Input signals.
       @return           Output signals.
     */
    public Matrix activateLayer( Matrix x )
            throws IllegalArgumentException{
        Matrix input = x.transpose().times(w);
        input = input.transpose();
        for ( int i = 0; i < input.getRowDimension(); i++ ){
            double reaction =  activation_func.activate( input.get( i, 0 ) );
            input.set( i, 0, reaction );
        }
        return input;
    }

    /** Make independent copy of itself.
       @return          Copy of itself.
     */
    public Layer copy(){
        return new Layer( w, activation_func );
    }

    /** Print the weight matrix to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void print( int precision ){
        w.print( 2, precision );
    }

     /** Print the weight matrix to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void print( PrintWriter out, int precision ){
        w.print( out, 2, precision );
    }

    /** Initialize weight matrix with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void randomInit( double max_val){
        Random rand = new Random();
        for ( int i = 0; i < w.getRowDimension(); i++ ){
            for ( int j = 0; j < w.getColumnDimension(); j++ ){
                double val = max_val * rand.nextDouble();
                w.set( i, j, val );
            }
        }
    }

    /**W = W + dw
     * @param dw    Adding matrix.
     */
    public void addW( Matrix dw ){
        w = w.plus( dw );
    }

   /** Get a single weight.
   @param i    Row index.
   @param j    Column index.
   @return     w(i,j)
   */
    public double getW( int i, int j ){
        return w.get( i, j );
    }

   /** Set a single weight.
   @param i    Row index.
   @param j    Column index.
   @param val  Setting value. 
   */
    public void setW( int i, int j, double val ){
        w.set( i, j, val );
    }
}
