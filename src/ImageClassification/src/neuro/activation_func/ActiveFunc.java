package neuro.activation_func;

import java.io.Serializable;

/** @author    Vadim Shpakovsky. */

public interface ActiveFunc extends Serializable{
    public double activate( double input );   
}
