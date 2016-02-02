package input;

/**
 * @author Nyrmburk
 *
 *         This is the class that holds the actual values of inputs. For example
 *         a mouse, button or joystick. For an analog input such as a joystick,
 *         values are usually normalized between -1 and 1 with everything in
 *         between. Digital inputs like buttons only have discrete -1 and 1 with
 *         no values between. Absolute inputs like mouse input or touchpads are
 *         not scaled and are used as raw values.
 *
 */
public abstract class Input {

	public static InputInterface input;

	public String name;
	private Object binding;

	private float delta;
	private float range;
	// private float deadzone;

	private float value;

	public Input(Object binding, String name, float range) {

		this.binding = binding;
		this.name = name;
		this.range = range;
	}

	public abstract void onUpdate(int delta);

	public float getValue() {

		return value;
	}

	public void setValue(float value) {

		this.delta = value - this.value;
		this.value = value;
	}

	public float getNormalizedValue() {

		return value / range;
	}

	public float getDelta() {

		return delta;
	}

	public float getRange() {

		return range;
	}

	public Object getBinding() {

		return binding;
	}
}
