package analysis.data.ad_hoc;

import analysis.data.pixels.IPixelPack;


public class RectBoundsOfFloat implements Cloneable{
    private float north;
    private float west;
    private float south;
    private float east;

    public RectBoundsOfFloat(){
        north = west = Float.POSITIVE_INFINITY;
        south = east = Float.NEGATIVE_INFINITY;
    }

    public RectBoundsOfFloat(float x, float y, float w, float h){
        assert h >= 0;
        assert w >= 0;

        north = y;
        west  = x;
        south = y + h;
        east  = x + w;
    }


    public RectBoundsOfInt toIntBounds(){
        RectBoundsOfInt result =  new RectBoundsOfInt();
        result.setNorth((int) Math.floor(north));
        result.setWest ((int) Math.floor(west ));
        result.setSouth((int) Math.ceil (south));
        result.setEast ((int) Math.ceil (east ));
        return result;
    }


    public float getNorth() {
        return north;
    }
    public void setNorth(float north) {
        this.north = north;
    }


    public float getWest() {
        return west;
    }
    public void setWest(float west) {
        this.west = west;
    }


    public float getSouth() {
        return south;
    }
    public void setSouth(float south) {
        this.south = south;
    }


    public float getEast() {
        return east;
    }
    public void setEast(float east) {
        this.east = east;
    }


    public float getWidth(){
        return east - west;
    }

    public float getHeight(){
        return south - north;
    }


    public boolean contains(float x, float y){
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

    public boolean contains(RectBoundsOfFloat rect){
        return rect.north >= north
            && rect.west  >= west
            && rect.south <= south
            && rect.east  <= east;
    }



    public void merge(float x, float y){
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


    public void merge(RectBoundsOfFloat other) {
        assert this != other;

        north = Math.min(north, other.north);
        west  = Math.min(west , other.west );
        south = Math.max(south, other.south);
        east  = Math.max(east , other.east );
    }

    @Override
    public RectBoundsOfFloat clone(){
        RectBoundsOfFloat cloned = new RectBoundsOfFloat();
        cloned.north = north;
        cloned.west  = west;
        cloned.south = south;
        cloned.east  = east;
        return cloned;
    }
}

