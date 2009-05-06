package analysis.data.acumulators;

import analysis.data.ad_hoc.RectBoundsOfInt;
import analysis.data.pixels.*;

/**
 * User: M-NPO
 * Date: 10.04.2009
 * Time: 1:09:05
 */
public class DecomposedRegion implements IMergible<DecomposedRegion>{
    public static enum Keys{ // There is an idea of replacing the lot of fields with
        STATISTICS,          // a single map, which would provide more flexibility,
        CONVEX_HULL,         // as long as all its items remain megible.
        PACKS,
        BOX
    }


    private StatisticsAccumulator statAcc_m;
    private EdgeAccumulator       edgeAcc_m;
    private PackAccumulator       packAcc_m;
    private RectBoundsOfInt       box_m;

    private PixelPack currentLayer_m;

    public DecomposedRegion() {
        statAcc_m = new StatisticsAccumulator();
        edgeAcc_m = new EdgeAccumulator      ();
        packAcc_m = new PackAccumulator      ();
        box_m     = new RectBoundsOfInt      ();

        currentLayer_m = new PixelPack();
    }


    public StatisticsAccumulator getStatAcc() { return statAcc_m; }
    public EdgeAccumulator       getEdgeAcc() { return edgeAcc_m; }
    public PackAccumulator       getPackAcc() { return packAcc_m; }
    public RectBoundsOfInt       getBox    () { return box_m;     }


    
    @Override
    public void merge(DecomposedRegion other) {
        assert this != other : "Self merging!";
        
        statAcc_m.merge(other.statAcc_m);
        edgeAcc_m.merge(other.edgeAcc_m);
        packAcc_m.merge(other.packAcc_m);
        box_m    .merge(other.box_m    );
    }

    public void append(ForegroundData pack){
        statAcc_m.merge (pack.getStatistics());
        packAcc_m.append(pack);
        box_m    .merge (pack);

        if (pack.getY() != currentLayer_m.getY()){
            switchToNextRow(pack);
        } else {
            currentLayer_m.setSpan(
                    pack.getEnd() - currentLayer_m.getStart()
            );
        }

    }


    public void seal(){
        upadteEdge();
    }


    private void switchToNextRow(IPixelPack pack){
        upadteEdge();

        currentLayer_m.setStart(pack.getStart());
        currentLayer_m.setSpan (pack.getSpan ());
        currentLayer_m.setY    (pack.getY    ());
    }


    private void upadteEdge(){
        if (currentLayer_m.getSpan() == 0) return;
        
        edgeAcc_m.takeLine(
            currentLayer_m.getStart(),
            currentLayer_m.getEnd()-1,
            currentLayer_m.getY()
        );
    }
}
