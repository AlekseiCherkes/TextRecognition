package jblas.vectors;

import jblas.util.IJblasEnumerator;
import jblas.util.Density;

/**
 * @author M-NPO
 */
public interface IVector extends IIndexed {

    Density getDensityType();

    IJblasEnumerator getEnumerator(Density density);
}
