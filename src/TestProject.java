import neuro.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.02.2009
 * Time: 16:46:15
 * To change this template use File | Settings | File Templates.
 */
public class TestProject {
        public static void main(String[] args){

            // two layer perceptron:
            // input 12x12
            // first layer 144x85
            // second layer 85x26
            // out 26
            ArrayList<Layer> list = new ArrayList<Layer>();
            Sigmoid s = new Sigmoid();
            //Matrix w = new Matrix( 144, 85, 0. );

            Matrix w = new Matrix( 9, 6, 0. );

            Layer layer = new Layer( w, s);
            list.add( layer.copy() );
            //w = new Matrix( 85, 26, 0. );
            w = new Matrix( 6, 2, 0. );
            layer = new Layer( w, s);
            list.add( layer.copy() );
            TwoLayerPerceptron net = new TwoLayerPerceptron( list, 0.7 );
            net.randomInit( 1. );
            
            //Recognizer rec = new Recognizer( net, 12, 12 );
            Recognizer rec0 = new Recognizer( net, 3, 3 );


            try{
                //rec0.save( "data\\3x3_net" );
                Recognizer rec = new Recognizer( null, 3, 3 );
                rec.load( "data\\3x3_net" );
                rec.train( "tests", "tests\\log.txt", 0.01 );
                rec.save( "data\\trained_3x3_net" );
            }
            catch( Exception e ){
                System.out.println( e.getMessage() );
            }
    }
}
