package processing;

import java.awt.image.BufferedImage;

public class Binarization
{
    public static BufferedImage work(BufferedImage in)
    {
        int w = in.getHeight();
        int h = in.getWidth();

        BufferedImage out = in.getSubimage(0, 0, w, h);

        // TODO

        return out;
    }
}
