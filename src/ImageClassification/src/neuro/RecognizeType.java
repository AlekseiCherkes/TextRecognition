package neuro;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.02.2009
 * Time: 2:33:14
 * To change this template use File | Settings | File Templates.
 */

// Store information about recognize type.
public class RecognizeType {
    private String type;
    // Probability of right recognizing.
    private double accuracy;

    public RecognizeType( String type, double accuracy ){
        this.type = type;
        this.accuracy = accuracy;
    }

    public String getType(){
        return type;
    }
    public void setType( String val ){
        type = val;
    }
    public double getAccuracy(){
        return accuracy;
    }
    public void setAccuracy( double val ){
        accuracy = val;
    }
}
