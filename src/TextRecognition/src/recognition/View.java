package recognition;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;
import recognition.Worker;

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

    public void setImages(QImage source, QImage processed) {
        this.source = source != null ? source.convertToFormat(QImage.Format.Format_ARGB32_Premultiplied) : null;
        this.processed = processed != null ? processed.convertToFormat(QImage.Format.Format_ARGB32_Premultiplied) : null;

        if (source != null && processed != null) {
            assert source.size() == processed.size();
        }
        update();
        valid.emit(source != null && processed != null);
    }

    public QImage getSourceImage() {
        return source;
    }

    public QImage getProcessedImage() {
        return processed;
    }

    public QSize sizeHint() {
        return new QSize(500, 500);
    }

    protected void paintEvent(QPaintEvent e) {
        if (true || background == null) {
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

        if (source != null && !source.isNull()) {
            int w = source.width();
            int h = source.height();

            QSize size = size();
            QSize isize_v = new QSize(w, 2 * h);
            QSize isize_h = new QSize(2 * w, h);

            isize_v.scale(size, Qt.AspectRatioMode.KeepAspectRatio);
            isize_h.scale(size, Qt.AspectRatioMode.KeepAspectRatio);

            QRect rect1 = new QRect();
            QRect rect2 = new QRect();

            if (isize_h.width() * isize_h.height() >=
                    isize_v.width() * isize_v.height()) {
                QSize isize = isize_h;
                w = isize.width() / 2;
                h = isize.height();

                QRect rect = new QRect(width() / 2 - isize.width() / 2,
                        size.height() / 2 - isize.height() / 2,
                        isize.width(), isize.height());

                int dw = (int) (0.1 * w);
                int dh = (int) (0.1 * h);

                rect1.setRect(rect.x() + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
                rect2.setRect(rect.x() + w + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
            } else {
                QSize isize = isize_v;
                w = isize.width();
                h = isize.height() / 2;

                QRect rect = new QRect(width() / 2 - isize.width() / 2,
                        size.height() / 2 - isize.height() / 2,
                        isize.width(), isize.height());

                int dw = (int) (0.1 * w);
                int dh = (int) (0.1 * h);

                rect1.setRect(rect.x() + dw, rect.y() + dh, w - 2 * dw, h - 2 * dh);
                rect2.setRect(rect.x() + dw, rect.y() + h + dh, w - 2 * dw, h - 2 * dh);
            }

            p.drawImage(rect1, source);
            if (processed != null && !processed.isNull()) {
                p.drawImage(rect2, processed);
            }
        }

        p.end();
    }

    private QImage source;
    private QImage processed;
    private QPixmap background;

    private Worker delayedUpdate = new Worker(this) {
        public void execute() {
            update();
        }
    };
}
