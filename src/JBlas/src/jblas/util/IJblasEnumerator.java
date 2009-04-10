package jblas.util;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 20.02.2009
 * Time: 18:16:33
 * To change this template use File | Settings | File Templates.
 */
public interface IJblasEnumerator extends IEnumerator{

    Density getDensityType();

    float getVal  ();
    int   getIndex();
}
