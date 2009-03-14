package neuro.layer;
import java.lang.Math;

/** @author    Vadim Shpakovsky. */

public class Sigmoid implements ActiveFunc{
    public double activate(double  input){
        return 1. / ( 1. + Math.pow( Math.E, -input ) );
    }

    public double getDerivative( double point ){
        return point * ( 1. - point );
    }
}
