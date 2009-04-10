package jblas.operations;

import jblas.vectors.IVector;
import jblas.matrices.IMatrix;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 06.04.2009
 * Time: 21:19:43
 * To change this template use File | Settings | File Templates.
 */
public class RiaOperations implements ISummator, IMultiplier{

    @Override
    public void add(IVector left, IVector right, IVector destination) {
        int n = left.getLength();

        for (int i=0 ; i < n ; ++i){
            destination.set(i, left.get(i) + right.get(i));
        }
    }

    @Override
    public void sub(IVector left, IVector right, IVector destination) {
        int n = left.getLength();

        for (int i=0 ; i < n ; ++i){
            destination.set(i, left.get(i) - right.get(i));
        }
    }

    @Override
    public void mul(IVector source, float scale, IVector destination) {
        int n = source.getLength();

        for (int i=0 ; i < n ; ++i){
            destination.set(i, scale * source.get(i));
        }
    }

    @Override
    public void add(IMatrix left, IMatrix right, IMatrix destination) {
        int n = left.getRowCount();
        int m = left.getColCount();
        int j;

        for(int i=0 ; i < n ; ++i){
            for(j=0 ; j < m ; ++j){
                destination.set(i,j, left.get(i,j) + right.get(i,j));
            }
        }
    }

    @Override
    public void sub(IMatrix left, IMatrix right, IMatrix destination) {
        int n = left.getRowCount();
        int m = left.getColCount();
        int j;

        for(int i=0 ; i < n ; ++i){
            for(j=0 ; j < m ; ++j){
                destination.set(i,j, left.get(i,j) - right.get(i,j));
            }
        }
    }

    @Override
    public void mul(IMatrix source, float scale, IMatrix destination) {
        int n = source.getRowCount();
        int m = source.getColCount();
        int j;

        for(int i=0 ; i < n ; ++i){
            for(j=0 ; j < m ; ++j){
                destination.set(i,j, scale * source.get(i,j));
            }
        }
    }










    @Override
    public float dot(IVector left, IVector right) {
        float dot = 0;
        int n = left.getLength();

        for (int i=0 ; i < n ; ++i){
            dot += left.get(i) * right.get(i);
        }

        return dot;
    }

    @Override
    public void mul(IVector left, IMatrix right, IVector destination) {
        int n = right.getRowCount();
        int m = right.getColCount();
        int i;
        float dot;

        for(int j=0 ; j < m ; ++j){
            dot = 0;

            for(i=0 ; i < n ; ++i){
                dot += left.get(i) * right.get(i, j);
            }

            destination.set(j, dot);
        }
    }

    @Override
    public void mul(IMatrix left, IVector right, IVector destination) {
        int n = left.getRowCount();
        int m = left.getColCount();
        int j;
        float dot;

        for(int i=0 ; i < n ; ++i){
            dot = 0;

            for(j=0 ; j < m ; ++j){
                dot += left.get(i,j) * right.get(j);
            }

            destination.set(i, dot);
        }
    }

    @Override
    public void mul(IMatrix left, IMatrix right, IMatrix destination) {
        int n = destination.getRowCount();
        int m = destination.getColCount();
        int p = left       .getColCount();

        int j;
        int k;
        float dot;

        for(int i=0 ; i < n ; ++i){
            for(j=0 ; j < m ; ++j){
                dot = 0;
                for (k=0 ; k < p ; ++k){
                    dot += left.get(i,k) * right.get(k, j);
                }

                destination.set(i,j, dot);
            }
        }

    }
}
