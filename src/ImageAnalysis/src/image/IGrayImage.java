package image;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 21.02.2009
 * Time: 20:49:14
 * To change this template use File | Settings | File Templates.
 */
public interface IGrayImage {

    int getWidth ();
    int getHeight();

    float get(int x, int y);
    void  set(int x, int y, float hue);

    boolean isForeground(float hue);
}
