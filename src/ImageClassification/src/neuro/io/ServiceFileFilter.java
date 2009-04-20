package neuro.io;

import java.io.FileFilter;
import java.io.File;

/**
 * @author Vadim Shpakovsky.
 */

public class ServiceFileFilter implements FileFilter {
    public boolean accept( File pathname ){
        if ( pathname.getName().charAt( 0 ) == '.' )
            return false;
        else
            return true;
    }
}
