package binary;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
* User: Nick
* Date: 23.03.2009
* Time: 19:31:08
* To change this template use File | Settings | File Templates.
*/
class PacksRegistry {
    private ArrayList<PixelPack> connectable_m;
    private ArrayList<PixelPack> unready_m;
    private int cursor_m;

    private UnionFind_Structure tree_m;
    private boolean weakConnectionsAllowed_m;

    PacksRegistry(){
        connectable_m = new ArrayList<PixelPack>();
        unready_m     = new ArrayList<PixelPack>();

        tree_m = new UnionFind_Structure();
        weakConnectionsAllowed_m = true;

    }

    public void registerPack(PixelPack pack){
        unready_m.add(pack);

        if (weakConnectionsAllowed_m){
            ++pack.x_max;
            --pack.x_min;
            findConnections(pack);
            --pack.x_max;
            ++pack.x_min;
        } else {
            findConnections(pack);
        }
    }

    public void switchToNextLine(){
        ArrayList<PixelPack> tmp;
        connectable_m.clear();

        tmp = connectable_m;
        connectable_m = unready_m;
        unready_m = tmp;

        cursor_m = 0;
    }

    private void findConnections(PixelPack pack){
        int lim = connectable_m.size();
        if (cursor_m == lim){
            pack.key = tree_m.makeRootEntry();
            return;
        }

        PixelPack cnn;

        do {
            cnn = connectable_m.get(cursor_m);
            ++cursor_m;
        } while (cursor_m < lim && cnn.x_max < pack.x_min);


        if (cursor_m == lim || cnn.x_min > pack.x_max){
            pack.key = tree_m.makeRootEntry();
            return;
        }

        pack.key = cnn.key;

        while (++cursor_m < lim){
            cnn = connectable_m.get(cursor_m);
            if (cnn.x_min > pack.x_max) return;

            tree_m.unite(cnn.key,  pack.key);
        }
    }


}
