package neuro.net;

import neuro.Matrix;
import neuro.net.RecognizeType;

import java.util.ArrayList;

/** @author    Vadim Shpakovsky. */

public interface IStaticNet {
    IStaticNet copy() throws Exception;
    void print( int precision );
    String getType();
    int getInputSize();
    int getInputWidth();
    int getInputHeight();
    int getOutputSize();
    ArrayList< String > getRecognizingTypes();

    Matrix recognize( Matrix x ) throws Exception;
    ArrayList< Matrix > traceRecognize( Matrix x );
    RecognizeType recognizeClass( Matrix x ) throws Exception;
    void init( String storage )  throws Exception;
}
