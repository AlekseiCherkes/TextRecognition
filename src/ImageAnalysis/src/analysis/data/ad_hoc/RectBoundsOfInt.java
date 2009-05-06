package analysis.data.ad_hoc;

import analysis.data.acumulators.IMergible;
import analysis.data.pixels.IPixelPack;

import java.awt.geom.Point2D;

/** It is one more product of the Java uglyness,
 *  inspired by it and cursed with it.
 *  For it is ugly to implement such manually.
 */
public class RectBoundsOfInt implements IMergible<RectBoundsOfInt> {
    private int north;
    private int west;
    private int south;
    private int east;

    public RectBoundsOfInt(){
        north = west = Integer.MAX_VALUE;
        south = east = Integer.MIN_VALUE;
    }

    public RectBoundsOfInt(int x, int y, int w, int h){
        assert h >= 0;
        assert w >= 0;

        north = y;
        west  = x;
        south = y + h;
        east  = x + w;
    }

    



    public int getNorth() {
        return north;
    }
    public void setNorth(int north) {
        this.north = north;
    }


    public int getWest() {
        return west;
    }
    public void setWest(int west) {
        this.west = west;
    }


    public float getSouth() {
        return south;
    }
    public void setSouth(int south) {
        this.south = south;
    }


    public int getEast() {
        return east;
    }
    public void setEast(int east) {
        this.east = east;
    }




    public int getWidth(){
        return east - west;
    }

    public int getHeight(){
        return south - north;
    }


    public Point2D getCenter(){
        return new Point2D.Float(
                0.5f * (west  + east ),
                0.5f * (north + south)
                );
    }
    

    public boolean contains(int x, int y){
        return y >= north
            && x >= west
            && y <  south
            && x <  east;
    }


    public boolean contains(IPixelPack pack){
        return pack.getY    () >= north
            && pack.getStart() >= west
            && pack.getY    () <  south
            && pack.getEnd  () <= east;
    }

    public boolean contains(RectBoundsOfInt rect){
        return rect.north >= north
            && rect.west  >= west
            && rect.south <= south
            && rect.east  <= east;
    }



    public void merge(int x, int y){
        north = Math.min(north, y  );
        west  = Math.min(west , x  );
        south = Math.max(south, y+1);
        east  = Math.max(east , x+1);
    }


    public void merge(IPixelPack pack){
        north = Math.min(north, pack.getY());
        west  = Math.min(west , pack.getStart());
        south = Math.max(south, pack.getY()+1);
        east  = Math.max(east , pack.getEnd());
    }


    @Override
    public void merge(RectBoundsOfInt other) {
        assert this != other;

        north = Math.min(north, other.north);
        west  = Math.min(west , other.west );
        south = Math.max(south, other.south);
        east  = Math.max(east , other.east );
    }

    public void merge(RectBoundsOfFloat other){
        north = (int) Math.min(north, Math.floor(other.getNorth()));
        west  = (int) Math.min(west , Math.floor(other.getWest ()));
        south = (int) Math.max(south, Math.ceil (other.getSouth()));
        east  = (int) Math.max(east , Math.ceil (other.getEast ()));
    }


     public void expandAroundNewCenter(float centerX, float centerY){
        north = Math.min(north, (int) Math.floor(centerY - (south-centerY)));
        west  = Math.min(west , (int) Math.floor(centerX - (east -centerX)));
        south = Math.max(south, (int) Math.ceil (centerY + (centerY-north)));
        east  = Math.max(east , (int) Math.ceil (centerX + (centerX- west)));
    }

    @Override
    public RectBoundsOfInt clone(){
        RectBoundsOfInt cloned = new RectBoundsOfInt();
        cloned.north = north;
        cloned.west  = west;
        cloned.south = south;
        cloned.east  = east;
        return cloned;
    }
}
