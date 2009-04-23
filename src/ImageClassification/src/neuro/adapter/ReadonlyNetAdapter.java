package neuro.adapter;

import neuro.net.INet;
import neuro.net.Perceptron;
import neuro.net.RecognizedType;
import neuro.layer.ActiveLayer;
import neuro.io.BufferedImageCodec;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import jblas.matrices.Matrix;

/**
 * @author Vadim Shpakovsky.
 */

// Get readonly access to net.
public class ReadonlyNetAdapter{
    private Perceptron net;

    public ReadonlyNetAdapter(){}
    public void init( String storage ) throws Exception{
        net = new Perceptron();
        ObjectInputStream input_stream = null;
        try{
            input_stream = new ObjectInputStream( new FileInputStream( storage ) );
            //int temp = ( Integer )input_stream.readObject();
            String read_type = ( String )input_stream.readObject();
            if ( !net.getType().equals( read_type ) ){
                throw new Exception( "Mismatch of net's types. " );
            }
            ArrayList<ActiveLayer> layers = ( ArrayList<ActiveLayer> )input_stream.readObject();
            int input_height = ( Integer )input_stream.readObject();
            int input_width = ( Integer )input_stream.readObject();
            net.setStructure( layers, input_height, input_width );
            ArrayList< String > output_types = ( ArrayList< String > )input_stream.readObject();
            net.setRecognizingTypes( output_types );
        }
        finally{
            if ( input_stream != null ){
                input_stream.close();
            }
        }
    }
    public RecognizedType recognize(String path ) throws Exception{
        File image = new File( path );
        BufferedImageCodec codec = new BufferedImageCodec();
        return net.recognizeClass( codec.convert( codec.loadImage( image ) ) );
    }
}
