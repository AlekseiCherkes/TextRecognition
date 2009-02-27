package binary;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 22.02.2009
 * Time: 13:14:24
 
 * The class represents the forest-like structure
 * where each tree root has itself as a parent.
 */
public class UnionFind_Structure {
    private ArrayList<Integer> references_m;

    public UnionFind_Structure(){
        references_m = new ArrayList<Integer>(4);
    }

    /**
     * Makes new tree in the forest
     * @return the root been made
     */
    public int makeRootEntry(){
        int newKey = references_m.size();
        references_m.add(newKey);
        return newKey;
    }

    /**
     * Adds a child to the specitied parent
     * @param  parent the parent for new entry
     * @return the child been made
     */
    public int makeChildEntry(int parent){
        int newKey = references_m.size();
        references_m.add(parent);
        return newKey;
    }

    /**
     * Merges the two thees into one
     * @param left any node of the first tree
     * @param right any node of the second tree
     */
    public void unite(int left, int right){
        int tmp;
        int leftDepth = 0;
        int rightDepth = 0;

        while ((tmp = references_m.get(left)) != left){
            left = tmp;
            ++leftDepth;
        }

        while ((tmp = references_m.get(right)) != right){
            right = tmp;
            ++rightDepth;
        }

        if (leftDepth < rightDepth){
            references_m.set(left, right);
        } else {
            references_m.set(right, left);
        }
    }

    /**
     * Flattens all paths of all trees so
     * that any node in the tree has
     * the root of that tree as an immediate parent.
     */
    public void regudePaths(){
        int size = references_m.size();
        int parent;
        int refref;

        for (int i=1 ; i<size ; ++i){
            parent = references_m.get(i);
            refref = references_m.get(parent);

            //TODO: learn wether it will be faster without check
            if (refref != parent){
                references_m.set(i, refref);
            }
        }
    }


    /**
     * Flattens all paths of all trees so
     * that any node in the tree has
     * the root of that tree as an immediate parent.
     *
     * Also it changes values of all the roots,
     * reducing gaps in their numeration.
     * @return the number of the roots
     */
    public int allotRoots(){
        int size = references_m.size();
        int parent;
        int refref;
        int cnt = 0;

        for (int i=1 ; i<size ; ++i){
            parent = references_m.get(i);

            if (i == parent){
                references_m.set(i, cnt);
                ++cnt;

            } else {
                refref = references_m.get(parent);
                references_m.set(i, refref);
            }
        }
        return cnt;
    }


    

    





}
