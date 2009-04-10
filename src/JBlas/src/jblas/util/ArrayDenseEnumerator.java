package jblas.util;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 20.02.2009
 * Time: 20:09:09
 * To change this template use File | Settings | File Templates.
 */
public class ArrayDenseEnumerator implements IJblasEnumerator {
    protected float[] arr_m;
    protected int  cursor_m;

    public ArrayDenseEnumerator(float[] source){
        arr_m = source;
        reset();
    }

    public Density getDensityType() {
        return Density.DENSE;
    }

    public boolean move() {
        return (++cursor_m) < arr_m.length;
    }

    public void reset(){
        cursor_m = -1;
    }

    public Object current() {
        return null;
    }

    public float getVal() {
        return arr_m[cursor_m];
    }

    public int getIndex() {
        return cursor_m;
    }
}
