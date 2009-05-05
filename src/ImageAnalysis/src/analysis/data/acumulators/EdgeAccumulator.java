package analysis.data.acumulators;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author M-ACH
 */
public class EdgeAccumulator implements IMergible<EdgeAccumulator>{

    private ArrayList<Point> leftVertices_m;
    private ArrayList<Point> rightVertices_m;


    public EdgeAccumulator() {
        leftVertices_m  = new ArrayList<Point>();
        rightVertices_m = new ArrayList<Point>();

    }


    /**
     * Takes pixel of left edge and pixel of right edge
     * and updates internal edge data if necessary.
     * The y cord must be increased between calls.
     *
     */
    public void takeLine(int xLeft, int xRight, int y) {
        assert xRight >= xLeft;
//        assert leftVertices_m.size() > 0 && y > leftVertices_m.get(leftVertices_m.size() - 1).y;
//        assert rightVertices_m.size() > 0 && y > rightVertices_m.get(rightVertices_m.size() - 1).y;

        Point pl = new Point(xLeft, y);
        Point pr = new Point(xRight, y);
        leftVertices_m.add(pl);
        rightVertices_m.add(pr);
        updateConvex();
    }

    private void updateConvex() {
        // Левая область
        while (leftVertices_m.size() > 2) {
            int size = leftVertices_m.size();
            Point p3 = leftVertices_m.get(size - 1);
            Point p2 = leftVertices_m.get(size - 2);
            Point p1 = leftVertices_m.get(size - 3);

            int t = calculateTurnDirection(p1, p2, p3);
            if (t > 0) break;

            leftVertices_m.remove(size - 2);
        }

        // Правая область
        while (rightVertices_m.size() > 2) {
            int size = rightVertices_m.size();
            Point p3 = rightVertices_m.get(size - 1);
            Point p2 = rightVertices_m.get(size - 2);
            Point p1 = rightVertices_m.get(size - 3);

            int t = calculateTurnDirection(p1, p2, p3);
            if (t < 0) break;

            rightVertices_m.remove(size - 2);
        }
    }

    /**
     * Get sign of sinus of angel between (p3 - p2) and (p2 - p1) vectors.
     * if > 0 -- left direction, if < 0 -- right, if == 0 forward or backward
     */
    private int calculateTurnDirection(Point p1, Point p2, Point p3) {
        // Направление поворота определяется знаком векторного произведения.
        int ax = p3.x - p2.x;
        int ay = p3.y - p2.y;
        int bx = p2.x - p1.x;
        int by = p2.y - p1.y;

        return ax * by - bx * ay;
    }

    /**
     * In Case two shapes appear to be connected,
     * connects their edges as well.
     *
     * @param rightNeibour edge of the right hand part
     */
    public void merge(EdgeAccumulator rightNeibour) {
        ArrayList<Point> left1 = rightNeibour.leftVertices_m;
        ArrayList<Point> left2 = this.leftVertices_m;
        ArrayList<Point> right1 = rightNeibour.rightVertices_m;
        ArrayList<Point> right2 = this.rightVertices_m;

        if (left1.size() == 0 && right1.size() == 0) {
            return;
        }

        if (left2.size() == 0 && right2.size() == 0) {
            this.leftVertices_m = rightNeibour.leftVertices_m;
            this.rightVertices_m = rightNeibour.rightVertices_m;
        }

        EdgeAccumulator ea = new EdgeAccumulator();

        assert left1.size() > 0; // TODO: ...

        int il1 = 0;
        int il2 = 0;
        int ir1 = 0;
        int ir2 = 0;
        while (true) {
            int yl1 = il1 < left1.size() ? left1.get(il1).y : Integer.MAX_VALUE;
            int yl2 = il2 < left2.size() ? left2.get(il2).y : Integer.MAX_VALUE;
            int yr1 = ir1 < right1.size() ? right1.get(ir1).y : Integer.MAX_VALUE;
            int yr2 = ir2 < right2.size() ? right2.get(ir2).y : Integer.MAX_VALUE;

            if (yl1 == Integer.MAX_VALUE && yl2 == Integer.MAX_VALUE &&
                    yr1 == Integer.MAX_VALUE && yr2 == Integer.MAX_VALUE) {
                break;
            } else if (yl1 <= yl2 && yl1 <= yr1 && yl1 <= yr2) {
                int x = left1.get(il1).x;
                ea.takeLine(x, x, yl1);
                il1++;
            } else if (yl2 <= yl1 && yl2 <= yr1 && yl2 <= yr2) {
                int x = left2.get(il2).x;
                ea.takeLine(x, x, yl2);
                il2++;
            } else if (yr1 <= yl1 && yr1 <= yl2 && yr1 <= yr2) {
                int x = right1.get(ir1).x;
                ea.takeLine(x, x, yr1);
                ir1++;
            } else if (yr2 <= yl1 && yr2 <= yl2 && yr2 <= yr1) {
                int x = right2.get(ir2).x;
                ea.takeLine(x, x, yr2);
                ir2++;
            }
        }

        this.leftVertices_m = ea.leftVertices_m;
        this.rightVertices_m = ea.rightVertices_m;
    }


    /**
     * Constructs convex polygon in compliance with collected data.
     * Clock-wise order.
     *
     * @return convex polygon
     */
    public Shape buildConvexPolygon() {
        Polygon ret = new Polygon();

        for (int i = 0; i < rightVertices_m.size(); ++i) {
            Point p = rightVertices_m.get(i);
            ret.addPoint(p.x, p.y);
        }

        for (int i = leftVertices_m.size() - 1; i >= 0; --i) {
            Point p = leftVertices_m.get(i);
            ret.addPoint(p.x, p.y);
        }

        return ret;
    }

}
