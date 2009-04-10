package analysis.decomposition;

import com.trolltech.qt.gui.QImage;
import analysis.decomposition.IRegionCollector;
import analysis.decomposition.DecompositionContext;
import analysis.data.acumulators.StatisticsAccumulator;

/**
 * @author M-NPO
 */
public class DecompositionFasade {

    public void decompose(QImage source, IRegionCollector<QImage> handler){
        DecompositionContext decomposer = new DecompositionContext();
        //IGreyImage greyImage = new QImageAdapter(source);


        int N = 3;


        int dw = (int) (source.width() * 0.3 / (2 * N));
        int w  = (int) (source.width() * 0.7 / N);
        int h1 = (int) (0.3 * source.height() / 2.0);
        int h2 = (int) (0.7 * source.height());


        for (int i=0 ; i<N ; ++i){
            QImage cloned = source.clone();
            StatisticsAccumulator stat = new StatisticsAccumulator();
            stat.xMin_m = i * (2 * dw + w) + dw;
            stat.xMax_m = (i + 1) * (2 * dw + w) - dw;
            stat.yMin_m = h1;
            stat.yMax_m = h2;
            handler.onImageRegion(cloned, stat);
        }
        
    }

}
