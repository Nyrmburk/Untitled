package widget;

import java.util.List;

/**
 * Created by Nyrmburk on 8/27/2016.
 */
public class SignMerger extends SimpleWidget {

	public static final int POSITIVE_SIGN = 0;
	public static final int NEGATIVE_SIGN = 1;
	public static final int BOTH_SIGN = 0;

	public SignMerger() {
		super(2, 1);
	}

	@Override
	public void fire(List<Synapse> inputs, List<Synapse> outputs) {

		float positive = inputs.get(POSITIVE_SIGN).getValue();
		float negative = inputs.get(NEGATIVE_SIGN).getValue();

		outputs.get(BOTH_SIGN).fire(negative + positive);
	}

	@Override
	public String getInputName(int index) {
		return index == 0 ? "positive" : "negative";
	}

	@Override
	public String getOutputName(int index) {
		return "both";
	}
}
