package neuro.net;

import jblas.matrices.Matrix;

import java.util.List;

/**
 * @author Vadim Shpakovsky.
 */

public class ReadonlyAdapter implements INet{
    private INet source_m;

    public ReadonlyAdapter(INet source) {
        source_m = source;
    }

    public INet clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Share the instance you have!");
    }

    public void print(int precision) {
        source_m.print(precision);
    }

    public String getType() {
        return source_m.getType();
    }

    public int getInputSize() {
        return source_m.getInputSize();
    }

    public int getInputWidth() {
        return source_m.getInputWidth();
    }

    public int getInputHeight() {
        return source_m.getInputHeight();
    }

    public int getOutputSize() {
        return source_m.getOutputSize();
    }

    public List<String> getRecognizingTypes() {
        return source_m.getRecognizingTypes();
    }

    public Matrix recognize(Matrix x) throws Exception {
        return source_m.recognize(x);
    }

    public RecognizedType recognizeClass(Matrix x) throws Exception {
        return source_m.recognizeClass(x);
    }

    public void randomInit(double max_val) {
        throw new UnsupportedOperationException();
    }

    public void save(String storage) throws Exception {
        source_m.save(storage);
    }
}
