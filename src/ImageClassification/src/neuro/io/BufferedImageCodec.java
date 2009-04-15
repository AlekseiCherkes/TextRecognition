package neuro.io;

import jblas.matrices.Matrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import processing.Binarization;

import javax.imageio.ImageIO;

/**
 * @author M-NPO
 */
public class BufferedImageCodec implements IImageCodec<BufferedImage> {

    @Override
    public Matrix convert(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();
        Matrix x = new Matrix ( h * w, 1 );
        for( int j = 0; j < h; ++j ){
            for( int i = 0; i < w; ++i ){
                int rgb = image.getRGB( i, j ) & 0xffffff;
                if ( rgb == 0){
                    x.set( i + j * h, 0, 1 );
                }
                else{
                    x.set( i + j * h, 0, 0 );
                }
            }
        }
        return x;
    }


    @Override
    public BufferedImage loadImage(File source) throws IOException {
        BufferedImage bi_image = Binarization.work( ImageIO.read( source ) );
        return null;
    }
}
