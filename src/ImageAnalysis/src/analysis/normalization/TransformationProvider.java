package analysis.normalization;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.acumulators.StatisticsAccumulator;
import com.trolltech.qt.gui.QMatrix;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * User: Nick
 * Date: 04.05.2009
 * Time: 15:28:50
 */
public class TransformationProvider {


    public AxisesInfo provideAxisesInfo(DecomposedRegion region){
        StatisticsAccumulator stat = region.getStatAcc();
        double Mx  = stat.getMx();
        double My  = stat.getMy();
        double Dx  = stat.getDx(Mx);
        double Dy  = stat.getDy(My);
        double Kxy = stat.getKxy(Mx, My);

        return  determineMainAxises(Dx, Dy, Kxy);
    }


    public AffineTransform provideAwtTransform(AxisesInfo axises, Point2D anchor){
        AffineTransform transform = new AffineTransform();
        transform.setToRotation(
                axises.mainAxis_dx,
                axises.mainAxis_dy,
                anchor.getX(),
                anchor.getY()
        );

        return transform;
    }


    public QMatrix provideQtTransform(AxisesInfo axises, Point2D anchor){
        AffineTransform transform = new AffineTransform();
        transform.setToRotation(
                axises.mainAxis_dx,
                axises.mainAxis_dy,
                anchor.getX(),
                anchor.getY()
        );

        QMatrix qtransform = toQtTransform(transform);
        return qtransform;
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
                axises.mainAxis_dy       = Dy - Dx + discriminant;  //TODO: may be " ... = Dy - Dx + discriminant;"
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
