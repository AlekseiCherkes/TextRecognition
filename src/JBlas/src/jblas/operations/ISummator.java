package jblas.operations;

import jblas.vectors.IVector;
import jblas.matrices.IMatrix;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 06.04.2009
 * Time: 21:21:00
 * To change this template use File | Settings | File Templates.
 */
public interface ISummator {

    void add(IVector left  , IVector right, IVector destination);
    void sub(IVector left  , IVector right, IVector destination);
    void mul(IVector source, float   scale, IVector destination);

    void add(IMatrix left  , IMatrix right, IMatrix destination);
    void sub(IMatrix left  , IMatrix right, IMatrix destination);
    void mul(IMatrix source, float   scale, IMatrix destination);
}
