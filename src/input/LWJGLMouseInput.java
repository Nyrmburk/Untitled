package input;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class LWJGLMouseInput extends PointerInput {

	@Override
	public void getInputs() {

		Input input;

		while (Mouse.next()) {

			input = inputs.get(translateBindings(Mouse.getEventButton()));

			if (input != null)
				input.setValue(Mouse.getEventButtonState() ? input.getRange() : -input.getRange());
		}

		input = inputs.get(bindings.X_COORD);
		if (input != null)
			input.setValue(Mouse.getX());
		input = inputs.get(bindings.Y_COORD);
		if (input != null)
			input.setValue(Display.getHeight() - Mouse.getY());

		input = inputs.get(bindings.X_SCROLL);
		if (input != null)
			input.setValue(Mouse.getDWheel());

		// Y_SCROLL not supported by lwjgl
	}

	@Override
	public Object getBinding(String name) {

		return PointerInput.bindings.valueOf(name);
	}

	@Override
	public String[] getInputNames() {

		bindings[] bindings = PointerInput.bindings.values();
		String[] names = new String[bindings.length];

		return names;
	}

	@Override
	public int getChangedInput() {

		if (Mouse.next())
//			return translateBindings(Mouse.getEventButton()).ordinal();
			return Mouse.getEventButton();
		return -1;
	}

	@Override
	public float getRange(Object Binding) {

		return 1;
	}

	private static bindings translateBindings(int lwjglMouseIndex) {

		bindings bindings = null;
		if (lwjglMouseIndex > -1)
			bindings = bindings.values()[lwjglMouseIndex + bindings.BUTTON_0.ordinal()];

		return bindings;
	}
}
