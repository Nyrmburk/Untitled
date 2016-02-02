package input;

import java.util.TreeMap;

/**
 * @author Nyrmburk
 *
 *         InputContext is a class to keep track of the valid inputs and keep
 *         them on a 'layer' of sorts. For example, when playing the game, there
 *         are several inputs that would be active in this class. When the pause
 *         button is hit, a new InputContext would go on top and be the active
 *         context. It would block the underlying inputs so that the pause menu
 *         can prevent the player from moving while paused.
 */
public abstract class InputContext {

	public TreeMap<Object, Input> inputs = new TreeMap<Object, Input>();

	public abstract boolean isValid();

	public void addInput(Input input) {

		inputs.put(input.getBinding(), input);
	}

	public void update(int delta) {

		for (Input input : inputs.values()) {

			input.onUpdate(delta);
		}
	}
}
