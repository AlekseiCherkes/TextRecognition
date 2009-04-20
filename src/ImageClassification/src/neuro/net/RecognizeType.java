package neuro.net;

/** @author    Vadim Shpakovsky. */

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
