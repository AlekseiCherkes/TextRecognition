package analysis.data.acumulators;

import analysis.data.ad_hoc.BitPacker;
import analysis.data.ad_hoc.ArrayOfInt;
import analysis.data.pixels.IPixelPack;
import analysis.data.pixels.PixelPack;

import java.util.*;

/**
 * author M-NPO
 */
public class PackAccumulator implements IMergible<PackAccumulator>, Iterable<IPixelPack>{

    private class Bundle{
        // masks are already shifted by appropriet offset
        private static final int START_MASK = 0x0000ffff;
        private static final int SPAN_MASK  = 0x7fff0000;
        private static final int SPAN_OFFS  = 16;
        private static final int YINC_MASK  = 0x80000000;
        private static final int YINC_OFFS  = 31;

        private int yMin_m;
        private int yMax_m;

        private ArrayOfInt data_m;

        public Bundle(){
            yMin_m = Integer.MIN_VALUE;
            yMax_m = Integer.MIN_VALUE;
            data_m = new ArrayOfInt(2);
        }

        public boolean isEmpty(){
            return yMax_m == Integer.MIN_VALUE; 
        }

        public int size(){
            return data_m.size();
        }

        private int pack(int xStart, int xSpan, int yInc){
            int pack = BitPacker.setBits(   0, xStart, START_MASK,         0);
            pack     = BitPacker.setBits(pack, xSpan , SPAN_MASK , SPAN_OFFS);
            pack     = BitPacker.setBits(pack, yInc  , YINC_MASK, YINC_OFFS);

            return pack;
        }

        public void append(int xStart, int xSpan, int y){
            assert (yMax_m == Integer.MIN_VALUE || yMax_m == y || yMax_m+1 == y ): "Y index is not valid";

            int yinc = 0;

            if (yMin_m == Integer.MIN_VALUE){
                yMin_m = y;
                yMax_m = y;

            } else if (y > yMax_m){
                yinc = 1;
                ++yMax_m;
            }

            int item = pack(xStart, xSpan, yinc);
            data_m.add(item);
        }

        private int     getStart(int bits){ return  bits & SPAN_MASK;}
        private int     getSpan (int bits){ return (bits & SPAN_MASK) >>> SPAN_OFFS; }
        private boolean isYInc  (int bits){ return (bits & YINC_MASK) != 0;}

    }


    protected class PixelPackIterator implements Iterator<IPixelPack>{
        private Iterator<Bundle> bundlesIter_m;
        private Bundle           currentBundle_m;
        private int              packIndex_m;
        private int              y_m;

        protected PixelPackIterator(){
            bundlesIter_m = bundles_m.iterator();
            packIndex_m   = 0;
        }


        @Override
        public boolean hasNext() {
            while(true){
                if (currentBundle_m == null){
                    if (!bundlesIter_m.hasNext()) return false;
                    currentBundle_m = bundlesIter_m.next();
                    y_m = currentBundle_m.yMin_m;
                    packIndex_m = 0;
                }

                if (packIndex_m < currentBundle_m.size()) return true;
                currentBundle_m = null;
            }
        }

        @Override
        public IPixelPack next() {
            int bits = currentBundle_m.data_m.get(packIndex_m);
            ++packIndex_m;

            if (currentBundle_m.isYInc(bits)) ++y_m;

            PixelPack pack = new PixelPack();
            pack.setY(y_m);
            pack.setStart(currentBundle_m.getStart(bits));
            pack.setSpan (currentBundle_m.getSpan (bits));
            return pack;

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    // PackAccumulator BODY //

    private List<Bundle> bundles_m;
    private Bundle currentBundle_m;

    public PackAccumulator(){
        bundles_m       = new ArrayList<Bundle>();
        currentBundle_m = new Bundle();
        bundles_m.add(currentBundle_m);
    }

    public void append(IPixelPack pack){
        currentBundle_m.append(pack.getStart(), pack.getSpan(), pack.getY());
    }

    public void merge(PackAccumulator data){
        bundles_m.addAll(data.bundles_m);
    }

    @Override
    public Iterator<IPixelPack> iterator() {
        return new PixelPackIterator();
    }
}
