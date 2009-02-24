package jblas.vectors;

/**
 * @author M-NPO
 */
public interface IColumnView extends IVector {

    int  getColumnIndex();
    void setColumnIndex(int index);
}
