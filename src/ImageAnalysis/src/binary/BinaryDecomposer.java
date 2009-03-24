package binary;

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


    public void firstPhase(){
        int h = img_m.getHeight();
        int w = img_m.getWidth();

        int x;
        int y;

        y = 0;
        while (y < h){
            x = 0;

            while (x < w && !img_m.get(x, y)) ++x;

            if (x < w){
                PixelPack pack = new PixelPack();
                pack.x_min = x;
                pack.y     = y;

                while (x < w && img_m.get(x, y)) ++x;

                pack.x_max = x - 1;
                registry_m.registerPack(pack);
            }

            registry_m.switchToNextLine();
        }
    }


    public void secondPhase(){

    }






    


}
