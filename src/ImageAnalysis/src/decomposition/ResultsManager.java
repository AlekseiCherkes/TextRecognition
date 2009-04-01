package decomposition;

import decomposition.PixelPack;

import java.util.Map;
import java.util.Set;

import decomposition.FigureStatistics;
import decomposition.ImageData;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 29.03.2009
 * Time: 13:11:39
 * To change this template use File | Settings | File Templates.
 */
public class ResultsManager {

    private Map<Integer, FigureStatistics> statMap_m;
    private Map<Integer, ImageData>        dataMap_m;

    private int nextKey_m;

    private Set<Integer> oldRegions_m;
    private Set<Integer> unfinishedRegions_m;


    public FigureStatistics removeStatistics(int regionKey){
        return statMap_m.remove(regionKey);
    }

    public ImageData removeData(int regionKey){
        return dataMap_m.remove(regionKey);
    }


    public int registerRegion(FigureStatistics stat, PixelPack pack){
        ImageData data = new ImageData();
        data.append(pack);


        dataMap_m.put(nextKey_m, data);
        statMap_m.put(nextKey_m, stat);

        return ++nextKey_m;
    }

    public void appendRegionStatistics(FigureStatistics stat, PixelPack pack, int regionKey){

        ImageData data = dataMap_m.get(regionKey);
        data.append(pack);

        FigureStatistics regionStat = statMap_m.get(regionKey);
        regionStat.merge(stat);
    }


    public int mergeRegions(int leftKey, int rightKey){
        FigureStatistics leftStat  = statMap_m.get   (leftKey );
        FigureStatistics rightStat = statMap_m.remove(rightKey);

        ImageData leftData  = dataMap_m.get   (leftKey );
        ImageData rightData = dataMap_m.remove(rightKey);

        leftStat.merge(rightStat);
        leftData.merge(rightData);

        return leftKey;
    }


}
