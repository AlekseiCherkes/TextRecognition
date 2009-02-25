package processing;

import java.awt.image.BufferedImage;

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
}
