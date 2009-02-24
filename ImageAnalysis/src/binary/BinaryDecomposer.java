package binary;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Nick
 * Date: 21.02.2009
 * Time: 20:52:51
 * To change this template use File | Settings | File Templates.
 */
public class BinaryDecomposer {

    private PacksRegistry registry_m;

    private IBinaryImage img_m;
    private int x_m;
    private int y_m;
    private int h_m;
    private int w_m;



    private class PixelPack {
        public int x_min;
        public int x_max;
        public int y;

        public int key;

    }

    private class PacksRegistry {
        private ArrayList<PixelPack> expired_m;
        private ArrayList<PixelPack> connectable_m;
        private ArrayList<PixelPack> unready_m;
        private int cursor_m;

        private UnionFind_Structure tree_m;
        private boolean weakConnectionsAllowed_m;

        private PacksRegistry(){
            expired_m     = new ArrayList<PixelPack>();
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

            expired_m.addAll(connectable_m);
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

    public void firstPhase(){
        int h = img_m.getHeight();

        while (y_m < h_m){
			///....
        }

    }




    private void collectBlackPixels_1(){
        PixelPack pack = new PixelPack();
        pack.x_min = x_m;
        pack.y     = y_m;

        while (x_m < w_m && img_m.get(x_m, y_m)){
            ++x_m;
        }

        pack.x_max = x_m - 1;

        registry_m.registerPack(pack);

    }

    private void  skipWhitePixels(){
        while (x_m < w_m && !img_m.get(x_m, y_m)){
            ++x_m;
        }
    }


    


}
