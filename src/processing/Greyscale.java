package processing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class Greyscale
{
    public static BufferedImage work(BufferedImage in)
    {
        int w = in.getHeight();
        int h = in.getWidth();

        BufferedImage out = in.getSubimage(0, 0, w, h);

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                int rgb = in.getRGB(i, j);
                int r = (rgb & 0x00ff0000) >> 16;
                int g = (rgb & 0x0000ff00) >> 8;
                int b = (rgb & 0x000000ff);
            //  rgb = 0x0 | (b << 16) | (g << 8) | r; // Swap Red and Blue.
                int c = (r + b + g) / 3;
                rgb = (c << 16) | (c << 8) | c;
                out.setRGB(i, j, rgb);
            }
        }

        return out;
    }
}
