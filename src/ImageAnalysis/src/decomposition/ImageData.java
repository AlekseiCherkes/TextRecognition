package decomposition;

import decomposition.PixelPack;

import java.util.List;
import java.util.ArrayList;

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

}
