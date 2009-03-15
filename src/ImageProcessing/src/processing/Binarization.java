package processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Binarization
{
    public static BufferedImage work(BufferedImage in)
    {
        int w = in.getHeight();
        int h = in.getWidth();

        BufferedImage out = Greyscale.work(in);

        int sum = 0;
        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j ) {
                sum += out.getRGB(i, j) & 0xff;
            }
        }

        int threshold = sum / (w * h);
        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < w; ++j) {
                int c = out.getRGB(i, j) & 0xff;
                if(c > threshold)
                    out.setRGB(i, j, -1);
                else
                    out.setRGB(i, j, 0);
            }
        }

        return out;
    }

    /**Binarize all files in 'path' directory and it's subdirectories.
     * IMPORTANT!!! All files and directories begin with '.' are ignored.
     * Each binarizing file is replaced by it's binarizing copy.
     * @param path          Root directory.
     * @param formatName    Informal name of the format.
     */
    public static void work( String path,  String formatName )
        throws Exception{
        try{
            File dir = new File( path );
            String[] files = dir.list();
            if ( files == null ){
                return;
            }
            for ( String file_name : files ){
                // Ignore files and directories begin with '.'.
                if ( file_name.charAt( 0 ) == '.' ){
                    continue;
                }
                String full_file_name = path + "\\" + file_name;
                File file = new File( full_file_name );
                if ( file.isDirectory()  ){
                    Binarization.work( full_file_name, formatName );
                }
                else{
                    BufferedImage input_image = ImageIO.read( file );
                    BufferedImage output_image = Binarization.work( input_image );
                    ImageIO.write( output_image, formatName, file);
                }
            }
        }
        catch( Exception e ){
            throw new Exception( "Error --Binarization.work( String )--");
        }


    }
}
