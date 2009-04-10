package analysis.data.acumulators;

import analysis.data.pixels.ForegroundData;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 10.04.2009
 * Time: 1:09:05
 * To change this template use File | Settings | File Templates.
 */
public class DecomposedRegion implements IMergible<DecomposedRegion>{

    private StatisticsAccumulator statAcc_m;
    private EdgeAccumulator       edgeAcc_m;
    private PackAccumulator       packAcc_m;

    public DecomposedRegion() {
        statAcc_m = new StatisticsAccumulator();
        edgeAcc_m = new EdgeAccumulator      ();
        packAcc_m = new PackAccumulator      ();
    }


    public StatisticsAccumulator getStatAcc() { return statAcc_m; }
    public EdgeAccumulator       getEdgeAcc() { return edgeAcc_m; }
    public PackAccumulator       getPackAcc() { return packAcc_m; }

    @Override
    public void merge(DecomposedRegion other) {
        assert this != other : "Self merging!";
        
        statAcc_m.merge(other.statAcc_m);
        edgeAcc_m.merge(other.edgeAcc_m);
        packAcc_m.merge(other.packAcc_m);
    }

    public void append(ForegroundData pack){
        statAcc_m.merge (pack.getStatistics());
        packAcc_m.append(pack);
        //TODO: append edgeAcc_m;
    }
}
