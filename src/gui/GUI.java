package gui;

import main.Settings;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GUI {
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws LWJGLException
	 */
	public static void initialize() throws LWJGLException {

		Display.setDisplayMode(new DisplayMode(Settings.windowWidth, Settings.windowHeight));
		Display.setTitle("Colonies");
		Display.create();

	}
}
