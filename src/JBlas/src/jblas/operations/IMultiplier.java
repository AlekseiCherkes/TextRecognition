package jblas.operations;

import jblas.vectors.IVector;
import jblas.matrices.IMatrix;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 06.04.2009
 * Time: 21:25:23
 * To change this template use File | Settings | File Templates.
 */
public interface IMultiplier {

    float dot(IVector left, IVector right);

    void mul(IVector left, IMatrix right, IVector destination);
    void mul(IMatrix left, IVector right, IVector destination);
    void mul(IMatrix left, IMatrix right, IMatrix destination);
}
