package input;

import org.lwjgl.input.Keyboard;

public class LWJGLKeyInput extends InputInterface {

	@Override
	public void getInputs() {

		while (Keyboard.next()) {

			for (InputContext context : this) {

				Input input = context.inputs.get(Keyboard.getEventKey());

				if (input == null)
					continue;

				input.setValue(Keyboard.getEventKeyState() ? input.getRange() : -input.getRange());
			}
		}
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
}
