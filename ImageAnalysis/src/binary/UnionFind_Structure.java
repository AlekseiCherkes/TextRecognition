package binary;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 22.02.2009
 * Time: 13:14:24
 * To change this template use File | Settings | File Templates.
 */
public class UnionFind_Structure {
    private ArrayList<Integer> references_m;

    public UnionFind_Structure(){
        references_m = new ArrayList<Integer>(4);
        references_m.add(0);
    }

    public int makeRootEntry(){
        references_m.add(0);
        return references_m.size()-1;
    }

    public int makeChildEntry(int parent){
        references_m.add(parent);
        return references_m.size()-1;
    }

    public void unite(int left, int right){
        int tmp;
        int leftDepth = 0;
        int rightDepth = 0;

        while ((tmp = references_m.get(left)) != 0){
            left = tmp;
            ++leftDepth;
        }

        while ((tmp = references_m.get(right)) != 0){
            right = tmp;
            ++rightDepth;
        }

        if (leftDepth < rightDepth){
            references_m.set(left, right);
        } else {
            references_m.set(right, left);
        }
    }

    public void regudePaths(){
        int size = references_m.size();
        int ref;
        int refref;

        for (int i=2 ; i<size ; ++i){
            ref    = references_m.get(i);
            refref = references_m.get(ref);

            if (refref != 0){
                references_m.set(i, refref);
            }
        }
    }

    





}
