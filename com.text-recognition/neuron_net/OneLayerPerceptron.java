package neuron_net;

import java.lang.Math;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.02.2009
 * Time: 8:54:26
 * To change this template use File | Settings | File Templates.
 */
public class OneLayerPerceptron implements Net{
    private Matrix w;
    private ActiveFunc activation_func;
    //private Double threshold;

    // находит взвешенную сумму для каждого нейрона в слое
    // и применяет к ней пороговую активационную функцию
    // возвращает результат для всего слоя
    private Matrix activateLayer(Matrix x, Matrix w){
        Matrix out = x.times(w);
        out = activation_func.activate( out );
        return out;
    }

    public OneLayerPerceptron( Matrix w, ActiveFunc f ){
        this.w = w.copy();
        activation_func = f;
    }

    public void train( String learning_sample ){
       /* Matrix x;
        Matrix y;
        Matrix res = recognize( x );
        Matrix err = y.minus( x );*/
        
    }
	public void save( String storage ){}
	public void load( String storage ){}
	public Matrix recognize( Matrix x ){
        return activateLayer( x, w );
    }
}
