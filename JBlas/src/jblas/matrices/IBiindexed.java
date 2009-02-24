package jblas.matrices;

/**
 * @author M-NPO
 */
public interface IBiindexed {
    int getColCount();

    int getRowCount();

    float get(int i, int j);

    void set(int i, int j, float val);
}
