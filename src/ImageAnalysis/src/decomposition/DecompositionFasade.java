package decomposition;

import com.trolltech.qt.gui.QImage;
import image.QImageAdapter;
import image.IGreyImage;

/**
 * @author M-NPO
 */
public class DecompositionFasade {

    public void decompose(QImage source, IRegionCollector<QImage> handler){
        ImageDecomposer decomposer = new ImageDecomposer();
        //IGreyImage greyImage = new QImageAdapter(source);

        for (int i=0 ; i<1 ; ++i){
            QImage cloned = source.clone();
            handler.onImageRegion(cloned, new FigureStatistics());
        }
        
    }

}
