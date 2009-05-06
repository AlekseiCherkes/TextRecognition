package analysis.normalization;

import analysis.data.acumulators.*;
import analysis.data.ad_hoc.RectBoundsOfFloat;
import analysis.data.ad_hoc.RectBoundsOfInt;
import analysis.data.pixels.IPixelPack;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import java.awt.*;
import java.awt.geom.*;

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


    private Point2D determineCenter(DecomposedRegion region){
        StatisticsAccumulator stat = region.getStatAcc();
        return new Point2D.Double(stat.getMx(), stat.getMy());
    }



    public QImage normalize(QImage srcImg, DecomposedRegion region){
        AxisesInfo axises = transformationProvider_m.provideAxisesInfo(region);
        Point2D    center = determineCenter(region);

        AffineTransform draftTransform = transformationProvider_m.provideAwtTransform(axises, center);
        RectBoundsOfFloat pathBounds   = estimateTransformedBounds(region, draftTransform);
        RectBoundsOfInt   dstBounds    = pathBounds.toIntBounds();
        dstBounds.expandAroundNewCenter((float)center.getX(), (float)center.getY());

        RectBoundsOfInt   regBounds = region.getBox();
        RectBoundsOfInt   srcBounds = regBounds.clone();

        srcBounds.merge(dstBounds);

        QImage tmpImg = copyRegion(srcImg, srcBounds, region.getPackAcc());
        //return tmpImg;

        Point2D newCenter = new Point2D.Double(
                center.getX() - srcBounds.getWest(),
                center.getY() - srcBounds.getNorth()
        );
        QMatrix qtTransform = transformationProvider_m.provideQtTransform(axises, newCenter);

        
        tmpImg = tmpImg.transformed(qtTransform, Qt.TransformationMode.SmoothTransformation);
        copyDebug(tmpImg, region.getPackAcc(), srcBounds.getWest(), srcBounds.getNorth());

        QPainter painter = new QPainter(tmpImg);
        painter.setPen(QColor.darkGreen);
        painter.drawRect(
                dstBounds.getWest() - srcBounds.getWest(),
                dstBounds.getNorth() - srcBounds.getNorth(),
                dstBounds.getWidth(),
                dstBounds.getHeight()
        );

        painter.setPen(QColor.cyan);
        painter.drawRect(
                regBounds.getWest() - srcBounds.getWest(),
                regBounds.getNorth() - srcBounds.getNorth(),
                regBounds.getWidth(),
                regBounds.getHeight()
        );

        painter.setPen(QColor.black);
        painter.drawRect(
                0,
                0,
                srcBounds.getWidth(),
                srcBounds.getHeight()
        );

        painter.setPen(QColor.darkBlue);
        painter.drawRect((int)newCenter.getX(), (int)newCenter.getY(),2,2);
        
        painter.end();

        return tmpImg;

//        return tmpImg.copy(
//                dstBounds.getWest  (),
//                dstBounds.getNorth (),
//                dstBounds.getWidth (),
//                dstBounds.getHeight()
//        );
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


     private void copyDebug(QImage dstImg, Iterable<IPixelPack> packs, int x0, int y0){
        int dx;
        int dy;
        int sx;
        int sy;
        int se;
        int rgb = 0xdfff0000;

        for (IPixelPack pack : packs){
            sx = pack.getStart();
            se = pack.getEnd  ();
            sy = pack.getY    ();
            dx = sx - x0;
            dy = sy - y0;

            for (; sx < se ; ++sx, ++dx){
                dstImg.setPixel(dx, dy, rgb);
            }
        }
    }


    private RectBoundsOfFloat estimateTransformedBounds(DecomposedRegion region, AffineTransform transform){
        EdgeAccumulator edge = region.getEdgeAcc();
        Shape           hull = edge  .buildConvexPolygon();
        PathIterator    iter = hull  .getPathIterator(transform);

        return estimatePathBounds(iter);
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
