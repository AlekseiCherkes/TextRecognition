package neuro.io;

import jblas.matrices.Matrix;

import java.io.IOException;
import java.io.File;

/**
 * @author M-NPO
 */


public interface IImageCodec<TImage>{
    Matrix convert  (TImage image);
    TImage loadImage(File  source) throws IOException;
}
