package neuro.layer;

import java.io.Serializable;

/** @author    Vadim Shpakovsky. */

public interface ActiveFunc extends Cloneable, Serializable {
    public double activate( double input );

    /**Get value of derivative of function.
     * @param point     Point in which derivative is taken.
     * @return          Value of derivative in point.
     */
    public double getDerivative( double point );
}
