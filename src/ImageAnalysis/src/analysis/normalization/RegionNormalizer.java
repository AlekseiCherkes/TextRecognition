package analysis.normalization;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.acumulators.EdgeAccumulator;
import analysis.data.ad_hoc.RectBoundsOfFloat;
import analysis.data.ad_hoc.RectBoundsOfInt;
import analysis.data.pixels.IPixelPack;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QMatrix;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * User: Nick
 * Date: 19.04.2009
 * Time: 16:23:27
 */
public class RegionNormalizer {

//    private static final int NORTH = 0;
//    private static final int WEST  = 1;
//    private static final int SOUTH = 2;
//    private static final int EAST  = 3;

    private TransformationProvider transformationProvider_m;

    public RegionNormalizer() {
        transformationProvider_m = new TransformationProvider();
    }

    public TransformationProvider getTransformationProvider() {
        return transformationProvider_m;
    }

    public void setTransformationProvider(TransformationProvider transformationProvider) {
        transformationProvider_m = transformationProvider;
    }


    private AffineTransform getTransformForRegion(DecomposedRegion region){
        return transformationProvider_m.provideTransform(region);
    }




    public QImage normalize(QImage srcImg, DecomposedRegion region){
        AffineTransform awtTransform = getTransformForRegion(region);

        EdgeAccumulator edge = region.getEdgeAcc();
        Shape           hull = edge  .buildConvexPolygon();
        PathIterator    iter = hull  .getPathIterator(awtTransform);

        RectBoundsOfFloat pathBounds = estimatePathBounds(iter);
        RectBoundsOfInt   dstBounds  = pathBounds.toIntBounds();
        RectBoundsOfInt   regBounds  = region.getBox();


        RectBoundsOfInt   srcBounds  = regBounds.clone();
        srcBounds.merge(dstBounds);

        QImage tmpImg = copyRegion(srcImg, srcBounds, region.getPackAcc());


        QMatrix qtTransform = TransformationProvider.toQtTransform(awtTransform);
        tmpImg = tmpImg.transformed(qtTransform, Qt.TransformationMode.SmoothTransformation);

        return tmpImg.copy(
                dstBounds.getWest  (),
                dstBounds.getNorth (),
                dstBounds.getWidth (),
                dstBounds.getHeight()
        );
    }


    private QImage copyRegion(QImage srcImg, RectBoundsOfInt srcBounds, Iterable<IPixelPack> packs){

        QImage tmpImg = new QImage(srcBounds.getWidth(), srcBounds.getHeight(), srcImg.format());
        tmpImg.fill(0xffffffff);

        int x0 = srcBounds.getWest();
        int y0 = srcBounds.getNorth();

        copyPixels(srcImg, tmpImg, packs, x0, y0);

        return tmpImg;
    }


    private void copyPixels(QImage srcImg, QImage dstImg, Iterable<IPixelPack> packs, int x0, int y0){
        int dx;
        int dy;
        int sx;
        int sy;
        int se;
        int rgb;

        for (IPixelPack pack : packs){
            sx = pack.getStart();
            se = pack.getEnd  ();
            sy = pack.getY    ();
            dx = sx - x0;
            dy = sy - y0;

            for (; sx < se ; ++sx, ++dx){
                rgb = srcImg.pixel(sx, sy);
               /* System.err.println(
                        "Pixel (" + sx + "; " + sy + ")->(" + dx + "; " + dy + ")"
                );*/
                dstImg.setPixel(dx, dy, rgb);
            }
        }
    }


    private RectBoundsOfFloat estimatePathBounds(PathIterator pathIter){
        float[] chunk = new float[6];
        RectBoundsOfFloat bounds = new RectBoundsOfFloat();

        for (; !pathIter.isDone(); pathIter.next()){
            pathIter.currentSegment(chunk);

            bounds.merge(chunk[0], chunk[1]);
        }

        return bounds;
    }



}
