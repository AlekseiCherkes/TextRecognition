package recognition;

import analysis.data.acumulators.StatisticsAccumulator;
import analysis.decomposition.*;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.*;
import neuro.adapter.Recognizer;
import neuro.net.RecognizedType;
import neuro.net.StaticPerceptron;
import processing.Binarization;

import java.util.ArrayList;

public class MainWindow extends QMainWindow {
    private Ui_MainWindow ui = new Ui_MainWindow();
    private QDirModel dirModel;
    private ImageTableModel imageModel;
    private View view;

    private ArrayList<StatisticsAccumulator> statisticsList;

    private Recognizer recognizer;

    private DecompositionFasade decomposer;
    private IRegionCollector<QImage> collector
            = new IRegionCollector<QImage>() {

        private int identity;

        @Override
        public void onImageRegion(QImage region, StatisticsAccumulator statistics) {
            String str = Integer.toString(identity++);
            region.save("data\\decomposed\\Out_image_" + str + ".bmp");
            statisticsList.add(statistics);
        }
    };

    public MainWindow() {
        ui.setupUi(this);
        setupTableView();
        setupView();
        readSettings();

        try {
            StaticPerceptron staticPerceptron = new StaticPerceptron();
            staticPerceptron.init("data/nets/20x20.net");
            recognizer = new Recognizer(staticPerceptron);
            statusBar().showMessage("Network opened");
        }
        catch (Exception e) {
            statusBar().showMessage("File open error");
        }

          decomposer = new TrueDecomposition();
        //  decomposer = new DecompositionFasade();
    }

    private QImage drawStatistics(QImage image) {
        QPainter p = new QPainter(image);
        p.setBrush(new QColor(255, 0, 0, 127));
        p.setPen(new QColor(0, 0, 0, 0));
        for(StatisticsAccumulator stat : statisticsList) {
            p.drawRect(stat.getXMin(), stat.getYMin(),
                       stat.getXMax() - stat.getXMin() + 1, stat.getYMax() - stat.getYMin() + 1);
        }
        p.end();
        return image;
    }

    public void on_dirView_activated(QModelIndex index) throws Exception {
        QFileInfo info = dirModel.fileInfo(index);
        String s;
        if (info.isDir()) {
            statusBar().showMessage("This is directory");
        } else {
            QImage image = new QImage();
            if (image.load(info.absoluteFilePath())) {
                statisticsList = new ArrayList<StatisticsAccumulator>();
                decomposer.decompose(image, collector);
                view.setImages(image, drawStatistics(Binarization.work(image)));

                if (recognizer == null) {
                    s = "Recognizer hasn't been loaded.";
                    statusBar().showMessage("Network wasn't opened");
                } else {
                    try {
                        RecognizedType type = recognizer.recognize(info.absoluteFilePath());
                        String t = type.getType(); // Не рефакторить!!!                        
                        s = t;
                        s += "\t" + Double.toString(((int) Math.round(type.getAccuracy() * 100.)) / 100.);
                    }
                    catch (Exception e) {
                        s = "Can't recognize analysis.image." + "\n" + e.getMessage();
                    }
                }
                ui.recognitionLabel.setText(s);
                statusBar().showMessage("Image opened");

            } else {
                statusBar().showMessage("Can't load analysis.image");
            }
        }
    }

    public void on_actionOpen_Network_triggered() throws Exception {
        String fileName = QFileDialog.getOpenFileName(this, "File to open", "*.net");
        if (fileName.length() == 0 || !fileName.toLowerCase().endsWith("net")) {
            statusBar().showMessage("Not openning file");
        } else {
            try {
                StaticPerceptron staticPerceptron = new StaticPerceptron();
                staticPerceptron.init(fileName);
                recognizer = new Recognizer(staticPerceptron);
                statusBar().showMessage("Network opened");
            }
            catch (Exception e) {
                statusBar().showMessage("File open error");
            }
        }
    }

    public void on_tableView_activated(QModelIndex index) {
        //    view.setImages(imageModel.imageAt(index.row()));
        //    statusBar().showMessage("Displaying analysis.image");
    }

    public void on_resetColorBalance_clicked() {
//        ui.redCyanBalance.setValue(0);
//        ui.greenMagentaBalance.setValue(0);
//        ui.blueYellowBalance.setValue(0);
//        ui.colorBalance.setValue(0);
//        ui.inverted.setChecked(false);
    }

