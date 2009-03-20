import neuro.adapter.Recognizer;
import neuro.adapter.Tutor;
import neuro.layer.ActiveLayer;
import neuro.activation_func.Sigmoid;
import neuro.net.RecognizeType;
import neuro.net.TrainingPerceptron;
import neuro.net.StaticPerceptron;
import neuro.net.TeachingData;


import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import jblas.matrices.Matrix;

/** @author    Vadim Shpakovsky. */

public class TestingClass {
        public static void main(String[] args){
            int m = 20;
            int n = 20;
            int input = m * n;
            int output = 26;
            int inner_layer = ( input + output ) / 2;
            double teaching_speed = 0.5;
            double output_accuracy = 0.1;
            double idle_accuracy = 1e-7;
            double shift = 0.;

            // Create two layer perceptron:
            // input        (m x n)
            // first layer  (input x inner_layer)
            // second layer (inner_layer x output)
            // out          output
            ArrayList<ActiveLayer> layers_list = new ArrayList<ActiveLayer>();
            Sigmoid s = new Sigmoid( shift );

            Matrix w = new Matrix( input, inner_layer );
            ActiveLayer layer = new ActiveLayer( w, s);
            layers_list.add( layer.copy() );
            w = new Matrix( inner_layer, output  );
            layer = new ActiveLayer( w, s);
            layers_list.add( layer.copy() );


            try{
                // Transform all teaching images to binary form.

                //Binarization.work( teaching_path, "jpg" );

//                TrainingPerceptron training_net = new TrainingPerceptron( layers_list, m, n, teaching_speed );
//                training_net.setTeachingPath( "data//for_teaching//teaching_set" );
//                training_net.setControlPath( "data//for_teaching//control_set" );
//                training_net.setBriefLog( "data//brief_log.txt" );
////                training_net.setDetailedLog( "data//detailed_log.txt");
//                training_net.setMinInput( 0. );
//                training_net.setMaxInput( 1. );
//                training_net.setOutputAccuracy( output_accuracy );
//                training_net.setIdleAccuracy( idle_accuracy );
//                training_net.setPrintAccuracy( 8 );
//
//                double ceiling = 4. / ( m * n );
//                training_net.randomInit( ceiling );
////                training_net.init( "data\\nets\\32x32_test" );
//
//                Tutor teacher = new Tutor( training_net );
//                //teacher.initNet( "data\\nets\\32x32_test" );
//                teacher.train();
//                teacher.save( "data\\nets\\32x32_test");
//
                StaticPerceptron static_net = new StaticPerceptron( layers_list, m, n );
                Recognizer recognizer = new Recognizer( static_net );
                recognizer.initNet( "data\\nets\\32x32_test" );

                runTests( recognizer, "data\\for_recognition", "data\\test_result.txt" );
            }
            catch( Exception e ){
                StackTraceElement[] frames = e.getStackTrace();
                System.out.println( "Error! " + e.getMessage() );
                for ( StackTraceElement frame : frames ){
                    System.out.print( frame.getClassName() + "." + frame.getMethodName() + "()\n" );
                }
            }
    }

    /**Recognize test images by net.
     * @param recognizer    Interface for work with net.
     * @param tests_path    Place where are found test images.
     * @param output_path   File with testing results.
     * @throws Exception    When are problems with directory location.
     */
     public static void runTests( Recognizer recognizer, String tests_path, String output_path )
            throws Exception{
        String[] all_files = new File( tests_path ).list();
        if ( all_files == null ){
             throw new Exception( "Wrong testing directory or I/O error occurs." );
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
        finally{
            if ( log != null ){
                try{
                    log.close();
                }
                catch( Exception e ){
                    throw new Exception( "Problem with file close." );
                }
            }
        }
     }
}
