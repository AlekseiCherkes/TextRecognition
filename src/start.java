import neuron_net.*;

import java.util.ArrayList;
import java.io.File;

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
            Matrix w = new Matrix( 144, 85, 0. );
            Layer layer = new Layer( w, s);
            list.add( layer.copy() );
            w = new Matrix( 85, 26, 1. );
            layer = new Layer( w, s);
            list.add( layer.copy() );
            TwoLayerPerceptron net = new TwoLayerPerceptron( list );
            net.randomInit( 1. );
            Recognizer rec = new Recognizer( net, 12, 12 );

            //rec.printNet( 4 );

            String path =  "D:\\Study(8 sem)\\ИТиРОД\\text-recognition\\trunk\\src";
            String list2[] = new File( path ).list();
            for( int i = 0; i < list2.length; i++ ){
                System.out.println(list2[i]);
                File f = new File( path + "\\" + list2[i] );
                String list3[] = f.list();
                if (list3 == null)
                    continue;
                for(int j = 0; j < list3.length; j++){
                    System.out.printf("\t%s\n", list3[j]);
                }
            }


    }
}
