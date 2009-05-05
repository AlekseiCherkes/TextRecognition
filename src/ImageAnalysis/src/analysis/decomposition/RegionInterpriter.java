package analysis.decomposition;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.pixels.IPixelPack;
import analysis.data.ad_hoc.RectBoundsOfInt;
import com.trolltech.qt.gui.QImage;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 31.03.2009
 * Time: 16:31:48
 * To change this template use File | Settings | File Templates.
 */
public class RegionInterpriter {


    private QImage source_m;


    public RegionInterpriter(){}


    public QImage getSource() {
        return source_m;
    }
    public void setSource(QImage source) {
        source_m= source;
    }

    public QImage InterpriteImageData(DecomposedRegion region, StatisticsAccumulator statistics){
        RectBoundsOfInt box = region.getBox();

        int x0 = box.getWest ();
        int y0 = box.getNorth();


        QImage source = getSource();
        QImage.Format format = source.format();
        QImage result = new QImage(box.getWidth(), box.getHeight(), format);
        result.fill(0xffffffff);



        int dx;
        int dy;
        int sx;
        int sy;
        int se;
        int rgb;

        for (IPixelPack pack : region.getPackAcc()){
            sx = pack.getStart();
            se = pack.getEnd  ();
            sy = pack.getY    ();
            dx = sx - x0;
            dy = sy - y0;

            for (; sx < se ; ++sx, ++dx){
                rgb = source.pixel(sx, sy);
                result.setPixel(dx, dy, rgb);
            }

        }
        return result;
    }
}

