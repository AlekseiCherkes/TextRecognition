package analysis.decomposition;

import analysis.image.IGreyImage;
import analysis.data.acumulators.DecomposedRegion;
import analysis.data.pixels.ForegroundData;



/**
 * Created by IntelliJ IDEA.
 * Author: M-NPO
 * Date: 21.02.2009
 * Time: 20:52:51
 * To change this template use File | Settings | File Templates.
 */
public class DecompositionContext {

    private ConnectionFinder connector_m;
    private RegionsKeeper regions_m;

    private IRegionCollector<DecomposedRegion> collector_m;

    private IGreyImage img_m;
    private int x_m;
    private int y_m;
    private int w_m;



    public DecompositionContext(){
        regions_m  = new RegionsKeeper();

        ConnectionFinder.IConnectionListener handler = new ConnectionFinder.IConnectionListener() {
            @Override
            public void onRegionFinished(int regionKey) {
                DecomposedRegion region = regions_m.removeRegion(regionKey);
                collector_m.onImageRegion(region, region.getStatAcc());
            }

            @Override
            public void noConnectionFound(ForegroundData context) {
                assert context.getKey() == 0 : "Foreground is already registered yet!";
                int key = regions_m.registerNewRegion(context);
                context.setKey(key);
            }

            @Override
            public void onFirstConnectionFound(ForegroundData context, int oldKey) {
                assert context.getKey() == 0 : "Foreground is already registered yet!";
                regions_m.appendOldRegion(context, oldKey);
                context.setKey(oldKey);
            }

            @Override
            public void onNextConnectionFound(ForegroundData context, int oldKey) {
                assert context.getKey() != 0 : "Foreground is unregistered yet!";
                regions_m.megreOldRegions(context.getKey(), oldKey);
            }
        };

        connector_m = new ConnectionFinder(handler);
    }


    public void decompose(IGreyImage img, IRegionCollector<DecomposedRegion> handler){
        assert img     != null: "Source image is null!";
        assert handler != null: "Collector is null!";
        img_m = img;
        collector_m = handler;
        scanImage();
    }


    private void scanImage(){
        int h = img_m.getHeight();
        w_m = img_m.getWidth();
        x_m = 0;
        y_m = 0;

        while (y_m < h){
            processBackGround();
            if (x_m <  w_m) processForeground(); // beware!, it increases x_m
            if (x_m >= w_m) switchLine();
        }

        connector_m.finish();
    }


    private void switchLine(){
        x_m = 0;
        ++y_m;
        connector_m.switchToNextLine();
    }


    private void processBackGround(){
        while (x_m < w_m && !img_m.isForeground(img_m.get(x_m, y_m))) ++x_m;
    }


    private void processForeground(){
        ForegroundData foreground = new ForegroundData();
        foreground.setY(y_m);
        foreground.setStart(x_m);

        float hue;

        while (x_m < w_m){
            hue = img_m.get(x_m, y_m);
            if (!img_m.isForeground(hue)) break;

            foreground.takePixel(x_m, y_m, hue);
            ++x_m;
        }

        foreground.setSpan(x_m - foreground.getStart());
        connector_m.findConnections(foreground);
    }


}
