package neuron_net;
import java.lang.Math;
/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.02.2009
 * Time: 8:57:36
 * To change this template use File | Settings | File Templates.
 */
public class Sigmoid implements ActiveFunc{
    public double activate(double  input){
        return 1. / ( 1. + Math.pow( Math.E, -input ) );
//        Matrix output = input.copy();
//        for( int i = 0; i < output.getRowDimension(); i++ )
//            for( int j = 0; j < output.getColumnDimension(); j++ ){
//                double f = 1. / ( 1. + Math.pow( Math.E, -output.get( i, j )));
//                output.set( i, j, f);
//            }
//        return output;
    }
}
