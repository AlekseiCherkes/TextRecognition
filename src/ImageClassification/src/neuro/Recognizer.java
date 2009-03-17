package neuro;

import neuro.net.IStaticNet;
import neuro.net.RecognizeType;
import neuro.net.TrainingPerceptron;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/** @author    Vadim Shpakovsky. */

// Incapsulate work with net.
public class Recognizer{
    private IStaticNet net;

    /**Construct recognizer with net.
    @param net        Static neuron net.
    */
    public Recognizer( IStaticNet net )
        throws Exception{
        if ( net != null ){
            this.net = net.copy();
        }
        else{
            net = null;
        }
    }

    /** Initialize net from file.
     * @param storage       File for initializing.
     * @throws Exception
     */
	public void initNet( String storage ) throws Exception{
        if ( net == null ){
            throw new Exception( "Error. --Recognizer.initNet( String )-- net == null." );
        }
        net.init( storage );
    }

    /**Recognize class for input image.
     * @param x     Input image.
     * @return      Class of image. If net coudn't classificate image return 'null'.
     */
	public RecognizeType recognize( Matrix x )
        throws Exception{
        try{
            return net.recognizeClass( x );
        }
        catch( Exception e ){
            throw new Exception("--Recognizer.recognize( Matrix )-- " + e.getMessage() );
        }
    }

    /**Recognize class for input image.
     * @param path      File which consists image.
     * @return          Class of image. If net coudn't classificate image return 'null'.
     */
	public RecognizeType recognize( String path ) throws Exception{
        try{
            // Read input from file.
            Matrix input_x = TrainingPerceptron.readImage( path );
//        Scanner scanner = null;
//        try{
//            scanner = new Scanner( new File( path ) );
//            input_x = new Matrix( net.getInputSize() , 1 );
//            for ( int i = 0; i < net.getInputSize(); i++ ){
//                double d =  scanner.nextDouble();
//                input_x.set( i, 0, d );
//            }
//        }
//        catch( Exception e){
//           throw new Exception( "Error. --Recognizer.recognize( String )-- Problem with reading image from file." +
//                    e.getMessage() );
//        }
//        finally{
//            if(  scanner != null ){
//                try{
//                    scanner.close();
//                }
//                catch( Exception e ){
//                    throw e;
//                }
//            }
//        }

            return net.recognizeClass( input_x );
        }
        catch( Exception e ){
            throw new Exception("--Recognizer.recognize( String )-- " + e.getMessage() );
        }
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
}
