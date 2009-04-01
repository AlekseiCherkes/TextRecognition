package decomposition;

import com.trolltech.qt.gui.QImage;
import image.IGreyImage;
import image.QImageAdapter;

/**
 * @author M-NPO
 */
public class TrueDecomposition extends DecompositionFasade {

    private ImageDataInterpriter interpriter_m;

    public TrueDecomposition() {
        interpriter_m = new ImageDataInterpriter();
    }

    
    @Override
    public void decompose(QImage source, final IRegionCollector<QImage> handler) {
        ImageDecomposer decomposer       = new ImageDecomposer();
        IGreyImage greyImage             = new QImageAdapter(source);


        interpriter_m.setSource(source);

        decomposer.decompose(greyImage,
                new IRegionCollector<ImageData>(){
                    @Override
                    public void onImageRegion(ImageData region, FigureStatistics statistics) {
                        QImage img = interpriter_m.InterpriteImageData(region, statistics);
                        handler.onImageRegion(img, statistics);

                    }
                }
        );
    }
}
