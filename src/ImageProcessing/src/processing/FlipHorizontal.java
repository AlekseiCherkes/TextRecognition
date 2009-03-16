package processing;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class FlipHorizontal
{
    public static BufferedImage work(BufferedImage in)
    {
        int h = in.getHeight();
        int w = in.getWidth();

        BufferedImage out = in.getSubimage(0, 0, w, h);

        for(int i = 0; i < w / 2; ++i) {
            for(int j = 0; j < h; ++j) {
                int i2 = w - i -1;
                int c1 = out.getRGB(i, j);
                int c2 = out.getRGB(i2, j);
                out.setRGB(i2, j, c1);
                out.setRGB(i, j, c2);
            }
        }

        return out;
    }
}