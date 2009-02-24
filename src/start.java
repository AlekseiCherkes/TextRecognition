import neuro.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.02.2009
 * Time: 16:46:15
 * To change this template use File | Settings | File Templates.
 */
public class start {
        public static void main(String[] args){

            // two layer perceptron:
            // input 12x12
            // first layer 144x85
            // second layer 85x26
            // out 26
            ArrayList<Layer> list = new ArrayList<Layer>();
            Sigmoid s = new Sigmoid();
            //Matrix w = new Matrix( 144, 85, 0. );

            Matrix w = new Matrix( 4, 3, 0. );

            Layer layer = new Layer( w, s);
            list.add( layer.copy() );
            //w = new Matrix( 85, 26, 0. );
            w = new Matrix( 3, 2, 0. );
            layer = new Layer( w, s);
            list.add( layer.copy() );
            TwoLayerPerceptron net = new TwoLayerPerceptron( list, 0.5 );
            net.randomInit( 1. );
            
            //Recognizer rec = new Recognizer( net, 12, 12 );
            Recognizer rec = new Recognizer( net, 2, 2 );

            try{
                rec.train("D:\\Study(8 sem)\\ИТиРОД\\text-recognition\\trunk\\tests",
                    "D:\\Study(8 sem)\\ИТиРОД\\text-recognition\\trunk\\tests\\log.txt", 0.01 );
            }
            catch( Exception e ){
                System.out.println( e.getMessage() );
            }
    }
}
