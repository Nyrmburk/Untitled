package widget;

import java.util.List;

/**
 * Created by Nyrmburk on 8/27/2016.
 */
public class Inverter extends SimpleWidget {

	public static final int NORMAL = 0;
	public static final int INVERTED = 0;

	public Inverter() {
		super(1, 1);
	}

	@Override
	public void fire(List<Synapse> inputs, List<Synapse> outputs) {

		float input = inputs.get(NORMAL).getValue();

		// map (0,1] to (1, 0] and
		// [-1, 0) to  [0, -1)
		// this effectively inverts the input
		input = (-input + 1);
		if (input > 1)
			input = input - 2;

		outputs.get(INVERTED).fire(input);
	}

	@Override
	public String getInputName(int index) {
		return null;
	}

	@Override
	public String getOutputName(int index) {
		return null;
	}
}
