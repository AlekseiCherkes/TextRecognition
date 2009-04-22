import neuro.adapter.Recognizer;
import neuro.adapter.PerceptronTutor;
import neuro.layer.ActiveLayer;
import neuro.activation_func.Sigmoid;
import neuro.net.RecognizedType;
import neuro.net.Perceptron;


import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;

import jblas.matrices.Matrix;

/** @author    Vadim Shpakovsky. */

public class TestingClass {
        public static void main(String[] args){
            int m = 20;
            int n = 20;
            int input = m * n;
            int output = 26;
            int inner_layer = ( input + output ) / 2;
            double teaching_speed = 0.7;
            double output_accuracy = 0.1;
            double idle_accuracy = 1e-6;
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
                Perceptron net_for_teaching = new Perceptron();
                net_for_teaching.setStructure( layers_list, n, m );
                // !!! This is important!
                double ceiling = 4. / ( m * n );
                net_for_teaching.randomInit( ceiling );
                PerceptronTutor tutor = new PerceptronTutor( net_for_teaching );


                tutor.setTeachingPath( "data//for_teaching//teaching_set" );
                tutor.setControlPath( "data//for_teaching//control_set" );
                tutor.setBriefLog( "data//brief_log.txt" );
                //tutor.setDetailedLog( "data//detailed_log.txt");
                tutor.setOutputAccuracy( output_accuracy );
                tutor.setIdleAccuracy( idle_accuracy );
                tutor.setPrintAccuracy( 8 );
                tutor.setTeachingSpeed( teaching_speed );

                tutor.train();
                tutor.save( "data\\nets\\20x20.net" );
//
//
//                NetAdapter static_net = new NetAdapter();
//                static_net.setStructure( layers_list, m, n );
//                Recognizer recognizer = new Recognizer( static_net );
//                recognizer.initNet( "data\\nets\\20x20.net" );
//
//                runTests( recognizer, "data\\for_recognition", "data\\test_result.txt" );
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
//        String[] all_files = new File( tests_path ).list();
//        if ( all_files == null ){
//             throw new Exception( "Wrong testing directory or I/O error occurs." );
//        }
//        ArrayList< String > tests = new ArrayList< String >();
//        for ( String file: all_files ){
//            String test_name = tests_path + "\\" + file;
//            File test_file = new File( test_name );
//            if ( file.charAt( 0 ) != '.' && test_file.isFile() ){
//                tests.add( test_name );
//            }
//        }
//
//        PrintWriter log = null;
//        try{
//            log = new PrintWriter( output_path );
//            for ( String test: tests ){
//                RecognizedType type = recognizer.recognize( test );
//                if ( type == null){
//                    log.printf("Test \"%s\": %s\n", test, "didn't recognize."  );
//                    continue;
//                }
//                log.printf("Test \"%s\": %s  %f\n", test, type.getType(), type.getAccuracy()  );
//            }
//        }
//        finally{
//            if ( log != null ){
//                try{
//                    log.close();
//                }
//                catch( Exception e ){
//                    throw new Exception( "Problem with file close." );
//                }
//            }
//        }
     }
}
