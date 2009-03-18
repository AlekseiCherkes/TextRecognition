package neuro.layer;

import jblas.matrices.Matrix;

/** @author    Vadim Shpakovsky. */

// Layer that can get neuron's reactions.
public class ActiveLayer extends Layer{
    // Activation function for each neuron in layer.
    private ActiveFunc activation_func;

    /**Construct layer with weight matrix and activation function.
    @param w        Data for initialization weight matrix.
    @param f        Activation function.
    */
    public ActiveLayer( Matrix w, ActiveFunc f){
        this.w = w.copy();
        activation_func = f;
    }

    /**Construct layer with weight matrix full by zeros and activation function.
    @param height    Number of rows in weight matrix 'w'.
    @param width     Number of columns in weight matrix 'w'.
    @param f         Activation function.
    */
    public ActiveLayer( int height, int width, ActiveFunc f ){
        this.w = new Matrix( height, width, 0.);
        // TODO How realize copy() or clone() for class consists only methods?
        activation_func = f;
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
    public Matrix activateLayer( Matrix x ){
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
    public ActiveLayer copy(){
        return new ActiveLayer( w, activation_func );
    }
}
