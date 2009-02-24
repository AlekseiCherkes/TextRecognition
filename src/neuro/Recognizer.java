package neuro;

import java.io.*;
import java.util.TreeMap;

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
    // Contains all classes that this network can recognize.
    // Position's number in this collection indicate number of output for it class in network.
    //private TreeMap< String, Integer > output_types;

    /**Construct recognizer with net and size of input image.
    @param net        Neuron net.
    @param width      Width of input image.
    @param height     Height of input image.
    */
    public Recognizer( Net net, int height, int width ){
        if ( net != null ){
            this.net = net.copy();
        }
        else{
            net = null;
        }
        this.width = width;
        this.height = height;
        //output_types = new TreeMap< String, Integer >();
    }

    // for input use
    //
    // TODO write for external use!
    //
    /**
     * Learn the net.
     * @param  learning_path      Absolute path where are all learning tests.
     *      On this path must be subdirectory for each class of recognition objects.
     *      This subdirectory contain all objects belong certain class.
     *      The name of subdirectory identify the name of class.
     * @param log_file            Log file for teaching history. 
     * @param precision           Size of max accepable difference between input and output.
     *      when output is considered right.
     */
    public void train( String learning_path, String log_file, double precision )
            throws Exception{
        net.train( learning_path, log_file, precision );
    }

    /** Save net in file.
     * @param storage       File for saving.
     * @throws Exception    
     */
    public void save( String storage ) throws Exception{
        ObjectOutputStream outstream = null;
        try{
            outstream = new ObjectOutputStream( new FileOutputStream( storage ) );
            outstream.writeObject( net );
        }
        catch( Exception e){
            throw new Exception( "Error --Recognizer.save()-- " + e.getMessage() );
        }
        finally{
            if ( outstream != null ){
                    try{
                        outstream.close();
                    }
                    catch( Exception e ){
                        e.getMessage();
                    }
                }
        }
    }
    /** Load net from file.
     * @param storage       File for loading.
     * @throws Exception
     */
	public void load( String storage ) throws Exception{
        ObjectInputStream input_stream = null;
        try{
            input_stream = new ObjectInputStream( new FileInputStream( storage ) );
            net = ( Net )input_stream.readObject();
        }
        catch( Exception e){
            throw new Exception( "Error --Recognizer.load()-- " + e.getMessage() );
        }
        finally{
            if ( input_stream != null ){
                    try{
                        input_stream.close();
                    }
                    catch( Exception e ){
                        e.getMessage();
                    }
                }
        }
    }
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
