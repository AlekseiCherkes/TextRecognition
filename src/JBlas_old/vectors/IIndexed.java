package jblas.vectors;

/**
 * @author M-NPO
 */
public interface IIndexed
{
    int getLength();

    float get(int i);
    void  set(int i, float val);
}
