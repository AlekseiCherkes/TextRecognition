package jblas.util;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 20.02.2009
 * Time: 22:54:17
 * To change this template use File | Settings | File Templates.
 */
public class Sparse2DenseEnumerator implements IJblasEnumerator {
    private IJblasEnumerator sparse_m;
    private int outerCursor_m;


    private int limit_m;
    private boolean isFinishing_m;

    public Sparse2DenseEnumerator(IJblasEnumerator source, int limit) {
        setSource(source,limit);
    }

    public IJblasEnumerator getSource(){
        return sparse_m;
    }
    public void setSource(IJblasEnumerator source, int limit){
        sparse_m = source;
        limit_m  = limit;
        isFinishing_m = false;
        /*no reset call here */
    }

    public Density getDensityType() {
        return Density.DENSE;
    }

    public boolean move() {
        if (!isFinishing_m && outerCursor_m == sparse_m.getIndex()){
            isFinishing_m = !sparse_m.move();
        }
        return (++outerCursor_m < limit_m);
    }

    public void reset() {
        outerCursor_m = -1;
        isFinishing_m = false;
        sparse_m.reset();
    }

    public Object current() {
        return null;
    }

    public float getVal() {
        if (!isFinishing_m && outerCursor_m == sparse_m.getIndex()){
            return sparse_m.getVal();
        }
        return 0;
    }

    public int getIndex() {
        return outerCursor_m;
    }
}
