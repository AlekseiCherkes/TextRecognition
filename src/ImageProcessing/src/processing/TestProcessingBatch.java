package processing;

import processing.Binarization;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;

public class TestProcessingBatch {
    public static void main(String[] args) throws IOException {
        System.out.printf("TestProcessing running with arguments:\t");
        for (int i = 0; i < args.length; ++i) {
            System.out.printf("%s ", args[i]);
        }
        System.out.printf("\n");

        if (2 != args.length) {
            System.out.printf("Arguments error.\n");
            System.out.printf("Usage: TestProcessing <dir with input images> <dir for results>\n");
            System.exit(-1);
        }

        File outd = new File(args[1]);
        outd.mkdir();

        File ind = new File(args[0]);
        String[] flist = ind.list();
        for (File fin : ind.listFiles()) {
            File fout = new File(outd.getAbsolutePath() + "\\" + fin.getName());
            BufferedImage iin = ImageIO.read(fin);
            BufferedImage iout = Binarization.work(iin);
            ImageIO.write(iout, "jpg", fout);
        }
    }
}
