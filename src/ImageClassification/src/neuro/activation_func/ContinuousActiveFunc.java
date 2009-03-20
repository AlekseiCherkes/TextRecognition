package neuro.activation_func;

import neuro.activation_func.ActiveFunc;

/**
 * @author Vadim Shpakovsky.
 */

public interface ContinuousActiveFunc extends ActiveFunc {

    public double getDerivative( double point );
}
