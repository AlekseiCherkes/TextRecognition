package jblas.util;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 20.02.2009
 * Time: 22:48:42
 * To change this template use File | Settings | File Templates.
 */
public class Dense2SparseEnumerator implements IEnumerator {
    private IEnumerator dense_m;

    public Dense2SparseEnumerator(IEnumerator source) {
        setSource(source);
    }

    public IEnumerator getSource(){
        return dense_m;
    }
    public void setSource(IEnumerator source){
        dense_m = source;
        /*no reset call here */
    }

    public Density getDensityType() {
        return Density.SPARSE;
    }

    public boolean move() {
        boolean res;

        do {
            res = dense_m.move();
        } while (res && dense_m.getVal() == 0);

        return res;
    }

    public void reset() {
        dense_m.reset();
    }

    public float getVal() {
        return dense_m.getVal();
    }

    public int getIndex() {
        return dense_m.getIndex();
    }
}
