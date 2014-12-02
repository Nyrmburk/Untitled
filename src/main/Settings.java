package main;

import java.awt.Font;

public class Settings {
	
	// window settings
	public static int windowWidth, windowHeight;
	public static boolean fullscreen;
	public static boolean _3D;
	public static boolean AAFonts;
	public static int fontSize;
	public static Font awtFont;
	
	/**
	 * Load the settings from a file.
	 */
	public static void loadSettings() {
		
		windowWidth = 1024;
		windowHeight = 768;
		_3D = false;
		AAFonts = true;
		fontSize = 14;
		awtFont = new Font("Arial", Font.PLAIN, fontSize);
		fullscreen = false;
		
	}
	
	/**
	 * Save the settings to a file.
	 */
	public void saveSettings() {
		
	}
}
