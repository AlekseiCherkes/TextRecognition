package jblas.vectors;

import jblas.util.IEnumerator;
import jblas.util.Density;

/**
 * @author M-NPO
 */
public interface IVector extends IIndexed {

    Density getDensityType();

    IEnumerator getEnumerator(Density density);
}
