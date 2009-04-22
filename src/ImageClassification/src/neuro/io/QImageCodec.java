package neuro.io;

import com.trolltech.qt.gui.QImage;
import jblas.matrices.Matrix;
import neuro.io.IImageCodec;

import java.io.IOException;
import java.io.File;

/**
 * @author M-NPO
 */
public class QImageCodec implements IImageCodec<QImage> {


    /**Convert image to input data for net.
     * @param image        Qt image object.
     * @return             Input matrix for net.
     */
    @Override
    public Matrix convert(QImage image) {
        int h = image.height();
        int w = image.width();
        Matrix x = new Matrix ( h * w, 1 );
        for( int j = 0; j < h; ++j ){
            for( int i = 0; i < w; ++i ){
                int rgb = image.pixel( i, j ) & 0xffffff;
//                double val = min_input + rgb * ( max_input - min_input ) / 0xffffff;
//                x.set( i + j * h, 0, val );
                int threshold = 0xffffff / 2;
                if ( rgb >= threshold){
                    x.set( i + j * h, 0, 0 );
                }
                else{
                    x.set( i + j * h, 0, 1 );
                }
            }
        }
        return x;
    }


    @Override
    public QImage loadImage(File source) throws IOException {
        String path = source.getAbsolutePath();
        QImage image = new QImage(path);
        return image;
    }
}
