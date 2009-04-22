package neuro.exception;

/**
 * @author Vadim Shpakovsky.
 */

public class StopTeachingProgressException extends Exception{
    public StopTeachingProgressException(){};
    public StopTeachingProgressException( String message ){
        super( message );
    };
}