/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 30.03.2009
 * Time: 9:54:46
 * To change this template use File | Settings | File Templates.
 */

import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.Qt;

public class Main {
    public static void main(String args[]) {
        QApplication.initialize(args);
        MainWindow main_window = new MainWindow();
        main_window.show();
        QApplication.exec();
    }
}
