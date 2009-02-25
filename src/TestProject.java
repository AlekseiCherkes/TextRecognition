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


            // two layer perceptron:
            // input 12x12
            // first layer 144x85
            // second layer 85x26
            // out 26
            ArrayList<Layer> list = new ArrayList<Layer>();
            Sigmoid s = new Sigmoid();
            //Matrix w = new Matrix( 144, 85, 0. );

            Matrix w = new Matrix( 24, 13, 0. );

            Layer layer = new Layer( w, s);
            list.add( layer.copy() );
            //w = new Matrix( 85, 26, 0. );
            w = new Matrix( 13, 2, 0. );
            layer = new Layer( w, s);
            list.add( layer.copy() );
            TwoLayerPerceptron net = new TwoLayerPerceptron( list, 0.7 );
            net.randomInit( 1. );

            //Recognizer rec = new Recognizer( net, 12, 12 );
            Recognizer rec0 = new Recognizer( net, 6, 4 );

            try{
                TestGenerator test_generator = new TestGenerator( 6, 4 );
                //test_generator.clearDir( "tests\\training\\not_A" );
                //test_generator.generate( "tests\\tests_images", "tests\\tests_images", 500 );

                //rec0.save( "data\\6x4_net" );
                Recognizer rec = new Recognizer( null, 6, 4 );
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
