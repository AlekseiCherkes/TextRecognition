package binary;

/**
 * @author M-NPO
 */
public class Main {
    public static void main(String[] args){
        String msg = args[0];

        for (int i=0 ; i < 500 ; ++i){
            System.out.println(msg);
            
            try {
                Thread.sleep(0);
            }
            catch(InterruptedException e){
                System.out.println("INTERRUPTED!");
                return;
            }
        }
    }

}
