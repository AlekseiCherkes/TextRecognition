package neuron_net;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 0:14:57
 * To change this template use File | Settings | File Templates.
 */
// @author Vadim Shpakovsky.

// Describe layer of neurons in neuron  network.
public class Layer {
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
        activation_func = f;
    }

    /**
     * @return      The number of neurons in layer.
     * */
    public int size(){
            return w.getColumnDimension();
    }

    /**Get input signals, calculate input signal for each neuron and output theirs reactions.
       @param input     Input signals.
       @return          Output signals.
     */
    public Matrix activateLayer( Matrix input ){
        Matrix out = input.transpose().times(w);
        for ( int i = 0; i < out.getColumnDimension(); i++ ){
            double reaction =  activation_func.activate( out.get( 0, i ) );
            out.set( 0, i, reaction );
        }
        return out;
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
}
