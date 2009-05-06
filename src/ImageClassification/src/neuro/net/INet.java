package neuro.net;

import jblas.matrices.Matrix;

import java.util.List;

/** @author    Vadim Shpakovsky. */

public interface INet extends Cloneable {
    INet                clone() throws CloneNotSupportedException;
    void                print( int precision );
    String              getType();
    int                 getInputSize();
    int                 getInputWidth();
    int                 getInputHeight();
    int                 getOutputSize();
    List< String >      getRecognizingTypes();

    Matrix              recognize( Matrix x ) throws Exception;
    RecognizedType      recognizeClass( Matrix x ) throws Exception;
    void                randomInit( double max_val );
    void                save( String storage ) throws Exception;
}
