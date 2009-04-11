package analysis.image;

import com.trolltech.qt.gui.QImage;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 31.03.2009
 * Time: 16:45:05
 * To change this template use File | Settings | File Templates.
 */
public class QImageAdapter implements IGreyImage {
    private QImage source_m;
    private float threshold_m = 0.001f;


    public float getThreshold_() {
        return threshold_m;
    }
    public void setThreshold_(float threshold) {
        threshold_m = threshold;
    }


    public QImage getSource() {
        return source_m;
    }
    public void setSource(QImage source) {
        source_m = source;
    }


    public QImageAdapter(QImage source){
        source_m = source;
    }

    @Override
    public int getWidth() {
        return source_m.width();
    }

    @Override
    public int getHeight() {
        return source_m.height();
    }

    @Override
    public float get(int x, int y) {
        int rgb = source_m.pixel(x, y);
        int hue = ( rgb & 0x000000ff)
                + ((rgb & 0x0000ff00) >>>  8)
                + ((rgb & 0x00ff0000) >>> 16);
        return 1.0f - hue / 765.f;
    }

    @Override
    public void set(int x, int y, float hue) {
       int r = Math.round((1 - hue)*255);
       int rgb = (r) | (r << 8) | (r << 16);
       source_m.setPixel(x, y, rgb);
    }

    @Override
    public boolean isForeground(float hue) {
        return hue >= threshold_m;
    }
}
