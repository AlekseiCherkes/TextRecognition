package binary;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 23.03.2009
 * Time: 19:09:03
 * To change this template use File | Settings | File Templates.
 */
public class PixelPackFetcher {

    private IBinaryStream       stream_m;

    private PacksRegistry registry_m;


    public PixelPackFetcher() {

    }


    public IBinaryStream getStream() {
        return stream_m;
    }
    public void setStream(IBinaryStream stream) {
        stream_m = stream;
    }


    public boolean is(float hue){
        return hue > 0;
    }


    public PixelPack getNextPack(){
        return null;
    }



}
