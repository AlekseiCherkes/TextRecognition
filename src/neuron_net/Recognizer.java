package neuron_net;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 0:25:46
 * To change this template use File | Settings | File Templates.
 */
// Incapsulate work with net.
public class Recognizer {
    // size of input picture
    private int width, height;
    private Net net;

    /**Construct recognizer with net and size of input image.
    @param net        Neuron net.
    @param width      Width of input image.
    @param height     Height of input image.
    */
    public Recognizer( Net net, int height, int width ){
        this.net = net.copy();
        this.width = width;
        this.height = height;
    }

    // for input use
    //
    // TODO write for external use!
    //
    /**
     * Learn the net.
     * @param  learning_sample      Path where are all learning tests.
     */
    public void train( String learning_sample ){
       /* Matrix x;
        Matrix y;
        Matrix res = recognize( x );
        Matrix err = y.minus( x );*/
        

    }
    public void save( String storage ){}
	public void load( String storage ){}
	public Matrix recognize( Matrix x ){
        return null;
    }

    /** Initialize all wights of net by random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void resetNet( double max_val ){
        net.randomInit( max_val );
    }

    /** Print the weights of all layers in net to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void printNet( int precision ){
        net.print( precision );
    }

    /** Get net's type.
        @return      String that consist net's type.
     */
    public String getNetType(){
        return net.getType();
    }
}
