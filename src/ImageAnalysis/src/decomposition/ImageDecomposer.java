package decomposition;

import image.IGreyImage;
import decomposition.ConnectionFinder;
import decomposition.ResultsManager;
import decomposition.FigureStatistics;
import decomposition.PixelPack;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 21.02.2009
 * Time: 20:52:51
 * To change this template use File | Settings | File Templates.
 */
public class ImageDecomposer {

    private ConnectionFinder connector_m;
    private ConnectionFinder.IConnectionListener handler_m;
    private ResultsManager registry_m;
    private FigureStatistics unregistered_m;


    private IRegionCollector<ImageData> collector_m;

    private IGreyImage img_m;
    private int x_m;
    private int y_m;
    private int h_m;
    private int w_m;



    public ImageDecomposer(){
        registry_m  = new ResultsManager();

        handler_m =  new ConnectionFinder.IConnectionListener(){
            @Override
            public void onRegionFinished(int regionKey){
                FigureStatistics stat = registry_m.removeStatistics(regionKey);
                ImageData        data = registry_m.removeData(regionKey);
                data.finish();
                collector_m.onImageRegion(data, stat);
            }

            @Override
            public void noConnectionFound(PixelPack newPack) {
                int key = registry_m.registerRegion(unregistered_m, newPack);
                newPack.key = key;
            }

            @Override
            public void onFirstConnectionFound(PixelPack newPack, int oldKey) {
                registry_m.appendRegionStatistics(unregistered_m, newPack, oldKey);
                newPack.key = oldKey;
            }

            @Override
            public void onNextConnectionFound(PixelPack newPack, int oldKey) {
                int key = registry_m.mergeRegions(newPack.key, oldKey);
                newPack.key = key;
            }
        };

        connector_m = new ConnectionFinder(handler_m);
    }


    public void decompose(IGreyImage img, IRegionCollector<ImageData> handler){
        img_m = img;
        collector_m = handler;
        scanImage();
    }


    private void scanImage(){
        h_m = img_m.getHeight();
        w_m = img_m.getWidth();
        x_m = 0;
        y_m = 0;

        while (y_m < h_m){

            processBackGround();

            if (x_m < w_m){
                processForeground(); // beware!, it increases x_m
            }

            if (x_m >= w_m){
                switchLine();
            }
        }
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
        PixelPack pack = new PixelPack();
        pack.x_min = x_m;
        pack.y     = y_m;

        unregistered_m = new FigureStatistics();
        float hue;

        while (x_m < w_m){
            hue = img_m.get(x_m, y_m);
            if (!img_m.isForeground(hue)) break;

            unregistered_m.takePixel(x_m, y_m, hue);
            ++x_m;
        }

        pack.x_max = x_m - 1;
        connector_m.find4Connections(pack);
        unregistered_m = null;
    }


}
