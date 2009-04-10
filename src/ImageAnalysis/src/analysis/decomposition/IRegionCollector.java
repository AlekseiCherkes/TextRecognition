package analysis.decomposition;

import analysis.data.acumulators.StatisticsAccumulator;

/**
 * @author M-NPO
*/
public interface IRegionCollector<T> {
    void onImageRegion(T region, StatisticsAccumulator statistics);
}
