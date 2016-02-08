package input;

import org.lwjgl.input.Keyboard;

public class LWJGLKeyInput extends KeyboardInterface {

	@Override
	public void getInputs() {

		while (Keyboard.next()) {

			Input input = inputs.get(Keyboard.getEventKey());

			if (input != null)
				input.setValue(Keyboard.getEventKeyState() ? input.getRange() : -input.getRange());
		}
	}

	@Override
	public Object getBinding(String name) {

		return Keyboard.getKeyIndex(name);
	}

	@Override
	public String[] getInputNames() {

		String[] names = new String[Keyboard.KEYBOARD_SIZE];

		for (int i = 0; i < names.length; i++)
			names[i] = Keyboard.getKeyName(i);

		return names;
	}

	@Override
	public int getChangedInput() {

		if (Keyboard.next())
			return Keyboard.getEventKey();
		return -1;
	}

	@Override
	public float getRange(Object Binding) {

		return 1;
	}
}
