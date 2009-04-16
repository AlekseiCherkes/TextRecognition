package neuro.net;

/** @author    Vadim Shpakovsky. */

public interface ITrainingNet extends IStaticNet {
    ITrainingNet copy();
    void randomInit( double max_val );
    void train() throws Exception;
    void save( String storage ) throws Exception;
}
