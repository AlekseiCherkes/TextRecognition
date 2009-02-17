package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.02.2009
 * Time: 16:46:15
 * To change this template use File | Settings | File Templates.
 */
public class start {
        public static void main(String[] args){
        double[][] arr = {{1, 2}, {7, 5}};
        Matrix w = new Matrix( arr );
        w.normalize();
        Matrix x = new Matrix( 2, 1, 1.);
        ThresholdFunc f = new ThresholdFunc( 0.5 );
        Sigmoid s = new Sigmoid();
        OneLayerPerceptron net = new OneLayerPerceptron( w, f );
        OneLayerPerceptron net2 = new OneLayerPerceptron( w, s );

        System.out.println("w = ");
        w.print( 2, 3 );
        System.out.println("x = ");
        x.print( 2, 3 );
        System.out.println("x' * w = ");
        x.transpose().times( w ).print( 2, 3 );

        

    }
}
