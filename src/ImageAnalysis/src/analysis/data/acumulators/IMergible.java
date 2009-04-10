package analysis.data.acumulators;

/**
 * Created by IntelliJ IDEA.
 * Author: M-NPO
 * Date: 10.04.2009
 * Time: 0:38:27
 * To change this template use File | Settings | File Templates.
 */
public interface IMergible<TExact extends IMergible<TExact>> {
    /**
     * Consume and incorporate the data, contained in the given object.
     * Merging object with itself is not supposed.
     * @param other the akin object
     */
    void merge(TExact other);
}
