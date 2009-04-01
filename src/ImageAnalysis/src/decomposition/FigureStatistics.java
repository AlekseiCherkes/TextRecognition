package decomposition;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 26.03.2009
 * Time: 2:45:38
 * To change this template use File | Settings | File Templates.
 */
public class FigureStatistics {

    protected double sum_m;
    protected double a01_m;
    protected double a10_m;
    protected double a02_m;
    protected double a11_m;
    protected double a20_m;

    protected int xMin_m;
    protected int xMax_m;
    protected int yMin_m;
    protected int yMax_m;


    public FigureStatistics(){
        xMax_m = yMax_m = Integer.MAX_VALUE;
    }


    //public int getKey ()        { return key_m; }
    //public void setKey(int key) { key_m  = key; }


    public void takePixel(int x, int y, float hue){
        sum_m += hue;
        a01_m += hue * x;
        a10_m += hue * y;
        a02_m += hue * x * x;
        a11_m += hue * x * y;
        a20_m += hue * y * y;

        xMin_m = Math.min(xMin_m, x);
        xMax_m = Math.max(xMax_m, x);
        yMin_m = Math.min(yMin_m, y);
        yMax_m = Math.max(yMax_m, y);

    }


    public void merge(FigureStatistics stat){
        assert this != stat : "Self merging!";

        sum_m += stat.sum_m;
        a01_m += stat.a01_m;
        a10_m += stat.a10_m;
        a02_m += stat.a02_m;
        a11_m += stat.a11_m;
        a20_m += stat.a20_m;

        xMin_m = Math.min(xMin_m, stat.xMin_m);
        xMax_m = Math.max(xMax_m, stat.xMax_m);
        yMin_m = Math.min(yMin_m, stat.yMin_m);
        yMax_m = Math.max(yMax_m, stat.yMax_m);
    }


    public int getYMax() {
        return yMax_m;
    }

    public int getYMin() {
        return yMin_m;
    }

    public int getXMax() {
        return xMax_m;
    }

    public int getXMin() {
        return xMin_m;
    }

    public double getN(){
        return sum_m;
    }

    public double getMx(){
        return a01_m / sum_m;
    }

    public double getMy(){
        return a10_m / sum_m;
    }

    public double getDx(double mx){
        return a02_m / sum_m - mx*mx;
    }

    public double getDy(double my){
        return a20_m / sum_m - my*my;
    }

    public double getKxy(double mx, double my){
        return a11_m / sum_m - mx*my;
    }


}

