package decomposition;

/**
 * @author M-NPO
*/
public interface IRegionCollector<T> {
    void onImageRegion(T region, FigureStatistics statistics);
}
