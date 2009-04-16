package neuro.activation_func;

/**
 * @author Vadim Shpakovsky.
 */

public interface ContinuousActiveFunc extends ActiveFunc {

    public double getDerivative( double point );
}
