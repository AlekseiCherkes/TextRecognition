package analysis.decomposition;

import analysis.data.acumulators.DecomposedRegion;
import analysis.data.pixels.ForegroundData;

import java.util.Map;
import java.util.HashMap;

/**
 * @author M-NPO
 */
public class RegionsKeeper {
    private Map<Integer, DecomposedRegion> regions_m;
    private int identity_m;

    public RegionsKeeper(){
        regions_m  = new HashMap<Integer, DecomposedRegion>();
        identity_m = 1;
    }

    public int registerNewRegion(ForegroundData pack){
        DecomposedRegion region = new DecomposedRegion();
        region.append(pack);
        return add(region);
    }


    public void appendOldRegion(ForegroundData pack, int oldKey){
        DecomposedRegion region = get(oldKey);
        region.append(pack);
    }

    public int megreOldRegions(int leftKey, int rightKey){
        DecomposedRegion left  = get(leftKey );
        DecomposedRegion right = get(rightKey);
        left.merge(right);
        return leftKey;
    }


    public DecomposedRegion removeRegion(int key){
        return remove(key);
    }


    private int add(DecomposedRegion region){
        assert regions_m.get(identity_m) == null : "Wrong identity!";
        assert region != null: "Adding null region!";

        regions_m.put(identity_m, region);
        return identity_m++;
    }


    private DecomposedRegion get(int key){
        assert regions_m.get(key) != null : "Wrong key!";
        return regions_m.get(key);
    }


    private DecomposedRegion remove(int key){
        assert regions_m.get(key) != null : "Wrong key!";
        return regions_m.remove(key);
    }


}
