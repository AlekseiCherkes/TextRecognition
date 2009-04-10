package jblas.vectors;

import jblas.util.*;

/**
 * @author M-NPO
 */
public class DenseVector implements IVector {

    private float[] cells_m;

    public DenseVector(int n){
        cells_m = new float[n];
    }

    public Density getDensityType() {
        return Density.DENSE;
    }

    public int getLength() {
        return cells_m.length;
    }


    public float get(int i) {
        return cells_m[i];
    }
    public void  set(int i, float val) {
        cells_m[i] = val;

    }

    public IJblasEnumerator getEnumerator(Density density) {
        IJblasEnumerator dense = new ArrayDenseEnumerator(cells_m);

        switch (density){
            case DENSE : return dense;
            case SPARSE: return new Dense2SparseEnumerator(dense);

            default: return null;
        }
    }
}
