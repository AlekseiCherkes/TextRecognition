package analysis.decomposition;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.pixels.IPixelPack;
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

    public QImage InterpriteImageData(DecomposedRegion data, StatisticsAccumulator statistics){
        int x_min = statistics.getXMin();
        int y_min = statistics.getYMin();
        int width = statistics.getXMax() - x_min + 1;
        int heigt = statistics.getYMax() - y_min + 1;

        QImage source = getSource();
        QImage.Format format = source.format();
        //QImage result = new QImage(width, heigt, format);
        //result.fill(0xaaaaaaaa);

        QImage result = source.copy();


        int sx;
        int sy;
        int se;

        for (IPixelPack pack : data.getPackAcc()){
            sx = pack.getStart();
            se = pack.getEnd  ();
            sy = pack.getY    ();

            for (; sx < se ; ++sx){
                result.setPixel(sx, sy, 0xff0000ff);
            }

        }
        return result;
/*
        int dx;
        int dy;
        int sx;
        int sy;
        int se;
        int rgb;

        for (IPixelPack pack : data.getPackAcc()){
            sx = pack.getStart();
            se = pack.getEnd  ();
            sy = pack.getY    ();
            dx = sx - x_min;
            dy = sy - y_min;

            for (; sx < se ; ++sx, ++dx){
                rgb = source.pixel(sx, sy);
                System.err.println(
                        "Pixel (" + sx + "; " + sy + ")->(" + dx + "; " + dy + ")" 
                );
                result.setPixel(dx, dy, rgb);
            }

        }
        return result;*/
    }
}

