package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 0:14:57
 * To change this template use File | Settings | File Templates.
 */
// @author Vadim Shpakovsky.
public class Layer {
    // Matrix of sinapse's weights.
    private Matrix w;
    // Activation function for each neuron in layer.
    private ActiveFunc activation_func;

    /*construct layer with weight matrix and activation function.
    @param w        Data for initialization weight matrix.
    @param f        Activation function.
    */
    public Layer( Matrix w, ActiveFunc f){
        this.w = w.copy();
        activation_func = f;
    }

    /*construct layer with weight matrix full by zeros and activation function.
    @param height    Number of rows in weight matrix 'w'.
    @param width     Number of columns in weight matrix 'w'.
    @param f         Activation function.
    */
    public Layer( int height, int width, ActiveFunc f ){
        this.w = new Matrix( height, width, 0.);
        activation_func = f;
    }

    /* Get input signals, calculate input signal for each neuron and output theirs reactions.
       @param input     Input signals.
       @return          Output signals.
     */
    public Matrix activateLayer( Matrix input ){
        Matrix out = input.transpose().times(w);
        return activation_func.activate( out );
    }

    /* Make independent copy of itself.
       @return          Copy of itself.
     */
    public Layer copy(){
        return new Layer( w, activation_func );
    }

    /* Print the weight matrix to stdout.
     */
    public void print(){
        w.print( 2, 2 );
    }
}
