package input;


import java.util.ArrayList;

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
public class InputContext {

	private static InputContext currentContext;

	public ArrayList<Input> inputs = new ArrayList<Input>();

	public void setAsCurrentContext() {

		currentContext = this;
	}

	public static InputContext getCurrentContext() {

		return currentContext;
	}

	public void update(float delta) {

		for (Input input : inputs) {

			input.onUpdate(delta);
		}
	}
}
