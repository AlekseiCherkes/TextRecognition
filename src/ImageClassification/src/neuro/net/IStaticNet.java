package neuro.net;

import jblas.matrices.Matrix;
import neuro.net.RecognizeType;

import java.util.ArrayList;

/** @author    Vadim Shpakovsky. */

public interface IStaticNet {
    IStaticNet          copy() throws Exception;
    void                print( int precision );
    String              getType();
    int                 getInputSize();
    int                 getInputWidth();
    int                 getInputHeight();
    int                 getOutputSize();
    void                setMinInput( double val );
    double              getMinInput();
    void                setMaxInput( double val );
    double              getMaxInput();
    ArrayList< String > getRecognizingTypes();

    Matrix              recognize( Matrix x ) throws Exception;
    ArrayList< Matrix > traceRecognize( Matrix x );
    RecognizeType       recognizeClass( Matrix x ) throws Exception;
    void                init( String storage )  throws Exception;
}
