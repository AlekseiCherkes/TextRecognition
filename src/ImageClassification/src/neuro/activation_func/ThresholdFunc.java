package neuro.activation_func;

/** @author    Vadim Shpakovsky. */

public class ThresholdFunc implements ActiveFunc {
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
