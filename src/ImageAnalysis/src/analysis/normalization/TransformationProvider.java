package analysis.normalization;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.acumulators.StatisticsAccumulator;
import analysis.data.ad_hoc.RectBoundsOfInt;
import com.trolltech.qt.gui.QMatrix;

import java.awt.geom.AffineTransform;

/**
 * User: Nick
 * Date: 04.05.2009
 * Time: 15:28:50
 */
public class TransformationProvider {

    private class AxisesInfo {
        public double mainAxis_dy;
        public double mainAxis_dx;
        public double mainAxis_len;
        public double secondaryAxis_len;
    }


    public AffineTransform provideTransform(DecomposedRegion region){
        StatisticsAccumulator stat = region.getStatAcc();
        double Mx  = stat.getMx();
        double My  = stat.getMy();
        double Dx  = stat.getDx(Mx);
        double Dy  = stat.getDy(My);
        double Kxy = stat.getKxy(Mx, My);

        AxisesInfo axises =  determineMainAxises(Dx, Dy, Kxy);

        RectBoundsOfInt box = region.getBox();
        double anchorX = 0.5 * box.getWidth ();
        double anchorY = 0.5 * box.getHeight();

        AffineTransform transform = new AffineTransform();
        transform.setToRotation(
                axises.mainAxis_dx,
                axises.mainAxis_dy,
                anchorX,
                anchorY
        );

        return transform;
    }


    private AxisesInfo determineMainAxises(double Dx, double Dy, double Kxy){
        AxisesInfo axises = new AxisesInfo();

        if (Kxy == 0){

            if (Dx > Dy){
                axises.mainAxis_dx       = 0.1;
                axises.mainAxis_dy       = 0.0;
                axises.mainAxis_len      = 4 * Math.sqrt(Dx);
                axises.secondaryAxis_len = 4 * Math.sqrt(Dy);

            } else {
                axises.mainAxis_dx       = 0.0;
                axises.mainAxis_dy       = 0.1;
                axises.mainAxis_len      = 4 * Math.sqrt(Dy);
                axises.secondaryAxis_len = 4 * Math.sqrt(Dx);
            }

        } else {
            double dd = Dx - Dy;
            double discriminant = Math.sqrt(dd*dd + 4 * Kxy*Kxy);

            if (Dx > Dy){
                axises.mainAxis_dy       = Dy - Dx + discriminant;  //TODO: may be " ... = Dy - Dx - discriminant;"
                axises.mainAxis_dx       = 2 * Kxy;

            } else {
                axises.mainAxis_dy       = Dy - Dx - discriminant;  //TODO: may be " ... = Dy - Dx + discriminant;"
                axises.mainAxis_dx       = 2 * Kxy;
            }

        }


        return axises;
    }


    public static QMatrix toQtTransform(AffineTransform t){
        QMatrix matr = new QMatrix(
                t.getScaleX(),
                t.getShearX(),
                t.getShearY(),
                t.getScaleY(),
                t.getTranslateX(),
                t.getTranslateY()
        );
        return matr;
    }


    public static AffineTransform toAwtTransform(QMatrix m){
        AffineTransform tramsform = new AffineTransform(
                m.m11(),
                m.m21(),
                m.m12(),
                m.m22(),
                m.dx (),
                m.dy ()
        );
        return tramsform;
    }
}
