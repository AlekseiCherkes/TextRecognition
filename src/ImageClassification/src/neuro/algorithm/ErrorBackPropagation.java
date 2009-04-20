package neuro.algorithm;

import neuro.net.Perceptron;
import neuro.layer.ActiveLayer;
import jblas.matrices.Matrix;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Vadim Shpakovsky.
 */

// Use for net's teaching Error Back Propagation algorithm.
// As yet this algorithm is applicable only for nets of type 'Perceptron'.
public class ErrorBackPropagation {
    /**Make correction of net's weights.
     * IMPORTANT! This correction bring closer network's reaction to right answer on THIS test image,
     * but you need use this function several times for steady result.
     * @param net               Trainee net.
     * @param input             Matrix of input image.
     * @param target            Matrix of desired output that is assosiated with 'input'.
     * @param teaching_speed    Define size of correction.
     */
    public static void teach( Perceptron net, Matrix input, Matrix target, double teaching_speed){
        // Run input through the net.
        ArrayList< Matrix > recognition_trace =  net.traceRecognize( input );
        Matrix y = recognition_trace.get( recognition_trace.size() - 1 );
        Matrix E = target.minus( y );

        LinkedList< Matrix > layers_dw = new LinkedList< Matrix >();
        // Correct weights output layer.
        ActiveLayer out_layer = net.getLayer( net.getLayersCount() - 1 );
        Matrix dF = new Matrix( net.getOutputSize(), 1 );
        for ( int i = 0; i < net.getOutputSize(); i++ ){
            dF.set( i, 0, out_layer.getActiveFunc().getDerivative( y.get( i, 0 ) ) );
        }
        Matrix delta = dF.arrayTimes( E );
        Matrix F = recognition_trace.get( recognition_trace.size() - 2 );
        Matrix dw = F.times( delta.transpose() );

        dw = dw.times( teaching_speed );
        layers_dw.addFirst( dw );

        // Calculate corrections weights of inner layers.
        for ( int i = 1; i < net.getLayersCount(); i++ ){
            ActiveLayer senior_layer = net.getLayer( net.getLayersCount() - i );
            ActiveLayer junior_layer = net.getLayer( net.getLayersCount() - i - 1 );
            F = recognition_trace.get( recognition_trace.size() - i - 1 );
            dF = new Matrix( junior_layer.size(), 1 );
            for ( int j = 0; j < junior_layer.size(); j++ ){
                dF.set( j, 0, junior_layer.getActiveFunc().getDerivative( F.get( j, 0 ) ) );
            }
            Matrix inner_delta = new Matrix( junior_layer.size(), 1 );
            for ( int j = 0; j < junior_layer.size(); j++ ){
                double sum = 0.;
                for ( int k = 0; k < net.getLayer( net.getLayersCount() - i ).size(); k++ ){
                    sum += delta.get( k, 0 ) * senior_layer.getW( j, k );
                }
                inner_delta.set( j, 0, sum * dF.get( j, 0 ));
            }
            F = recognition_trace.get( recognition_trace.size() - i - 2 );
            dw = F.times( inner_delta.transpose() );
            dw = dw.times( teaching_speed );
            layers_dw.addFirst( dw );
        }

        // Correct all weights.
        for ( int i = 0; i < net.getLayersCount(); i++ ){
            ActiveLayer layer = net.getLayer( i );
            layer.addW( layers_dw.get( i ) );
            net.setLayer( layer, i );
        }
    }
}
