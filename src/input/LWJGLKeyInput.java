package input;

import org.lwjgl.input.Keyboard;

public class LWJGLKeyInput extends KeyboardInterface {

	private static final String NAME = "LWJGLKeyInput";

	@Override
	public void updateInputs() {

		while (Keyboard.next()) {

			Input input = inputs.get(Keyboard.getEventKey());

			if (input != null)
				input.setValue(Keyboard.getEventKeyState() ? input.getRange() : -input.getRange());
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getBinding(String name) {

		return Keyboard.getKeyIndex(name);
	}

	@Override
	public String getBindingName(Object binding) {
		return Keyboard.getKeyName(((Integer) binding));
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
	public float getRange(Object binding) {

		return 1;
	}

//	private class LWJGLKeyBinding extends Binding {
//
//		public int keyIndex;
//
//		public LWJGLKeyBinding(int keyIndex) {
//
//			this.keyIndex = keyIndex;
//		}
//
//		@Override
//		public String getName() {
//
//			return getBindingName(this);
//		}
//
//		@Override
//		public int compareTo(Binding binding) {
//
//			return this.keyIndex - ((LWJGLKeyBinding) binding).keyIndex;
//		}
//	}
}
