package input;

/**
 * Created by Nyrmburk on 2/7/2016.
 */
public abstract class MultiInput extends Input {

	private Input[] inputs;

	public MultiInput(String name) {

		super(name);
	}

	public void setInputs(PartInput... inputs) {

		this.inputs = inputs;

		for (PartInput input : inputs)
			input.inputInterface.addInput(input);
	}

	@Override
	public void onUpdate(int delta) {

		onUpdate(inputs, delta);
	}

	public abstract void onUpdate(Input[] inputs, int delta);

	public class PartInput extends Input {

		InputInterface inputInterface;

		public PartInput(String name, InputInterface inputInterface) {

			super(name);
			this.inputInterface = inputInterface;
		}

		@Override
		public void onUpdate(int delta) {
		}
	}
}