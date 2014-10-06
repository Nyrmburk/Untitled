///////////////////////////////////////////////////////////////////////////////
//////////////////////	LOCKED This class is complete.	///////////////////////
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////		.-""-.		///////////////////////////////
//////////////////////////////	   / .--. \		///////////////////////////////
//////////////////////////////	  / /    \ \	///////////////////////////////
//////////////////////////////	  | |    | |	///////////////////////////////
//////////////////////////////	  | |.-""-.|	///////////////////////////////
//////////////////////////////	 ///`.::::.`\	///////////////////////////////
//////////////////////////////	||| ::/  \:: ;	///////////////////////////////
//////////////////////////////	||; ::\__/:: ;	///////////////////////////////
//////////////////////////////	 \\\ '::::' /	///////////////////////////////
//////////////////////////////	  `=':-..-'`	///////////////////////////////
///////////////////////////////////////////////////////////////////////////////

package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Get all the keyboard and mouse data. It can tell if a key state has changed,
 * what its current state is now. It can read and write from a key mapping file
 * (res/keymap.xml) and is fully configurable.
 * 
 * @author Christopher Dombroski
 * 
 */
public class Input {
	/** Value for a key being pressed. */
	public final static byte PRESSED = 1;
	/** Value for a key being Released. */
	public final static byte RELEASED = -1;
	/** Value for a key not being changed. */
	public final static byte NO_CHANGE = 0;

	/** List of all the keys. */
	public static HashMap<String, Integer> keys = new HashMap<String, Integer>();
	/** Whether or not the indicated keys are down. */
	public static HashMap<String, Boolean> keyDown = new HashMap<String, Boolean>();
	/** Whether of not the indicated keys have changed state. */
	public static HashMap<String, Byte> keyChanged = new HashMap<String, Byte>();

	/** Whether or not the user's mouse has a mousewheel. */
	public static boolean hasMouseWheel = Mouse.hasWheel();

	/** The number of button available on the users mouse. */
	public static byte buttonCount = (byte) Mouse.getButtonCount();

	/** Mouse location on the OpenGl Display */
	public static int mouseX, mouseY;
	/** Change in mouse location since last refresh(). */
	public static int deltaX, deltaY;

	/** Which keys are down */
	public static boolean[] mouseDown = new boolean[buttonCount];
	/** Which keys have changed state and what change occured */
	public static byte[] mouseChanged = new byte[buttonCount];

	/** Load the key mappings from res/keymaps.xml. */
	public static void load() {

		Properties loadKeys = new Properties();

		try {
			InputStream inStream = new FileInputStream("res\\keymap.xml");
			loadKeys.loadFromXML(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Object, Object> entry : loadKeys.entrySet()) {

			try {

				String key = entry.getKey().toString();
				int value = (int) Keyboard.class.getField(
						entry.getValue().toString()).get(null);

				keys.put(key, value);
				keyDown.put(key, false);
				keyChanged.put(key, NO_CHANGE);

			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

	}

	/** Update the key and mouse status. */
	public static void refresh() {

		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		deltaX = Mouse.getDX();
		deltaY = Mouse.getDY();

		for (String key : keyChanged.keySet()) {
			keyChanged.put(key, NO_CHANGE);
		}

		for (int i = 0; i < buttonCount; i++) {

			if (Mouse.isButtonDown(i)) {
				if (mouseDown[i] == false) {
					mouseChanged[i] = PRESSED;
				} else {
					mouseChanged[i] = NO_CHANGE;
				}
				mouseDown[i] = true;
			} else {
				if (mouseDown[i] == true) {
					mouseChanged[i] = RELEASED;
				} else {
					mouseChanged[i] = NO_CHANGE;
				}
				mouseDown[i] = false;
			}

		}

		while (Keyboard.next()) {

			if (Keyboard.getEventKeyState()) {

				for (Map.Entry<String, Integer> mapKey : keys.entrySet()) {

					if (Keyboard.getEventKey() == mapKey.getValue()) {
						if (keyDown.get(mapKey.getKey()) == false) {
							keyChanged.put(mapKey.getKey(), PRESSED);
						} else {
							keyChanged.put(mapKey.getKey(), NO_CHANGE);
						}
						keyDown.put(mapKey.getKey(), true);
					}

				}

			} else {

				for (Map.Entry<String, Integer> mapKey : keys.entrySet()) {

					if (Keyboard.getEventKey() == mapKey.getValue()) {
						if (keyDown.get(mapKey.getKey()) == true) {
							keyChanged.put(mapKey.getKey(), RELEASED);
						} else {
							keyChanged.put(mapKey.getKey(), NO_CHANGE);
						}
						keyDown.put(mapKey.getKey(), false);
					}

				}

			}

		}

	}

}
