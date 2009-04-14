package analysis.data.pixels;

import analysis.data.ad_hoc.BitPacker;

/**
 * @author M-NPO
 */
public class RowSpan implements IRowSpan {
    // masks are already shifted by appropriet offset
    private static final int START_MASK = 0x0000ffff;
    private static final int SPAN_MASK  = 0x7fff0000;
    private static final int SPAN_OFFS  = 16;
           
    private int   bits_m;

    public RowSpan(int start, int span){
        bits_m = BitPacker.setBits(0     ,start, START_MASK,         0);
        bits_m = BitPacker.setBits(bits_m,span , SPAN_MASK , SPAN_OFFS);
    }

    public int getStart(){ return  bits_m & START_MASK;}
    public int getEnd  (){ return getStart() + getSpan(); }
    public int getSpan (){ return (bits_m & SPAN_MASK) >>> SPAN_OFFS; }

    @Override
    public String toString(){
        int start = getStart();
        return "(" + start + "; " + (start + getSpan()-1) + ')';
    }    
}
