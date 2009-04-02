import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class View extends QWidget {
    public Signal1<Boolean> valid = new Signal1<Boolean>();

    public View(QWidget parent) {
        super(parent);

        int size = 40;
        QPixmap bg = new QPixmap(size, size);
        bg.fill(QColor.white);
        QPainter p = new QPainter();
        p.begin(bg);
        p.fillRect(0, 0, size / 2, size / 2, new QBrush(QColor.lightGray));
        p.fillRect(size / 2, size / 2, size / 2, size / 2, new QBrush(QColor.lightGray));
        p.end();

        QPalette pal = palette();
        pal.setBrush(backgroundRole(), new QBrush(bg));
        setPalette(pal);

        setAutoFillBackground(true);

        delayedUpdate.setDelay(10);
    }

    public QImage image() {
        return source_original;
    }

    public void setImage(QImage original) {
        this.source_original = original != null ? original.convertToFormat(QImage.Format.Format_ARGB32_Premultiplied) : null;
        resetImage();

        valid.emit(original != null);
    }

    public QImage modifiedImage() {
        return source_modified;
    }

    public QSize sizeHint() {
        return new QSize(500, 500);
    }

    protected void paintEvent(QPaintEvent e) {
        if (background == null) {
            background = new QPixmap(size());
            QPainter p = new QPainter(background);
            QLinearGradient lg = new QLinearGradient(0, 0, 0, height());
            lg.setColorAt(0.5, QColor.black);
            lg.setColorAt(0.7, QColor.fromRgbF(0.5, 0.5, 0.6));
            lg.setColorAt(1, QColor.fromRgbF(0.8, 0.8, 0.9));
            p.fillRect(background.rect(), new QBrush(lg));
            p.end();
        }

        QPainter p = new QPainter(this);
        p.drawPixmap(0, 0, background);

        if (source_modified == null)
            updateImage();

        if (source_modified != null && !source_modified.isNull()) {
            p.setViewport(rect().adjusted(10, 10, -10, -10));

            int w = source_modified.width();
            int h = source_modified.height();

            QSize size = size();
            QSize isize_v = new QSize(w, 2 * h);
            QSize isize_h = new QSize(2 * w, h);

            isize_v.scale(size, Qt.AspectRatioMode.KeepAspectRatio);
            isize_h.scale(size, Qt.AspectRatioMode.KeepAspectRatio);

            QRect rect1 = new QRect();
            QRect rect2 = new QRect();

            if (isize_h.width() * isize_h.height() >
                    isize_v.width() * isize_v.height()) {

                QSize isize = isize_h;
                w = isize.width() / 2;
                h = isize.height();

                QRect rect = new QRect(width() / 2 - isize.width() / 2,
                        size.height() / 2 - isize.height() / 2,
                        isize.width(), isize.height());

                int dw = (int) (0.1 * w);
                int dh = (int) (0.1 * h);

                rect1 = new QRect(rect.x() + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
                rect2 = new QRect(rect.x() + w + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
            } else {
                QSize isize = isize_v;
                w = isize.width();
                h = isize.height() / 2;

                QRect rect = new QRect(width() / 2 - isize.width() / 2,
                        size.height() / 2 - isize.height() / 2,
                        isize.width(), isize.height());

                int dw = (int) (0.1 * w);
                int dh = (int) (0.1 * h);

                rect1 = new QRect(rect.x() + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
                rect2 = new QRect(rect.x() + dw, rect.y() + h + dh, w - 2 * dw, h - 2 * dh);
            }

            p.drawImage(rect1, source_modified);
            p.drawImage(rect2, source_modified);
        }

        p.end();
    }

    protected void resizeEvent(QResizeEvent e) {
        if (background != null) {
            background.dispose();
            background = null;
        }

        resetImage();
    }

    private final void resetImage() {
        if (source_modified != null)
            source_modified.dispose();
        source_modified = null;
        delayedUpdate.start();
    }

    private static final QColor decideColor(int value, QColor c1, QColor c2) {
        QColor c = value < 0 ? c1 : c2;
        double sign = value < 0 ? -1.0 : 1.0;
        return QColor.fromRgbF(c.redF(), c.greenF(), c.blueF(), sign * value * 0.5 / 100);
    }

    private void updateImage() {
        if (source_original == null || source_original.isNull())
            return;

        if (source_modified != null)
            source_modified.dispose();

        source_modified = source_original.copy();

        QPainter p = new QPainter();
        p.begin(source_modified);
        p.setCompositionMode(QPainter.CompositionMode.CompositionMode_SourceAtop);
        p.end();
    }

    private QImage source_original;
    private QImage source_modified;
    private QImage processed_original;
    private QImage processed_modified;
    private QPixmap background;

    private Worker delayedUpdate = new Worker(this) {
        public void execute() {
            update();
        }
    };
}
