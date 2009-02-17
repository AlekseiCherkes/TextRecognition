package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.02.2009
 * Time: 8:59:10
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdFunc implements ActiveFunc{
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
    public Matrix activate(Matrix input){
        Matrix output = input.copy();
        for( int i = 0; i < output.getRowDimension(); i++ )
            for( int j = 0; j < output.getColumnDimension(); j++ ){
                if (output.get( i, j ) >= threshold )
                    output.set( i, j, 1. );
                else
                    output.set( i, j, 0. );
            }
        return output;
    }
}
