package analysis.decomposition;

import analysis.data.acumulators.DecomposedRegion;

/**
 * @author M-NPO
*/
public interface IRegionCollector<T> {
    void onImageRegion(DecomposedRegion region, T context);
}
