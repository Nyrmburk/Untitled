package main;

public class Settings {
	
	// window settings
	public static int windowWidth, windowHeight;
	public static boolean _3D;
	
	/**
	 * Load the settings from a file.
	 */
	public static void loadSettings() {
		
		windowWidth = 864;
		windowHeight = 540;
		_3D = false;
		
	}
	
	/**
	 * Save the settings to a file.
	 */
	public void saveSettings() {
		
	}
}
