package analysis.data.acumulators;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 26.03.2009
 * Time: 2:45:38
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsAccumulator implements IMergible<StatisticsAccumulator>{

    public double sum_m;
    public double a01_m;
    public double a10_m;
    public double a02_m;
    public double a11_m;
    public double a20_m;

    
    public StatisticsAccumulator(){}


    //public int getKey ()        { return key_m; }
    //public void setKey(int key) { key_m  = key; }


    public void takePixel(int x, int y, float hue){
        sum_m += hue;
        a01_m += hue * x;
        a10_m += hue * y;
        a02_m += hue * x * x;
        a11_m += hue * x * y;
        a20_m += hue * y * y;
    }


    public void merge(StatisticsAccumulator stat){
        assert this != stat : "Self merging!";

        sum_m += stat.sum_m;
        a01_m += stat.a01_m;
        a10_m += stat.a10_m;
        a02_m += stat.a02_m;
        a11_m += stat.a11_m;
        a20_m += stat.a20_m;
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


    public double getAngle(){
        return Math.PI/3;
    }

    
    @Override
    public String toString() {
        return "(Mass= " + sum_m + ')';
    }
}

