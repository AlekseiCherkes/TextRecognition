import neuro.*;

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
public class TestProject {
        public static void main(String[] args){
            int m = 6;
            int n = 4;
            int input = m * n;
            int output = 2;
            int inner_layer = ( input + output ) / 2;
            double teaching_speed = 0.7;

            // two layer perceptron:
            // input        (m x n)
            // first layer  (input x inner_layer)
            // second layer (inner_layer x output)
            // out          output
            ArrayList<Layer> list = new ArrayList<Layer>();
            Sigmoid s = new Sigmoid();

            Matrix w = new Matrix( input, inner_layer );
            Layer layer = new Layer( w, s);
            list.add( layer.copy() );
            w = new Matrix( inner_layer, output  );
            layer = new Layer( w, s);
            list.add( layer.copy() );
            TwoLayerPerceptron net = new TwoLayerPerceptron( list, teaching_speed );
            net.randomInit( 1. );

            Recognizer rec0 = new Recognizer( net, m, n );

            try{
                // Generate test images.
                TestGenerator test_generator = new TestGenerator( m, n );
                //test_generator.clearDir( "tests\\training\\not_A" );
                //test_generator.generate( "tests\\tests_images", 500 );

                //rec0.save( "data\\6x4_net" );
                Recognizer rec = new Recognizer( null, m, n );
                rec.load( "data\\trained_6x4_net" );
                runTests( rec, "tests\\tests_images", "tests\\test_result.txt" );
                //rec.train( "tests\\training", "tests\\training\\log.txt", 0.1 );
                //rec.train( "tests\\training", null, 0.1 );
                //rec.save( "data\\trained_6x4_net" );
            }
            catch( Exception e ){
                System.out.println( e.getMessage() );
            }
    }

    /**Recognize test images by net.
     * @param recognizer    Interface for work with teaching net.
     * @param tests_path    Place where are found test images.
     * @param output_path   File with testing results.
     */
     public static void runTests( Recognizer recognizer, String tests_path, String output_path )
            throws Exception{
            String[] all_files = new File( tests_path ).list();
            if ( all_files == null ){
                 throw new Exception( "Error --TestProject.runTests()-- Wrong testing directory or I/O error occurs." );
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
            throw new Exception( "Error. --TestProject.runTests()--" + e.getMessage() );
        }
        finally{
            if ( log != null ){
                try{
                    log.close();
                }
                catch( Exception e ){
                    throw new Exception( "Error. --TestProject.runTests()--" + e.getMessage() );
                }
            }
        }


     }
}
