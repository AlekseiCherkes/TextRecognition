package analysis.decomposition;

import analysis.data.acumulators.PackAccumulator;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.acumulators.DecomposedRegion;
import analysis.image.QImageAdapter;
import com.trolltech.qt.gui.QImage;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 31.03.2009
 * Time: 16:31:48
 * To change this template use File | Settings | File Templates.
 */
public class RegionInterpriter {


    private QImageAdapter adapter_m;
    private QImageAdapter greysource_m;


    public RegionInterpriter(){
        adapter_m     = new QImageAdapter(null);
        greysource_m  = new QImageAdapter(null);
    }


    public QImage getSource() {
        return greysource_m.getSource();
    }
    public void setSource(QImage source) {
        greysource_m.setSource(source);
    }

    public QImage InterpriteImageData(DecomposedRegion data, StatisticsAccumulator statistics){
        int x_min = statistics.getXMin();
        int y_min = statistics.getYMin();
        int width = statistics.getXMax() - x_min + 1;
        int heigt = statistics.getYMax() - y_min + 1;

        QImage source = getSource();
        QImage.Format format = source.format();
        QImage result = new QImage(width, heigt, format);
        /*adapter_m.setSource(result);

        GreyRegionView view = new GreyRegionView(adapter_m);
        view.setXMin  (-x_min);
        view.setYMin  (-y_min);
        view.setWidth (width);
        view.setHeight(heigt);
        
        analysis.data.copyPixels(greysource_m, view);
        adapter_m.setSource(null);*/
        //data.copyPixels(source, result, x_min, y_min);
        return result;
    }
}

