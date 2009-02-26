/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 10.02.2009
 * Time: 23:52:04
 * To change this template use File | Settings | File Templates.
 */

package neuro;

import java.util.ArrayList;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.lang.Exception;

abstract public class Net implements Serializable {
    protected int input_size;
    protected int output_size;
    protected String type;
    abstract Net copy();
    abstract void print( int precision );
    abstract void randomInit( double max_val );
    abstract int getLayersCount();
    abstract void train( String learning_sample, String log_file, double precise ) throws Exception;
    abstract Matrix recognize( Matrix x );
    abstract ArrayList< Matrix > traceRecognize( Matrix x );
    abstract RecognizeType recognizeClass( Matrix x );

    /** Get net's type.
        @return      String that consist net's type.
     */
    public  String getType(){
        return type;
    }
    /**
     * @return      Number of neurons in input layer.
     */
    public int getInputSize(){
        return input_size;
    }

    /**
     * @return      Number of neurons in output layer.
     */
    public int getOutputSize(){
        return output_size;
    }
}
