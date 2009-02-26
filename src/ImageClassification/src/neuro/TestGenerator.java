package neuro;

import java.util.Random;
import java.util.Calendar;
import java.io.PrintWriter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 24.02.2009
 * Time: 17:04:16
 * To change this template use File | Settings | File Templates.
 */

 /**Is used for generate testing images.
 */
public class TestGenerator {
    private int height;
    private int weight;

    public TestGenerator( int height, int weight ){
        this.height = height;
        this.weight = weight;
    }

     /** Generate testing image.
      *
      * @param path             Path for images.
      * @param copy_number      Number of generated images.
    */
    public void generate( String path, int copy_number )
            throws Exception{
         for ( int i = 0; i < copy_number; i++ ){
             int ones_count = 0;
             Matrix image = new Matrix( height, weight );
             Random rand = new Random();
             for ( int k = 0; k < image.getRowDimension(); k++ ){
                 for ( int j = 0; j < image.getColumnDimension(); j++ ){
                     double val = Math.round( rand.nextDouble() );
                     if (val == 1.0){
                         ones_count++;
                     }
                     image.set( k, j, val );
                 }
             }
             // Generate individual image's name.
             Calendar cal = Calendar.getInstance();
             String image_name = Long.toString( cal.getTimeInMillis() );
             image_name += "_" + Integer.toString( i );             
             path = path + "\\" + image_name;

             PrintWriter pw = null;
             try{
                pw = new PrintWriter( new File( path)  );
                image.print( pw, 2, 0 );
             }
             catch( Exception e ){
                 throw new Exception( "Error. --TestGenerator.generate()-- Problem with printing in file." +
                    e.getMessage() );
             }
             finally{
                 if ( pw != null ){
                        try{
                            pw.close();
                        }
                        catch( Exception e ){
                            e.getMessage();
                        }
                 }
            }
        }
    }

     /** Delete all files and subdirectorys inside directory.
      *
      * @param dir      Directory path.
    */
    public void clearDir( String dir ) throws Exception{
        try{
            String[] del_files = new File( dir ).list();
            for ( String file_name: del_files ){
                file_name = dir + "\\" + file_name;
                File deleting_file = new File( file_name );
                deleting_file.delete();
            }
        }
        catch( Exception e ){
            throw new Exception( "Error. --TestGenerator.clearDir()-- Problem with file/dir deletion. " +
                e.getMessage() );
        }
    }
}