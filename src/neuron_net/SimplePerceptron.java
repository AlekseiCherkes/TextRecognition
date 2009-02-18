package neuron_net;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.02.2009
 * Time: 0:01:10
 * To change this template use File | Settings | File Templates.
 */
public class SimplePerceptron implements Net {
    private Matrix w_1;
    private Matrix w_2;
    private ActiveFunc activation_func;
    //private Double threshold;
    
    // находит взвешенную сумму для каждого нейрона в слое
    // вычисляет активационную ф-ю
    private Matrix activateLayer(Matrix x, Matrix w){
        Matrix out = x.times(w);
        out = activation_func.activate( out );
        return out;
    }

    public SimplePerceptron( Matrix w_1, Matrix w_2, ActiveFunc f ){
        this.w_1 = w_1.copy();
        this.w_2 = w_2.copy();
        activation_func = f;
    }

    public void train( String learning_sample ){}
	public void save( String storage ){}
	public void load( String storage ){}
	public Matrix recognize( Matrix x ){
        Matrix first = activateLayer( x, w_1 );
        return first;
    }
}
