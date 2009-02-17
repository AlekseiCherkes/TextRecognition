/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 10.02.2009
 * Time: 23:52:04
 * To change this template use File | Settings | File Templates.
 */

package neuron_net;

import java.util.ArrayList;
import java.io.Serializable;

public interface Net extends Serializable {
    void train( String learning_sample );
	void save( String storage );
	void load( String storage );
	Matrix recognize( Matrix x );
}
