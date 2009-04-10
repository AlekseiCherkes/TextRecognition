package jblas.matrices;

import jblas.vectors.IVector;
import jblas.util.IJblasEnumerator;
import jblas.util.Density;


/**
 * @author M-NPO
 */
public interface IMatrix extends IBiindexed {

    Density getDensityType();

    void swapRows(int i, int j);

    void swapCols(int i, int j);

    void combineRows(int src, int dst, float scale);  //M[dst, :] += M[src, :]*scale

    void combineCols(int src, int dst, float scale);  //M[:, dst] += M[:, src]*scale

    IVector getCol(int j);

    IVector getRow(int i);

    IJblasEnumerator getEnumeratorOwerRow(int row, Density density);
    IJblasEnumerator getEnumeratorOwerCol(int col, Density density);

    
}
