package geom;

import java.awt.geom.Point2D;
import java.awt.*;
import java.util.List;

/**
 * @author M-NPO
 */
public class EdgeAccumulator {

    private List<Point2D> leftVertices_m;
    private List<Point2D> rightVertices_m;


    /**
     * Takes pixel of left edge and pixel of right edge
     * and updates internal edge data if necessary.
     * @param xLeft
     * @param xRight
     * @param y
     */
    public void takeLine(int xLeft, int xRight, int y){

    }

    /**
     * In Case two shapes appear to be connected,
     * connects their edges as well.
     * @param rightNeibour edge of the right hand part
     */
    public void join(EdgeAccumulator rightNeibour){

    }


    /**
     * Constructs convex polygon in compliecne with collected data.
     * @return convex polygon
     */
    public Shape buildConvexPolygon(){
        return null;
    }
}
