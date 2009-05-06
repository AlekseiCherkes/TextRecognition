package analysis.decomposition;

import analysis.data.ad_hoc.RectBoundsOfInt;
import com.trolltech.qt.gui.QImage;

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
            RectBoundsOfInt box = new RectBoundsOfInt();


            box.setWest(i * (2 * dw + w) + dw);
            box.setNorth(h1);
            box.setSouth(h2);
            box.setEast((i + 1) * (2 * dw + w) - dw);

            handler.onImageRegion(null, cloned);
        }
        
    }

}
