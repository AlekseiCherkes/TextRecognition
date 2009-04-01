package neuro.activation_func;

import neuro.activation_func.ContinuousActiveFunc;

import java.lang.Math;

/**
 * @author Vadim Shpakovsky.
 */

public class Sigmoid implements ContinuousActiveFunc{
    private double shift;

    public Sigmoid() {
        this.shift = 0.;
    }

    public Sigmoid(double shift) {
        this.shift = shift;
    }

    public double activate(double input) {
        return this.shift + 1. / (1. + Math.pow(Math.E, -input));
    }

    /**
     * Get value of derivative of function.
     *
     * @param point Point in which derivative is taken.
     * @return Value of derivative in point.
     */
    public double getDerivative(double point) {
        return point * (1. - point);
    }
}
