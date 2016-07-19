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

//	public InputInterface input;

	private InputContext context;

	private String name;
//	private Object binding;

	private float delta;
	private float range;
	// private float deadzone;

	private float value;

	public Input(String name) {

		this.name = name;
	}

	public abstract void onUpdate(float delta);

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

	protected void setRange(float range) {

		this.range = range;
	}

	public String getName() {

		return name;
	}
}