    public void on_actionSave_triggered() {
//        if (view.modifiedImage() == null) {
//            statusBar().showMessage("No analysis.image to save");
//            return;
//        }
//
//        String fileName = QFileDialog.getSaveFileName(this, "File to save", "*.png");
//        if (fileName.length() == 0 || !fileName.toLowerCase().endsWith("png")) {
//            statusBar().showMessage("Not saving analysis.image");
//            return;
//        }
//
//        view.modifiedImage().save(fileName, "PNG");
//
//        statusBar().showMessage("Image saved as '" + fileName + "'");
    }

    public void on_actionAbout_Qt_triggered() {
        QApplication.aboutQt();
    }

    public void on_actionAbout_Text_Recognition_triggered() {
        QDialog d = new QDialog(this);
        Ui_AboutTextRecognition ui = new Ui_AboutTextRecognition();
        ui.setupUi(d);
        //    ui.label.setPixmap(new QPixmap("classpath:com/trolltech/images/qt-logo.png"));

        QPalette pal = ui.textEdit.palette();
        pal.setBrush(QPalette.ColorRole.Base, d.palette().window());
        ui.textEdit.setPalette(pal);

        d.exec();
        d.dispose(); // No strictly needed, but it frees up memory faster.
    }

    public void on_actionAbout_Qt_Jambi_triggered() {
        QApplication.aboutQtJambi();
    }

    public void on_actionExit_triggered() {
        close();
    }

    public void on_actionClose_triggered() {
        view.setImages(null, null);
    }

    protected void showEvent(QShowEvent e) {
        if (dirModel == null)
            QTimer.singleShot(100, this, "setupDirModel()");
    }

    private void setupTableView() {
        imageModel = new ImageTableModel(this);
        ui.tableView.setModel(imageModel);
        ui.tableView.setIconSize(LazyPixmap.SMALL_SIZE);

        ui.tableView.clicked.connect(this, "on_resetColorBalance_clicked()");
    }

    public void setupDirModel() {
        if (dirModel != null)
            return;
        dirModel = new QDirModel(this);
        dirModel.setLazyChildCount(true);
        //  dirModel.setFilter(new QDir.Filters(QDir.Filter.Dirs, QDir.Filter.Drives, QDir.Filter.NoDotAndDotDot));
        dirModel.setFilter(new QDir.Filters(QDir.Filter.AllEntries, QDir.Filter.NoDotAndDotDot, QDir.Filter.TypeMask));

        ui.dirView.setModel(dirModel);

        for (int i = 1; i < ui.dirView.header().count(); ++i)
            ui.dirView.hideColumn(i);
        ui.dirView.header().hide();

        ui.dirView.header().setStretchLastSection(false);
        ui.dirView.header().setResizeMode(QHeaderView.ResizeMode.ResizeToContents);

        QDir dir = new QDir();
        dir.cd("trunk");
        dir.cd("data");
        //  QModelIndex initial = dirModel.index(dir.absolutePath());
        QModelIndex initial = dirModel.index(dir.absolutePath());
        if (initial != null) {
            ui.dirView.setCurrentIndex(initial);
            ui.dirView.activated.emit(initial);
        }

        ui.dirView.scrollTo(ui.dirView.currentIndex(), ScrollHint.PositionAtCenter);
    }

    private void setupView() {
        view = new View(this);
        setCentralWidget(view);

//        ui.redCyanBalance.valueChanged.connect(view, "setRedCyan(int)");
//        ui.greenMagentaBalance.valueChanged.connect(view, "setGreenMagenta(int)");
//        ui.blueYellowBalance.valueChanged.connect(view, "setBlueYellow(int)");
//        ui.colorBalance.valueChanged.connect(view, "setColorBalance(int)");
//        ui.inverted.toggled.connect(view, "setInvert(boolean)");

        view.valid.connect(ui.actionClose, "setEnabled(boolean)");
        view.valid.connect(ui.actionSave, "setEnabled(boolean)");
//        view.valid.connect(ui.groupBox, "setEnabled(boolean)");
    }

    public void closeEvent(QCloseEvent event) {
        writeSettings();
    }

    public void readSettings() {
        QSettings settings = new QSettings("Trolltech", "ImageViewer Example");
        resize((QSize) settings.value("size", new QSize(1000, 500)));
        QPoint point = (QPoint) settings.value("pos", null);
        if (point != null)
            move(point);
        restoreState((QByteArray) settings.value("state", null));
        settings.sync();
        settings.dispose();
    }

    public void writeSettings() {
        QSettings settings = new QSettings("Trolltech", "ImageViewer Example");
        settings.setValue("pos", pos());
        settings.setValue("size", size());
        settings.setValue("state", saveState());
        settings.sync();
        settings.dispose();
    }
}
