package neuro.net;

import neuro.Matrix;

/** @author    Vadim Shpakovsky. */

public interface ITrainingNet extends IStaticNet {
    ITrainingNet copy() throws Exception;
    void randomInit( double max_val );
    void train( String learning_sample, String brief_log_path, String detailed_log_path,
                double inaccuracy, double idle_accuracy ) throws Exception;
    void save( String storage ) throws Exception;
    //Matrix readImage( String image_path ) throws Exception;
}
