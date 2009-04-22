package neuro.adapter;

import neuro.net.ITrainingNet;
import neuro.net.RecognizedType;
import jblas.matrices.Matrix;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author Vadim Shpakovsky.
 */

public class Tutor {
    private ITrainingNet net;

    /**Construct recognizer with net.
    *@param pattern_net  Training neuron net.
    */
    public Tutor( ITrainingNet pattern_net ){
        if ( net != null ){
            net = pattern_net.copy();
        }
    }

    /** Initialize net from file.
     * @param storage       File for initializing.
     * @throws Exception    If was attempt to initialize absent net.
     */
    public void initNet( String storage ) throws Exception{
        if ( net == null ){
            throw new Exception( "Net not found." );
        }
        net.init( storage );
    }

    /** Save net in file.
     * @param storage       File for saving.
     * @throws Exception    If save error occured.
     */
    public void save( String storage ) throws Exception{
        net.save( storage );
    }

    /**Recognize class for input image.
     * @param x             Input image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    If problems with reading image from file occured.
     */
	public RecognizedType recognize( Matrix x )
        throws Exception{
        return net.recognizeClass( x );
    }

    /**Recognize class for input image.
     * @param path          File which consists image.
     * @return              Class of image. If net coudn't classificate image return 'null'.
     * @throws Exception    If problems with reading image from file occured.
     */
	public RecognizedType recognize( String path ) throws Exception{
        // Read input from file.
        Matrix input_x = null;
        Scanner scanner = null;
        try{
            scanner = new Scanner( new File( path ) );
            input_x = new Matrix( net.getInputSize() , 1 );
            for ( int i = 0; i < net.getInputSize(); i++ ){
                double d =  scanner.nextDouble();
                input_x.set( i, 0, d );
            }
        }
        catch( Exception e){
           throw new Exception( "Problem with reading analysis.image from file." + e.getMessage() );
        }
        finally{
            if(  scanner != null ){
                scanner.close();
            }
        }

        return net.recognizeClass( input_x );
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

    /**Get list of all  types that net can recognize.
     *
     * @return      List with types names.
     */
    public ArrayList< String > getRecognizingTypes(){
        return net.getRecognizingTypes();
    }

    /**
     * Learn the net.
//     * @param  learning_path      Absolute path where are all learning tests.
//     *      On this path must be subdirectory for each class of recognition objects.
//     *      This subdirectory contain all objects belong certain class.
//     *      The name of subdirectory identify the name of class.
//     * @param brief_log_path        Log file for brief teaching history.
//     * @param detailed_log_path     Log file for brief teaching history.
//     * @param inaccuracy        Size of max accepable difference between input and output
//     *                              when output is considered right.
//     * @param idle_accuracy    Size of min accepable difference between old output and output of corrected net
//     *                              when is considered that teach iteration was "useful" ( not idle ).
//     *      when output is considered right.
     */
    public void train()
            throws Exception{
        net.train();
    }

    /** Initialize all wights of net by random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void resetNet( double max_val ){
        net.randomInit( max_val );
    }
}
