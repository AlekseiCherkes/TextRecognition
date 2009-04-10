package analysis.decomposition;

import com.trolltech.qt.gui.QImage;
import analysis.image.IGreyImage;
import analysis.image.QImageAdapter;
import analysis.decomposition.DecompositionFasade;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.acumulators.DecomposedRegion;

/**
 * @author M-NPO
 */
public class TrueDecomposition extends DecompositionFasade {

    private RegionInterpriter interpriter_m;

    public TrueDecomposition() {
        interpriter_m = new RegionInterpriter();
    }

    
    @Override
    public void decompose(QImage source, final IRegionCollector<QImage> handler) {
        DecompositionContext decomposer       = new DecompositionContext();
        IGreyImage greyImage             = new QImageAdapter(source);


        interpriter_m.setSource(source);

        decomposer.decompose(greyImage,
                new IRegionCollector<DecomposedRegion>(){
                    @Override
                    public void onImageRegion(DecomposedRegion region, StatisticsAccumulator statistics) {
                        QImage img = interpriter_m.InterpriteImageData(region, statistics);
                        handler.onImageRegion(img, statistics);
                    }
                }
        );
    }
}
