package decomposition;

import decomposition.ImageData;
import decomposition.FigureStatistics;
import image.IGreyImage;
import image.QImageAdapter;
import image.GreyRegionView;
import com.trolltech.qt.gui.QImage;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 31.03.2009
 * Time: 16:31:48
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataInterpriter {


    private QImageAdapter adapter_m;
    private QImageAdapter greysource_m;


    public ImageDataInterpriter(){
        adapter_m     = new QImageAdapter(null);
        greysource_m  = new QImageAdapter(null);
    }


    public QImage getSource() {
        return greysource_m.getSource();
    }
    public void setSource(QImage source) {
        greysource_m.setSource(source);
    }

    public QImage InterpriteImageData(ImageData data, FigureStatistics statistics){
        int x_min = statistics.getXMin();
        int y_min = statistics.getYMin();
        int width = statistics.getXMax() - x_min + 1;
        int heigt = statistics.getYMax() - y_min + 1;

        QImage result = new QImage(width, heigt, QImage.Format.Format_ARGB32);
        adapter_m.setSource(result);

        GreyRegionView view = new GreyRegionView(adapter_m);
        view.setXMin  (-x_min);
        view.setYMin  (-y_min);
        view.setWidth (width);
        view.setHeight(heigt);
        
        data.copyPixels(greysource_m, view);
        adapter_m.setSource(null);
        return result;
    }
}

