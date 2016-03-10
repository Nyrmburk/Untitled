package input;

import main.INIWriter;
import main.ResourceManager;

import java.io.File;
import java.util.*;

/**
 * @author Nyrmburk InputInterface is an interface for raw inputs. It collects
 *         raw values from some device such as a keyboard or joystick and puts
 *         the values into Inputs.
 */
public abstract class InputInterface {

	private static List<InputInterface> inputInterfaces = new ArrayList<InputInterface>();

	public Map<Object, Input> inputs;

	public InputInterface() {

		inputInterfaces.add(this);
		inputs = new HashMap<Object, Input>();
	}

	public void addInput(Input input, Object binding) {

//		Object binding = Binding.getBindingFromName(input.getName());
		inputs.put(binding, input);
		input.setRange(this.getRange(binding));
	}

	public void removeInput(Input input) {

		//broken
		inputs.remove(input);
	}

	// provide some means to get hardware input and map it to Input
	public abstract void updateInputs();

	public abstract String getName();

	public abstract Object getBinding(String name);

	public abstract String getBindingName(Object binding);

	public final void update(int delta) {

		updateInputs();

//		for (Input input : inputs.values()) {
//
//			input.onUpdate(delta);
//		}
	}

	public void save() {

		Map<String, String> map = new TreeMap<String, String>();

		for (Map.Entry<Object, Input> entry : inputs.entrySet()) {

			map.put(entry.getValue().getName(), getBindingName(entry.getKey()));
		}

		//TODO replace with Properties and stupid magic strings
		INIWriter.write(map, new File("res\\input\\" + getName()));
	}

	public void load() {

		//TODO replace with Properties and stupid magic strings
		Map<String, String> map = INIWriter.read(new File("res\\input\\" + getName()));

		for (Map.Entry<String, String> entry : map.entrySet()) {

			inputs.get(getBinding(entry.getValue()));
		}
	}

	public abstract String[] getInputNames();

	public abstract int getChangedInput();

	public abstract float getRange(Object binding);

	public static List<InputInterface> getInterfaces() {

		return inputInterfaces;
	}
}