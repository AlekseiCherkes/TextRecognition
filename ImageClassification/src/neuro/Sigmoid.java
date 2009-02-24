package neuro;
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
    }

    public double getDerivative( double point ){
        return point / ( 1 - point );
    }
}
