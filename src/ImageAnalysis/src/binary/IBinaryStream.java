package binary;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 23.03.2009
 * Time: 2:04:11
 * To change this template use File | Settings | File Templates.
 */
public interface IBinaryStream {
    void addOnRowSwitchedListener();
    void removeOnRowSwitchedListener();

    float get();
    boolean moveNext();
}
