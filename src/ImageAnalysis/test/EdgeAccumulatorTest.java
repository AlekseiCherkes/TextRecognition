/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 04.04.2009
 * Time: 13:10:35
 * To change this template use File | Settings | File Templates.
 */

import geom.*;

import java.awt.*;

public class EdgeAccumulatorTest {
    static private EdgeAccumulator ea = null;
    static private EdgeAccumulator ea2 = null;
    static private Polygon p = null;

    private static void test1() {
        System.out.printf("test1: ");
        ea.takeLine(0, 0, 0);
        ea.takeLine(-3, 3, 1);
        ea.takeLine(-2, 2, 2);
        ea.takeLine(-1, 1, 3);
        ea.takeLine(-3, 3, 4);

        p = (Polygon) ea.buildConvexPolygon();

        if (p.npoints == 6) {
            if (p.xpoints[0] == 0 && p.ypoints[0] == 0 &&
                    p.xpoints[1] == 3 && p.ypoints[1] == 1 &&
                    p.xpoints[2] == 3 && p.ypoints[2] == 4 &&
                    p.xpoints[3] == -3 && p.ypoints[3] == 4 &&
                    p.xpoints[4] == -3 && p.ypoints[4] == 1 &&
                    p.xpoints[5] == 0 && p.ypoints[5] == 0) {
                System.out.printf("OK\n");
                return;
            }
        }

        System.out.printf("ERROR\n");
    }

    private static void test2() {
        System.out.printf("test2: ");
        ea.takeLine(0, 0, 0);

        p = (Polygon) ea.buildConvexPolygon();

        if (p.npoints == 2) {
            if (p.xpoints[0] == 0 && p.ypoints[0] == 0 &&
                    p.xpoints[1] == 0 && p.ypoints[1] == 0) {
                System.out.printf("OK\n");
                return;
            }
        }

        System.out.printf("ERROR\n");
    }

    private static void test3() {
        System.out.printf("test3: ");

        p = (Polygon) ea.buildConvexPolygon();

        if (p.npoints == 0) {
            System.out.printf("OK\n");
            return;
        }

        System.out.printf("ERROR\n");
    }

    private static void test4() {
        System.out.printf("test4: ");

        ea.takeLine(0, 0, 0);
        ea.takeLine(-3, 3, 1);
        ea.takeLine(-2, 2, 2);
        ea.takeLine(-1, 1, 3);
        ea.takeLine(-3, 3, 4);

        ea2.takeLine(-5, -2, 1);
        ea2.takeLine(-6, -1, 6);

        ea.join(ea2);

        p = (Polygon) ea.buildConvexPolygon();

        if (p.npoints == 8) {
            if (p.xpoints[0] == 0 && p.ypoints[0] == 0 &&
                    p.xpoints[1] == 3 && p.ypoints[1] == 1 &&
                    p.xpoints[2] == 3 && p.ypoints[2] == 4 &&
                    p.xpoints[3] == -1 && p.ypoints[3] == 6 &&
                    p.xpoints[4] == -1 && p.ypoints[4] == 6 &&
                    p.xpoints[5] == -6 && p.ypoints[5] == 6 &&
                    p.xpoints[6] == -5 && p.ypoints[6] == 1 &&
                    p.xpoints[7] == 0 && p.ypoints[7] == 0) {
                System.out.printf("OK\n");
                return;
            }
        }

        System.out.printf("ERROR\n");
    }

    public static void main(String[] args) {
        ea = new EdgeAccumulator();
        test1();
        ea = new EdgeAccumulator();
        test2();
        ea = new EdgeAccumulator();
        test3();
        ea = new EdgeAccumulator();
        ea2 = new EdgeAccumulator();
        test4();
    }
}
