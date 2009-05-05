package analysis.data.pixels;


/**
* User: Nick
* Date: 23.03.2009
* Time: 19:15:55
*/
public class PixelPack implements IPixelPack {
    protected int xStart_m;
    protected int xSpan_m;
    protected int y_m;

    @Override
    public int getY() {
        return y_m;
    }

    @Override
    public int getStart() {
        return xStart_m;
    }

    @Override
    public int getEnd() {
        return xStart_m+xSpan_m;
    }

    @Override
    public int getSpan() {
        return xSpan_m;
    }

    public void setStart(int xStart) { xStart_m = xStart; }

    public void setSpan(int xSpan) { xSpan_m = xSpan; }

    public void setY(int y) { y_m = y; }

    @Override
    public String toString() {
        return "(" + xStart_m + "; " + y_m +"; " + (xStart_m+xSpan_m-1) + ")";
    }
}
