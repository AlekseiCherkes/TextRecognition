package decomposition;

import decomposition.PixelPack;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
* User: Nick
* Date: 23.03.2009
* Time: 19:31:08
* To change this template use File | Settings | File Templates.
*/
public class ConnectionFinder {
    private ArrayList<PixelPack> connectable_m;
    private ArrayList<PixelPack> unready_m;
    private int cursor_m;

    private PixelPack newPack_m;
    private IConnectionListener listener_m;


    private Map<Integer, PixelPack> newFinishLimits_m;
    private Map<Integer, PixelPack> oldFinishLimits_m;



    public ConnectionFinder(IConnectionListener listener){
        listener_m    = listener;
        connectable_m = new ArrayList<PixelPack>();
        unready_m     = new ArrayList<PixelPack>();
        newFinishLimits_m = new HashMap<Integer, PixelPack>();
        oldFinishLimits_m = new HashMap<Integer, PixelPack>();
    }



    public void switchToNextLine(){
        ArrayList<PixelPack> swapList;
        connectable_m.clear();
        swapList      = connectable_m;
        connectable_m = unready_m;
        unready_m     = swapList;
        

        Map<Integer, PixelPack> swapMap;
        oldFinishLimits_m.clear();
        swapMap           = oldFinishLimits_m;
        oldFinishLimits_m = newFinishLimits_m;
        newFinishLimits_m = swapMap;


        cursor_m = 0;
        newPack_m = null;  // JIC
    }

    public void find4Connections(PixelPack newPack){
        newPack_m = newPack;
        findConnections(newPack.x_min, newPack.x_max);
        incorporatePack(newPack);
    }

    public void find8Connections(PixelPack newPack){
        newPack_m = newPack;
        findConnections(newPack.x_min-1, newPack.x_max+1);
        incorporatePack(newPack);
    }





    private void incorporatePack(PixelPack newPack){

        unready_m.add(newPack);
        newFinishLimits_m.put(newPack.key, newPack);
    }

    private void findConnections(int start, int finish){
        int lim = connectable_m.size();
        PixelPack cnn = null;

        while (cursor_m < lim){
           cnn = connectable_m.get(cursor_m);
           if (cnn.x_max >= start) break;

           tryToFinish(cnn.key, start);
           ++cursor_m;
        }


        if (cursor_m == lim || cnn.x_min > finish){
            fireNoConnectionsFound();
            return;
        }

        fireFirstConnectionFound(cnn);

        while (++cursor_m < lim){
            cnn = connectable_m.get(cursor_m);
            if (cnn.x_min > finish) return;

            fireNextConnectionFound(cnn);
        }
    }





    private void fireRegionFinished(int oldKey){
        listener_m.onRegionFinished(oldKey);
    }

    private void fireNoConnectionsFound(){
        listener_m.noConnectionFound(newPack_m);
    }

    private void fireFirstConnectionFound(PixelPack oldPack){
        listener_m.onFirstConnectionFound(newPack_m, oldPack.key);
        dismissFinishing(oldPack.key);
    }

    private void fireNextConnectionFound(PixelPack oldPack){
        int oldKey = oldPack.key;
        if (newPack_m.key == oldKey) return;

        listener_m.onNextConnectionFound(newPack_m, oldKey);
        dismissFinishing(oldKey);
        updateMergedKeys(oldKey, newPack_m.key);
    }




    private void updateMergedKeys(int oldKey, int newKey){
        int lim = connectable_m.size();
        PixelPack cnn;

        for (int i=cursor_m ; i < lim ; ++i){
            cnn = connectable_m.get(i);
            if (cnn.key == oldKey){
                cnn.key = newKey;
            }
        }
    }




    private void dismissFinishing(int oldKey){
        oldFinishLimits_m.remove(oldKey);
    }

    private void tryToFinish(int oldKey, int newPackStart){
        PixelPack lastPack = oldFinishLimits_m.get(oldKey);
        if (lastPack != null && lastPack.x_max < newPackStart){
            fireRegionFinished(oldKey);
        }
    }





    public interface IConnectionListener {

        /**
         * Signals that no connetion will be found
         * to existing region. Which means that all its'
         * pixels are collected.
         * @param regionKey is the key of the finished region.
         */
        void onRegionFinished(int regionKey);

        /**
         * Signals that no connection to a PixelPack
         * from previous row have been found.
         * @param newPack the pack of the current row,
         * that might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         */
        void noConnectionFound(PixelPack newPack);

        /**
         * Signals that connection to a PixelPack
         * from previous row have been found for the first time.
         * @param newPack is the pack of the current row,
         * which might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         * @param regionKey is the key of the region,
         * which has proved to be connected to the newPack.
         */
        void onFirstConnectionFound(PixelPack newPack, int regionKey);


        /**
         * Signals that connection to another PixelPack
         * from previous row have been found for the next time.
         * @param newPack is the pack of the current row,
         * which might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         * @param regionKey is the key of the region,
         * which has proved to be connected to the newPack.
         */
        void onNextConnectionFound(PixelPack newPack, int regionKey);


    }


}
