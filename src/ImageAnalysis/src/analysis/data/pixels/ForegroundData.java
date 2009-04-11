package analysis.data.pixels;

import analysis.data.pixels.IRowSpan;
import analysis.data.pixels.PixelPack;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.pixels.RowSpan;

/**
 * @author M-NPO
 */
public class ForegroundData extends PixelPack {

    private int key_m;

    private StatisticsAccumulator statistics_m;

    public ForegroundData() {
        statistics_m = new StatisticsAccumulator();
    }

    public int getKey()         { return key_m;}
    public void setKey(int key) { key_m = key; }


    public void takePixel(int x, int y, float hue){
        statistics_m.takePixel(x, y, hue);
    }

    public IRowSpan getRowSpan(){
        return new RowSpan(xStart_m, xSpan_m);
    }

    public StatisticsAccumulator getStatistics(){
        return statistics_m;
    }
}
