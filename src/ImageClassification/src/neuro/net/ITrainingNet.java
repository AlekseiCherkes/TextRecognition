package neuro.net;

import neuro.Matrix;

import java.io.IOException;

/** @author    Vadim Shpakovsky. */

public interface ITrainingNet extends IStaticNet {
    ITrainingNet copy() throws Exception;
    void randomInit( double max_val );
    void train( String learning_sample, String log_file, double precise ) throws Exception;
    void save( String storage ) throws Exception;
    Matrix readImage( String image_path ) throws Exception;
}
