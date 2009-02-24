package binary;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 21.02.2009
 * Time: 20:49:14
 * To change this template use File | Settings | File Templates.
 */
public interface IBinaryImage {

    int getWidth ();
    int getHeight();

    boolean get(int x, int y);
}
