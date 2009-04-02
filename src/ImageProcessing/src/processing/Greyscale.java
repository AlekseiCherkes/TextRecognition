package processing;

import com.trolltech.qt.gui.QImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class Greyscale {
    public static QImage work(QImage in) {
        int h = in.height();
        int w = in.width();

        QImage out = in.clone();

        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                int rgb = in.pixel(i, j);
                int a = (rgb & 0xff000000) >> 24;
                int r = (rgb & 0x00ff0000) >> 16;
                int g = (rgb & 0x0000ff00) >> 8;
                int b = (rgb & 0x000000ff);
                int c = (r + b + g) / 3;
                rgb = (a << 24) | (c << 16) | (c << 8) | c;
                out.setPixel(i, j, rgb);
            }
        }

        return out;
    }

    public static BufferedImage work(BufferedImage in) {
        int h = in.getHeight();
        int w = in.getWidth();

        BufferedImage out = in.getSubimage(0, 0, w, h);

        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                int rgb = in.getRGB(i, j);
                int a = (rgb & 0xff000000) >> 24;
                int r = (rgb & 0x00ff0000) >> 16;
                int g = (rgb & 0x0000ff00) >> 8;
                int b = (rgb & 0x000000ff);
                //  rgb = 0x0 | (b << 16) | (g << 8) | r; // Swap Red and Blue.
                int c = (r + b + g) / 3;
                rgb = (a << 24) | (c << 16) | (c << 8) | c;
                out.setRGB(i, j, rgb);
            }
        }

        return out;
    }
}
