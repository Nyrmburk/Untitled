package input;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nyrmburk InputInterface is an interface for raw inputs. It collects
 *         raw values from some device such as a keyboard or joystick and puts
 *         the values into Inputs.
 */
public abstract class InputInterface {

	public static ArrayList<InputInterface> inputInterfaces = new ArrayList<InputInterface>();

	public HashMap<Object, Input> inputs;

	public InputInterface() {

		inputInterfaces.add(this);
		inputs = new HashMap<Object, Input>();
	}

	// provide some means to get hardware input and map it to Input
	public abstract void getInputs();

	public abstract Object getBinding(String name);

	public final void update(int delta) {

		getInputs();

		for (Input input : inputs.values()) {

			input.onUpdate(delta);
		}
	}

	// is this necessary?
	public void addInput(Input input) {

		Object binding = getBinding(input.getName());

		inputs.put(binding, input);
		input.setRange(this.getRange(binding));
	}

	public void removeInput(Input input) {

		inputs.remove(input);
	}

	public void save() {

	}

	public void load() {

	}

	public abstract String[] getInputNames();

	public abstract int getChangedInput();

	public abstract float getRange(Object Binding);
}