package processing;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class FlipVertical
{
    public static BufferedImage work(BufferedImage in)
    {
        int h = in.getHeight();
        int w = in.getWidth();

        BufferedImage out = in.getSubimage(0, 0, w, h);

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h / 2; ++j) {
                int j2 = h - j - 1;
                int c1 = out.getRGB(i, j);
                int c2 = out.getRGB(i, j2);
                out.setRGB(i, j2, c1);
                out.setRGB(i, j, c2);
            }
        }

        return out;
    }
}
