package input;

import org.lwjgl.input.Mouse;

import main.Engine;

public class LWJGLMouseInput extends PointerInput {

	public static final int MOUSE_X = Mouse.getButtonCount() + 1;
	public static final int MOUSE_Y = Mouse.getButtonCount() + 2;
	public static final int MOUSE_WHEEL = Mouse.getButtonCount() + 3;

	private Input pointerX;
	private Input pointerY;

	public LWJGLMouseInput() {

		this.addInputContext(new InputContext() {

			{

				pointerX = new Input(MOUSE_X, "pointerX", 1) {
					@Override
					public void onUpdate(int delta) {
					}
				};
				pointerY = new Input(MOUSE_Y, "pointerY", 1) {
					@Override
					public void onUpdate(int delta) {
					}
				};
				
				this.addInput(pointerX);
				this.addInput(pointerY);
			}

			@Override
			public boolean isValid() {
				return true;
			}

			@Override
			public void update(int delta) {
			}
		});
	}

	@Override
	public void getInputs() {

		while (Mouse.next()) {

			for (InputContext context : this) {

				Input input = context.inputs.get(Mouse.getEventButton());

				if (input == null)
					continue;

				input.setValue(Mouse.getEventButtonState() ? input.getRange() : -input.getRange());
			}
		}

		for (InputContext context : this) {

			Input input = context.inputs.get(MOUSE_X);

			if (input == null)
				continue;

			input.setValue(Mouse.getX());
			
			input = context.inputs.get(MOUSE_Y);

			if (input == null)
				continue;

			input.setValue(Engine.renderEngine.getHeight() - Mouse.getY());

			input = context.inputs.get(MOUSE_WHEEL);

			if (input == null)
				continue;

			input.setValue(Mouse.getDWheel());
		}
	}

	@Override
	public String[] getInputNames() {

		String[] names = new String[Mouse.getButtonCount()];

		for (int i = 0; i < names.length; i++)
			names[i] = Mouse.getButtonName(i);

		return names;
	}

	@Override
	public int getChangedInput() {

		if (Mouse.next())
			return Mouse.getEventButton();
		return -1;
	}

	@Override
	public Input getX() {

		return pointerX;
	}

	@Override
	public Input getY() {

		return pointerY;
	}
}
