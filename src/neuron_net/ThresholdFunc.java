package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.02.2009
 * Time: 8:59:10
 * To change this template use File | Settings | File Templates.
 */

// TODO Think how find derivative.
public class ThresholdFunc /*implements ActiveFunc*/{
    private double threshold;
    public ThresholdFunc( double threshold ){
        setThreshold( threshold );
    }
    public double getThreshold(){
        return threshold;
    }
    public void setThreshold( double threshold ){
        this.threshold = threshold;
    }
    public double activate( double input ){
        if ( input >= threshold )
            return 1.;
        else
            return 0.;
    }
}
