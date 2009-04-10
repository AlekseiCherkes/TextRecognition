package analysis.image;

/**
 * @author M-NPO
 */
public class GreyRegionView implements IGreyImage {
    private IGreyImage source_m;

    private int width_m;
    private int height_m;
    
    private int xMin_m;
    private int yMin_m;


    public GreyRegionView(IGreyImage source){
        source_m = source;
    }



    public int getYMin() {
        return yMin_m;
    }
    public void setYMin(int yMin) {
        yMin_m = yMin;
    }

    public int getXMin() {
        return xMin_m;
    }
    public void setXMin(int xMin) {
        xMin_m = xMin;
    }




    public int getWidth() {
        return width_m;
    }
    public void setWidth(int width) {
        width_m = width;
    }


    public int getHeight() {
        return height_m;
    }
    public void setHeight(int height) {
        height_m = height;
    }




    public float get(int x, int y) {
        return source_m.get(x+xMin_m, y+xMin_m);
    }

    public void set(int x, int y, float hue) {
        source_m.set(x+xMin_m, y+xMin_m, hue);
    }

    public boolean isForeground(float hue) {
        return source_m.isForeground(hue);
    }
}
