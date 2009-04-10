package jblas.util;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 04.04.2009
 * Time: 23:47:55
 * To change this template use File | Settings | File Templates.
 */
public interface IEnumerator<TItem> {

    boolean move ();
    void    reset();

    TItem  current();
}
