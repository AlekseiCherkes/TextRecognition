package analysis.decomposition;

import analysis.data.pixels.IRowSpan;
import analysis.data.pixels.ForegroundData;
import analysis.data.ad_hoc.ArrayOfInt;

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

    private ArrayList<IRowSpan>  oldSpans_m;
    private ArrayOfInt           oldKeys_m;
    private Map<Integer,Integer> oldFinishLimits_m;


    private ArrayList<IRowSpan>  newSpans_m;
    private ArrayOfInt           newKeys_m;
    private Map<Integer,Integer> newFinishLimits_m;


    private int cursor_m;
    private ForegroundData context_m;
    private IConnectionListener listener_m;
    private boolean are8connections_m;







    public ConnectionFinder(IConnectionListener listener){
        listener_m        = listener;
        oldSpans_m        = new ArrayList<IRowSpan>();
        newSpans_m        = new ArrayList<IRowSpan>();
        newFinishLimits_m = new HashMap<Integer, Integer>();
        oldFinishLimits_m = new HashMap<Integer, Integer>();
    }



    public void switchToNextLine(){
        ArrayList<IRowSpan> swapList;
        oldSpans_m.clear();
        swapList   = oldSpans_m;
        oldSpans_m = newSpans_m;
        newSpans_m = swapList;


        ArrayOfInt swapArray;
        oldKeys_m.clear();
        swapArray = oldKeys_m;
        oldKeys_m = newKeys_m;
        newKeys_m = swapArray;


        Map<Integer, Integer> swapMap;
        oldFinishLimits_m.clear();
        swapMap           = oldFinishLimits_m;
        oldFinishLimits_m = newFinishLimits_m;
        newFinishLimits_m = swapMap;


        cursor_m  = 0;
        context_m = null;  // JIC
    }

    public void find4Connections(ForegroundData foreground){
        context_m = foreground;
        findConnections(foreground.getStart(), foreground.getEnd()-1);
        incorporatePack(foreground);
    }

    public void find8Connections(ForegroundData foreground){
        context_m = foreground;
        findConnections(foreground.getStart()-1, foreground.getEnd());
        incorporatePack(foreground);
    }

    public void findConnections(ForegroundData foreground){
        context_m = foreground;
        if (are8connections_m){
            findConnections(foreground.getStart()-1, foreground.getEnd());
        } else {
            findConnections(foreground.getStart(), foreground.getEnd()-1);
        }
        incorporatePack(foreground);
    }





    private void incorporatePack(ForegroundData foreground){
        newSpans_m.add(foreground.getRowSpan());
        newKeys_m .add(foreground.getKey());
        newFinishLimits_m.put(foreground.getKey(), foreground.getEnd());
    }

    private void findConnections(int start, int finish){
        int lim = oldSpans_m.size();
        IRowSpan cnn = null;
       
        while (cursor_m < lim){
           cnn = oldSpans_m.get(cursor_m); 

           if (cnn.getEnd() > start) break;

           tryToFinish(cursor_m, start);
           ++cursor_m;
        }


        if (cursor_m == lim || cnn.getStart() > finish){
            fireNoConnectionsFound();
            return;
        }

        fireFirstConnectionFound(cursor_m);

        while (++cursor_m < lim){
            cnn = oldSpans_m.get(cursor_m);
            if (cnn.getStart() > finish) return;

            fireNextConnectionFound(cursor_m);
        }
    }





    private void fireRegionFinished(int oldKey){
        listener_m.onRegionFinished(oldKey);
    }

    private void fireNoConnectionsFound(){
        listener_m.noConnectionFound(context_m);
    }

    private void fireFirstConnectionFound(int oldRegionIndex){
        int oldKey = oldKeys_m.get(oldRegionIndex);
        listener_m.onFirstConnectionFound(context_m, oldKey);
        dismissFinishing(oldKey);
    }

    private void fireNextConnectionFound(int oldRegionIndex){
        int oldKey = oldKeys_m.get(oldRegionIndex);
        int newKey = context_m.getKey();
        if (newKey == oldKey) return;

        listener_m.onNextConnectionFound(context_m, oldKey);
        dismissFinishing(oldKey);
        updateMergedKeys(oldKey, newKey);
    }




    private void updateMergedKeys(int oldKey, int newKey){
        oldKeys_m.replaceAll(oldKey, newKey, cursor_m);
    }




    private void dismissFinishing(int oldKey){
        oldFinishLimits_m.remove(oldKey);
    }

    private void tryToFinish(int regionIndex, int newPackStart){
        int     oldKey = oldKeys_m.get(regionIndex);
        Integer limit  = oldFinishLimits_m.get(oldKey);
        if (limit != null && limit <= newPackStart){
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
         * @param context the pack of the current row,
         * that might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         */
        void noConnectionFound(ForegroundData context);

        /**
         * Signals that connection to a PixelPack
         * from previous row have been found for the first time.
         * @param context is the pack of the current row,
         * which might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         * @param regionKey is the key of the region,
         * which has proved to be connected to the newPack.
         */
        void onFirstConnectionFound(ForegroundData context, int regionKey);


        /**
         * Signals that connection to another PixelPack
         * from previous row have been found for the next time.
         * @param context is the pack of the current row,
         * which might have been connected to other Packs.
         * Parameter provided to increace the visability
         * of this object.
         * @param regionKey is the key of the region,
         * which has proved to be connected to the newPack.
         */
        void onNextConnectionFound(ForegroundData context, int regionKey);


    }


}
