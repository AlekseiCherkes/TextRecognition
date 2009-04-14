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
    private boolean use8connections_m;







    public ConnectionFinder(IConnectionListener listener){
        listener_m        = listener;
        oldSpans_m        = new ArrayList<IRowSpan>();
        newSpans_m        = new ArrayList<IRowSpan>();
        oldKeys_m         = new ArrayOfInt();
        newKeys_m         = new ArrayOfInt();
        newFinishLimits_m = new HashMap<Integer, Integer>();
        oldFinishLimits_m = new HashMap<Integer, Integer>();
    }


    public boolean isUse8connections() {
        return use8connections_m;
    }

    public void setUse8connections(boolean use8connections) {
        use8connections_m = use8connections;
    }

    public void switchToNextLine(){
        finishOldRegions();
        oldSpans_m.clear();
        oldKeys_m.clear();

        ArrayList<IRowSpan> swapList;
        swapList   = oldSpans_m;
        oldSpans_m = newSpans_m;
        newSpans_m = swapList;

        ArrayOfInt swapArray;
        swapArray = oldKeys_m;
        oldKeys_m = newKeys_m;
        newKeys_m = swapArray;

        Map<Integer, Integer> swapMap;
        swapMap           = oldFinishLimits_m;
        oldFinishLimits_m = newFinishLimits_m;
        newFinishLimits_m = swapMap;


        cursor_m  = 0;
        context_m = null;  // JIC

        assert newSpans_m       .isEmpty();
        assert newKeys_m        .isEmpty();
        assert newFinishLimits_m.isEmpty();
    }

    public void find4Connections(ForegroundData foreground){
        context_m = foreground;
        findConnections(foreground.getStart(), foreground.getEnd());
        incorporatePack(foreground);
    }

    public void find8Connections(ForegroundData foreground){
        context_m = foreground;
        findConnections(foreground.getStart()-1, foreground.getEnd()+1);
        incorporatePack(foreground);
    }

    public void findConnections(ForegroundData foreground){
        context_m = foreground;
        if (use8connections_m){
            findConnections(foreground.getStart()-1, foreground.getEnd()+1);
        } else {
            findConnections(foreground.getStart(), foreground.getEnd());
        }
        incorporatePack(foreground);
    }

    public void finish(){
        switchToNextLine();
        finishOldRegions();
    }



    private void incorporatePack(ForegroundData foreground){
        assert foreground.getKey() != 0;

        newSpans_m.add(foreground.getRowSpan());
        newKeys_m .add(foreground.getKey());
        newFinishLimits_m.put(foreground.getKey(), foreground.getEnd());
    }

    private void findConnections(int start, int finish){
        int lim = oldSpans_m.size();
        IRowSpan cnn = null;
        int cnnEnd = 0;
       
        while (cursor_m < lim){
           cnn = oldSpans_m.get(cursor_m); 
           cnnEnd = cnn.getEnd();
           if (cnnEnd > start) break;

           tryToFinish(cursor_m, start);
           ++cursor_m;
        }


        if (cursor_m == lim || cnn.getStart() >= finish){
            fireNoConnectionsFound();
            return;
        }

        fireFirstConnectionFound(cursor_m);

        while ((cnnEnd <= finish) && (++cursor_m < lim)){
            cnn = oldSpans_m.get(cursor_m);
            cnnEnd = cnn.getEnd();
            if (cnn.getStart() >= finish) return;

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
        assert  oldKeys_m.contains(oldKey);

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
        assert  newKeys_m.contains(newKey);

        assert  newFinishLimits_m.containsKey(newKey);
        assert !oldFinishLimits_m.containsKey(newKey);

        assert  oldKeys_m.contains(oldKey);
        assert  oldKeys_m.get(cursor_m) == oldKey;

        oldKeys_m.replaceAll(oldKey, newKey, cursor_m);
        newKeys_m.replaceAll(oldKey, newKey, 0);
        newFinishLimits_m.remove(oldKey);

        assert  oldKeys_m.indexOf(oldKey) < cursor_m;
        assert !newKeys_m.contains(oldKey);
    }




    private void dismissFinishing(int oldKey){
        assert  oldKeys_m.contains(oldKey);
        oldFinishLimits_m.remove(oldKey);
        assert !oldFinishLimits_m.containsKey(oldKey);
    }

    private void tryToFinish(int regionIndex, int newPackStart){
        int     oldKey = oldKeys_m.get(regionIndex);

        assert !newKeys_m.contains(oldKey);
        assert !newFinishLimits_m.containsKey(oldKey);

        Integer limit  = oldFinishLimits_m.get(oldKey);
        if (limit != null && limit <= newPackStart){
            fireRegionFinished(oldKey);
            oldFinishLimits_m.remove(oldKey);
        }
    }

    private void finishOldRegions(){
        for(int oldKey : oldFinishLimits_m.keySet()){
            fireRegionFinished(oldKey);
        }
        oldFinishLimits_m.clear();
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
