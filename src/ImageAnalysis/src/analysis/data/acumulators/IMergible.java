package analysis.data.acumulators;

/**
 * Author: M-NPO
 * Date: 10.04.2009
 * Time: 0:38:27
 */
public interface IMergible<
        TExact extends IMergible<
                ? super TExact
        >
> {
    /**
     * Consume and incorporate the data, contained in the given object.
     * Merging object with itself is not supposed.
     * @param other the akin object
     */
    void merge(TExact other);
}
