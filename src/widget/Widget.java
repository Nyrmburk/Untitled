package widget;

import graphics.InstanceAttributes;
import graphics.ModelLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nyrmburk on 8/18/2016.
 */
public abstract class Widget {

	int received = 0;
	int connectedInputs = 0;

	private List<Synapse> inputs = new ArrayList<>();
	private List<Synapse> outputs = new ArrayList<>();

	public Widget(int inputs, int outputs) {

		setSize(inputs, outputs);
	}

	public abstract void update(float delta);
	public abstract void fire(List<Synapse> inputs, List<Synapse> outputs);

	public abstract String getInputName(int index);
	public abstract String getOutputName(int index);

	public abstract ModelLoader getModel();
	public abstract InstanceAttributes getInstance();

	public boolean connect(int inputIndex, Synapse synapse) {

		// TODO: check for circular reference
		// return false;

		synapse.connect(this);
		Synapse previous = inputs.set(inputIndex, synapse);

		if (previous == null) {
			connectedInputs++;
		} else {
			previous.disconnect(this);
		}

		return true;
	}

	protected void receive() {

		if (++received >= connectedInputs) {
			received = 0;

			fire(inputs, outputs);
		}
	}

	public Synapse getInput(int index) {
		return inputs.get(index);
	}

	public Synapse getOutput(int index) {
		return outputs.get(index);
	}

	public void setSize(int inputs, int outputs) {

		// inputs
		// shrink
		int i = inputs - this.inputs.size();
		while (0 <-- i) {
			this.inputs.remove(i);
			connectedInputs--;
		}

		// grow
		while (++i > 0)
			this.inputs.add(null);

		// outputs
		// shrink
		i = outputs - this.outputs.size();
		while (0 <-- i)
			this.outputs.remove(i);

		// grow
		while (++i > 0)
			this.outputs.add(new Synapse(this));
	}

	public int getInputSize() {
		return inputs.size();
	}

	public int getOutputSize() {
		return outputs.size();
	}
}
