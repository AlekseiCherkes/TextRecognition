import java.util.ArrayList;

//===============
// Neuron hierarchy
//===============
abstract public class Neuron{	
	protected float output;
	protected int input_size;	
	protected ArrayList<float> w;	
	public Neuron(int input_size);
	public Neuron(ArrayList<float> w);
	public abstract float processSignal();
	public void setW(int pos, float val);
	public float getW( int pos );
}

public class BiNeuron extends Neuron{
	private float threshold;	
	public BiNeuron(int input_size, float threshold);
	public BiNeuron(ArrayList<float> w, float threshold);
	// threshold activation function
	public boolean processSignal();
	float getThreshold();
	void setThreshold(float val);
} 

//===============
// Layer hierarchy
//===============
public interface Layer{	
	ArrayList<float> processSignal(ArrayList<float> x);	
}

public interface HomoLayer extends Layer{
}


public interface FullConnectedLayer extends Layer{
	FullConnectedLayer(int size);
} 

public class CommonLayer implements HomoLayer, FullConnectedLayer{
	private ArrayList<Neuron> neurons;
}

//===============
// Net hierarchy
//===============

public interface Net extends Serializzable{		
	void train( String learning_sample );
	void save( String storage );
	void load( String storage );
	ArrayList<float> recognize( ArrayList<float> x );
}

public interface HomoNet extends Net{		
}

public interface FullConnectedNet extends Net{	
}

abstract public class CommonNet implements HomoNet, FullConnectedNet{
	private ArrayList<CommonLayer>  layers;	
	public CommonNet( int input_size, int output_size, int layer_size, int layers_count );
	public abstract void train( String learning_sample );
	public abstract void save( String storage );
	public abstract void load( String storage );
	public abstract ArrayList<float> recognize( ArrayList<float> x );
}

public class SimplePerceptron: extends CommonNet{
	public void train( String learning_sample );
	public void save( String storage );
	public void load( String storage );
	public ArrayList<float> recognize( ArrayList<float> x );
}

// сеть встречного распространения
public class CounterPropagationNet: extends CommonNet{
	public void train( String learning_sample );
	public void save( String storage );
	public void load( String storage );
	public ArrayList<float> recognize( ArrayList<float> x );
}