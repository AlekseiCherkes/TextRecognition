package neuro.layer;

import jblas.matrices.Matrix;

import java.util.Random;
import java.io.PrintWriter;
import java.io.Serializable;

// @author Vadim Shpakovsky.

// Describe layer of neurons in neuron  network.
abstract public class Layer implements Serializable {
    // Matrix of sinapse's weights.
    protected Matrix w;

    /**
     * @return      The number of neurons in layer.
     * */
    public int size(){
        return w.getColumnDimension();
    }

    /**
     * @return      The size of previous layer.
     * */
    public int prevSize(){
        return w.getRowDimension();
    }

    abstract Layer copy();

    /** Print the weight matrix to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void print( int precision ){
        w.print( 2, precision );
    }

     /** Print the weight matrix to stdout.
     * @param precision     Number of digits after the decimal.
     */
    public void print( PrintWriter out, int precision ){
        w.print( out, 2, precision );
    }

    /** Initialize weight matrix with random numbers.
        @param max_val     Random numbers will be generate in range (0, max_val).
     */
    public void randomInit( double max_val){
        Random rand = new Random();
        for ( int i = 0; i < w.getRowDimension(); i++ ){
            for ( int j = 0; j < w.getColumnDimension(); j++ ){
                double val = max_val * rand.nextDouble();
                w.set( i, j, val );
            }
        }
    }

    /**W = W + dw
     * @param dw    Adding matrix.
     */
    public void addW( Matrix dw ){
        w = w.plus( dw );
    }

     /** Get a weight matrix.
   @return     w.
   */
    public Matrix getW(  ){
        return w.copy();
    }

   /** Get a single weight.
   @param i    Row index.
   @param j    Column index.
   @return     w(i,j)
   */
    public double getW( int i, int j ){
        return w.get( i, j );
    }

   /** Set a single weight.
   @param i    Row index.
   @param j    Column index.
   @param val  Setting value. 
   */
    public void setW( int i, int j, double val ){
        w.set( i, j, val );
    }
}
