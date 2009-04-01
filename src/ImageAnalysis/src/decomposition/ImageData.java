package decomposition;

import decomposition.PixelPack;

import java.util.List;
import java.util.ArrayList;

import image.IGrayImage;
import image.IGreyImage;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 30.03.2009
 * Time: 22:03:37
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {
    private List<List<PixelPack>> bundles_m;
    private List<PixelPack> currentBundle_m;

    public ImageData(){
        bundles_m       = new ArrayList<List<PixelPack>>();
        currentBundle_m = new ArrayList<PixelPack>();
    }

    public void append(PixelPack pack){
        currentBundle_m.add(pack);
    }

    public void merge(ImageData data){
        bundles_m.add(currentBundle_m);
        data.finish();
        bundles_m.addAll(data.bundles_m);
        currentBundle_m = new ArrayList<PixelPack>();
    }

    public void finish(){
        if (!currentBundle_m.isEmpty()){
            bundles_m.add(currentBundle_m);
        }
    }


    public void copyPixels(IGreyImage src, IGreyImage dst){
        int x;
        int y;
        float hue;

        for (List<PixelPack> bundle : bundles_m){
            for (PixelPack pack : bundle){
                y = pack.y;

                for (x=pack.x_min ; x <= pack.x_max ; ++x){
                    hue = src.get(x,y);
                    dst.set(x, y, hue);
                }
            }
        }
    }

}
