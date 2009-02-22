package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.02.2009
 * Time: 8:56:23
 * To change this template use File | Settings | File Templates.
 */
public interface ActiveFunc extends Cloneable {
    public double activate( double input );

    /**Get value of derivative of function.
     * @param point     Point in which derivative is taken.
     * @return          Value of derivative in point.
     */
    public double getDerivative( double point );
}
