import neuro.*;
import neuro.layer.ActiveLayer;
import neuro.layer.Sigmoid;
import neuro.net.RecognizeType;
import neuro.net.StaticTwoLayerPerceptron;
import neuro.net.TrainingTwoLayerPerceptron;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.02.2009
 * Time: 16:46:15
 * To change this template use File | Settings | File Templates.
 */
public class TestingClass {
        public static void main(String[] args){
            int m = 32;
            int n = 32;
            int input = m * n;
            int output = 26;
            int inner_layer = ( input + output ) / 2;
            double teaching_speed = 0.7;

            // two layer perceptron:
            // input        (m x n)
            // first layer  (input x inner_layer)
            // second layer (inner_layer x output)
            // out          output
            ArrayList<ActiveLayer> list = new ArrayList<ActiveLayer>();
            Sigmoid s = new Sigmoid();

            Matrix w = new Matrix( input, inner_layer );
            ActiveLayer layer = new ActiveLayer( w, s);
            list.add( layer.copy() );
            w = new Matrix( inner_layer, output  );
            layer = new ActiveLayer( w, s);
            list.add( layer.copy() );

            try{
                //TestGenerator generator = new TestGenerator( m, n );
                //generator.clearDir("data\\testing_set");
                //generator.generate( "data\\testing_set", 100 );
                TrainingTwoLayerPerceptron training_net = new TrainingTwoLayerPerceptron( list, m, n, teaching_speed );

                training_net.randomInit( 1. );

                Tutor teacher = new Tutor( training_net );

//                teacher.train("data\\teaching_set", "data\\log.txt", 0.1 );
//                teacher.save( "data\\nets\\64x64_english_letters_net");
//
//                StaticTwoLayerPerceptron static_net = new StaticTwoLayerPerceptron( list, m, n );
//                Recognizer recognizer = new Recognizer( static_net );
//                recognizer.initNet( "data\\nets\\64x64_english_letters_net" );
//
//                runTests( recognizer, "tests\\tests_images", "tests\\test_result.txt" );
            }
            catch( Exception e ){
                System.out.println( e.getMessage() );
            }
    }

    /**Recognize test images by net.
     * @param recognizer    Interface for work with net.
     * @param tests_path    Place where are found test images.
     * @param output_path   File with testing results.
     */
     public static void runTests( Recognizer recognizer, String tests_path, String output_path )
            throws Exception{
            String[] all_files = new File( tests_path ).list();
            if ( all_files == null ){
                 throw new Exception( "Error --TestingClass.runTests()-- Wrong testing directory or I/O error occurs." );
            }
            ArrayList< String > tests = new ArrayList< String >();
            for ( String file: all_files ){
                String test_name = tests_path + "\\" + file;
                File test_file = new File( test_name );
                if ( file.charAt( 0 ) != '.' && test_file.isFile() ){
                    tests.add( test_name );
                }
            }
        
        PrintWriter log = null;
        try{
            log = new PrintWriter( output_path );
            for ( String test: tests ){
                RecognizeType type = recognizer.recognize( test );
                if ( type == null){
                    type.setType( "not recognize" );
                }
                log.printf("Test \"%s\": %s  %f\n", test, type.getType(), type.getAccuracy()  );
            }
        }
        catch( Exception e ){
            throw new Exception( "Error. --TestingClass.runTests()--" + e.getMessage() );
        }
        finally{
            if ( log != null ){
                try{
                    log.close();
                }
                catch( Exception e ){
                    throw new Exception( "Error. --TestingClass.runTests()--" + e.getMessage() );
                }
            }
        }


     }
}
