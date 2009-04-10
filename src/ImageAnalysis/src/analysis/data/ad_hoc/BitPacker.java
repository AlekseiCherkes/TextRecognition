package analysis.data.ad_hoc;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 05.04.2009
 * Time: 22:29:34
 * To change this template use File | Settings | File Templates.
 */
public class BitPacker {

    public static int getBits(int src, int mask, int offset){
        return (src & mask) >>> offset;
    }

    public static int getBits(int src, int mask){
        return src & mask;
    }


    public static int setBits(int dst, int bits, int mask, int offset){
        assert ((bits << offset) &  ~mask) == 0: "Not enough bit spase for value";
        dst = (dst & ~mask) | ((bits << offset) & mask);
        return dst;
    }

    public static int setBits(int dst, int bits, int mask){
        assert (bits &  ~mask) == 0: "Not enough bit spase for value";
        dst = (dst & ~mask) | (bits & mask);
        return dst;
    }


}
