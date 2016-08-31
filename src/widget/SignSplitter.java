package widget;


import java.util.List;

/**
 * Created by Nyrmburk on 8/27/2016.
 */
public class SignSplitter extends SimpleWidget {

	public static final int BOTH_SIGN = 0;
	public static final int POSITIVE_SIGN = 0;
	public static final int NEGATIVE_SIGN = 1;

	public SignSplitter() {
		super(1, 2);
	}

	@Override
	public void fire(List<Synapse> inputs, List<Synapse> outputs) {

		float input = inputs.get(BOTH_SIGN).getValue();

		outputs.get(POSITIVE_SIGN).fire(input > 0 ? input : 0);
		outputs.get(NEGATIVE_SIGN).fire(input < 0 ? input : 0);
	}

	@Override
	public String getInputName(int index) {
		return "both";
	}

	@Override
	public String getOutputName(int index) {
		return index == 0 ? "positive" : "negative";
	}
}
