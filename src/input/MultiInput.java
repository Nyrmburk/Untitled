package input;

/**
 * Created by Nyrmburk on 2/7/2016.
 */
public abstract class MultiInput extends Input {

	private Input[] inputs;

	public MultiInput(String name) {

		super(name);
	}

	public void setInputs(Input... inputs) {

		this.inputs = inputs;

		for (Input input : inputs)
			Binding.delegate(input);
	}

	@Override
	public void onUpdate(float delta) {

		onUpdate(inputs, delta);
	}

	public abstract void onUpdate(Input[] inputs, float delta);

	public class SimpleInput extends Input {

		public SimpleInput(String name) {
			super(name);
		}

		@Override
		public void onUpdate(float delta) {
		}
	}
}