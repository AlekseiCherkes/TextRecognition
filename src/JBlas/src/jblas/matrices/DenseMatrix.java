package jblas.matrices;

import jblas.vectors.IRowView;
import jblas.vectors.IColumnView;
import jblas.util.IEnumerator;
import jblas.util.ArrayDenseEnumerator;
import jblas.util.Dense2SparseEnumerator;
import jblas.util.Density;


/**
 * @author M-NPO
 */
public class DenseMatrix implements IMatrix {

    /**
     * Data storage
     * first (left)   index is a row    index
     * right (second) index is a column index
     */
    private float[][] cells_m;


    public DenseMatrix(int rowCount, int colCount) {
        cells_m = new float[rowCount][colCount];
    }

    public DenseMatrix(int size) {
        this(size, size);
    }


    //////////////////////////////////// IBiindexed members /////////////////////////////////////
    public int getColCount() {
        return cells_m[0].length;
    }

    public int getRowCount() {
        return cells_m.length;
    }


    public float get(int i, int j) {
        return cells_m[i][j];
    }

    public void set(int i, int j, float val) {
        cells_m[i][j] = val;
    }


    //////////////////////////////////// IMatrix members  /////////////////////////////////////
    public Density getDensityType() {
        return Density.DENSE;
    }

    /**
     * Opened views will be not affected by this
     */
    public void swapRows(int i, int j) {
        float[] tmp = cells_m[i];
        cells_m[i] = cells_m[j];
        cells_m[j] = tmp;
    }

    public void swapCols(int i, int j) {
        float tmp;
        int rCnt = getRowCount();

        for (int row = 0; row < rCnt; ++row) {
            tmp = cells_m[row][i];
            cells_m[row][i] = cells_m[row][j];
            cells_m[row][j] = tmp;
        }
    }


    public void combineRows(int src, int dst, float scale) {
        int cCnt = getColCount();
        float[] dstArr = cells_m[dst];
        float[] srcArr = cells_m[src];

        for (int col = 0; col < cCnt; ++col) {
            dstArr[col] += srcArr[col] * scale;
        }
    }

    public void combineCols(int src, int dst, float scale) {
        int rCnt = getRowCount();

        for (int row = 0; row < rCnt; ++row) {
            cells_m[row][dst] += cells_m[row][src] * scale;
        }
    }


    public IColumnView getCol(int j) {
        ColView view = new ColView();
        view.setColumnIndex(j);
        return view;
    }

    public IRowView getRow(int i) {
        RowView view = new RowView();
        view.setRowIndex(i);
        return view;
    }

    public IEnumerator getEnumeratorOwerRow(int row, Density density) {
        IEnumerator dense = new ArrayDenseEnumerator(cells_m[row]);

        switch (density){
            case DENSE : return dense;
            case SPARSE: return new Dense2SparseEnumerator(dense);

            default: return null;
        }
    }

    public IEnumerator getEnumeratorOwerCol(int col, Density density) {
        IEnumerator dense = new ColDenseEnumerator(col);

        switch (density){
            case DENSE : return dense;
            case SPARSE: return new Dense2SparseEnumerator(dense);

            default: return null;
        }
    }






    protected class RowView implements IRowView {
        private float[] row;
        private int rowIndex;


        public int getRowIndex() {
            return rowIndex;
        }

        /**
         * Opened iterators will be not affected by this
         */
        public void setRowIndex(int index) {
            rowIndex = index;
            row = DenseMatrix.this.cells_m[rowIndex];
        }

        public int getLength() {
            return row.length;
        }

        public float get(int i) {
            return row[i];
        }

        public void set(int i, float val) {
            row[i] = val;
        }

        public Density getDensityType() {
            return Density.DENSE;
        }

        public IEnumerator getEnumerator(Density density) {
            return getEnumeratorOwerRow(rowIndex, density);
        }
    }








    protected class ColView implements IColumnView {
        private int colIndex;

        public int getColumnIndex() {
            return colIndex;
        }

        public void setColumnIndex(int index) {
            colIndex = index;
        }

        public int getLength() {
            return DenseMatrix.this.getRowCount();
        }

        public float get(int i) {
            return DenseMatrix.this.cells_m[i][colIndex];
        }

        public void set(int i, float val) {
            DenseMatrix.this.cells_m[i][colIndex] = val;
        }

        public Density getDensityType() {
            return Density.DENSE;
        }

        public IEnumerator getEnumerator(Density density) {
            return getEnumeratorOwerCol(colIndex, density);
        }
    }










    protected class ColDenseEnumerator implements IEnumerator {
        private int row_m;
        private int col_m;

        public ColDenseEnumerator(int col){
            col_m = col;
            reset();
        }

        public Density getDensityType() {
            return Density.DENSE;
        }

        public boolean move() {
            return (++row_m < cells_m.length);
        }

        public void reset() {
            row_m = -1;
        }

        public float getVal() {
            return cells_m[row_m][col_m];
        }

        public int getIndex() {
            return row_m;
        }
    }




}
