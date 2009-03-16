package processing;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import processing.Greyscale;
import processing.FlipHorizontal;
import processing.FlipVertical;
import processing.Binarization;

public class TestProcessing
{
    public static void main(String[] args) throws IOException
    {
//        System.out.printf("TestProcessing running with arguments:\t");
//        for(int i = 0; i < args.length; ++i ) {
//            System.out.printf("%s ", args[i]);
//        }
//        System.out.printf("\n");
//
//        if(2 != args.length) {
//            System.out.printf("Arguments error.\n");
//            System.out.printf("Usage: TestProcessing <input image> <output image>\n");
//            System.exit(-1);
//        }
//
//        File fin = new File(args[0]);
//        File fout = new File(args[1]);
        File fin = new File("data\\teaching_set\\A\\0.png");
        File fout = new File("data\\teaching_set\\A\\res.png");
        BufferedImage iin = ImageIO.read(fin);
        BufferedImage iout = Binarization.work(iin);
        ImageIO.write(iout, "jpg", fout);
    }
}
