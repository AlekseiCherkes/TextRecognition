package neuro.net;

import jblas.matrices.Matrix;

import java.util.*;

/**
 * @author Vadim Shpakovsky.
 */

// Contain analysis.data necessary for net teaching.
public class TeachingData {
    private TreeMap< String, TreeSet< String > > types;
    private TreeMap< String, TreeMap< String, Matrix > > net_inputs;

    public TeachingData(){
        types = new TreeMap< String, TreeSet< String > >();
        net_inputs = new TreeMap< String, TreeMap< String, Matrix > >();
    }

    /**
     * @return      List of types' names.
     */
    public TreeSet< String > getTypes(){
        TreeSet< String > type_set = new TreeSet< String >();
        Set< Map.Entry< String, TreeSet< String > > > set = types.entrySet();
        for( Map.Entry< String, TreeSet< String > > pair : set ){
            type_set.add( pair.getKey() );
        }
        return type_set;
    }

    /** Add new type. If such type already presents do nothing.
     * @param type_name     Name of adding type.
     */
    public void addType( String type_name ){
        if ( !types.containsKey( type_name ) ){
            types.put( type_name, new TreeSet< String >() );
            net_inputs.put( type_name, new TreeMap< String, Matrix >() );
        }
    }

    /** Get images for request type.
     * @param type      Name of type which images we want to get.
     * @return          Set of images' names for request type. Returns 'null' if there is no such type.
     */
    public TreeSet< String > getImages( String type ){
        return types.get( type );
    }

    /** Add new image. If such image already is present do nothing. If image's type not presents add it.
     * @param type      Type of adding image.
     * @param image     Name of adding image.
     */
    public void addImage( String type, String image ){
        if ( !types.containsKey( type ) ){
            types.put( type, new TreeSet< String >() );
        }
        TreeSet< String > images = types.get( type );
        if ( !images.contains( image ) ){
            images.add( image );
            types.put( type, images );
        }
    }

    /** Get input matrix for request image.
     * @param type      Name of image's type.
     * @param image     Name of image which input we want to get.
     * @return          Input matrix for request image. Returns 'null' if there is no such image.
     */
    public Matrix getNetInput( String type, String image ){
        TreeMap< String, Matrix > type_inputs = net_inputs.get( type );
        if ( type_inputs == null )
            return null;
        return type_inputs.get( image );
    }

    /** Add net input for existing image. If such image not present add it.
     * @param type      Type of image.
     * @param image     Name of image.
     * @param input     Adding input.
     */
    public void addNetInput( String type, String image, Matrix input ){
        addImage( type, image );
        TreeMap< String, Matrix > inputs = net_inputs.get( type );
        inputs.put( image, input );
        net_inputs.put( type, inputs );
    }

    /**
     * @return      Count of all teaching images.
     */
    public int getImagesCount(){
        int count = 0;
        Set< Map.Entry< String, TreeSet< String > > > set = types.entrySet();
        for( Map.Entry< String, TreeSet< String > > pair : set ){
            count += pair.getValue().size();
        }
        return count;
    }
}
