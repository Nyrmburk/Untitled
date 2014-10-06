package main;

public class Settings {
	
	//window settings
	public static int windowWidth, windowHeight;
	public static boolean threeD;

	/**
	 * Load the settings from a file.
	 */
	public static void loadSettings() {
		
		windowWidth = 1200;
		windowHeight = 900;
		threeD = false;
		
	}
	
	/**
	 * Save the settings to a file.
	 */
	public void saveSettings() {
		
	}
}
