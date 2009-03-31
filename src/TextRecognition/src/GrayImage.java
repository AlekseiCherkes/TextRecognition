import image.IGrayImage;
import com.trolltech.qt.gui.QImage;
import processing.Greyscale;

public class GrayImage implements IGrayImage {

    public GrayImage(QImage image) {
        this.image = image;
        updateThreshold();
    }

    public int getWidth() {
        return image.width();
    }

    public int getHeight() {
        return image.height();
    }

    public float get(int x, int y) {
        int rgb = image.pixel(x, y);
        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b = (rgb & 0x000000ff);

        return (r + b + g) / (255.0f * 3.0f);
    }

    public void set(int x, int y, float hue) {
        int c = (int) hue * 255;
        int r = (c & 0x00ff0000) >> 16;
        int g = (c & 0x0000ff00) >> 8;
        int b = (c & 0x000000ff);

        image.setPixel(x, y, (c << 16) | (c << 8) | c);
        updateThreshold();
    }

    // Не будет корректно работать, если картинка изменяется методом set т.к
    // нужно пересчитывать порог.
    public boolean isForeground(float hue) {
        return (hue * 255.0f) > threshold;
    }

    private void updateThreshold() {
        QImage tmp = Greyscale.work(image);

        int w = image.width();
        int h = image.height();
        int sum = 0;

        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                sum += tmp.pixel(i, j) & 0xff;
            }
        }

        threshold = sum / (w * h);
    }

    private QImage image;
    private int threshold;
}
