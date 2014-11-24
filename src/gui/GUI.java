package gui;

import java.awt.Color;

import main.Settings;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GUI {
	
	public static final int SMALL_SCALE = 1;
	public static final int NORMAL_SCALE = 2;
	public static final int LARGE_SCALE = 3;
	public static final int HUGE_SCALE = 4;
	
	/**
	 * Scale for the pixels. It allows for various dimensions of ui to be drawn.
	 * ps stands for Pixel Scale.
	 */
	public static int ps = NORMAL_SCALE;
	public static int padding = 4;
	
	public static Color systemColor = Color.BLACK;
	public static Color iconColor = Color.WHITE;
	public static Color focusColor = Color.LIGHT_GRAY;
	public static Color unfocusColor = Color.GRAY;
	public static Color textColor = Color.BLACK;
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws LWJGLException
	 */
	public static void initialize() throws LWJGLException {
		
		Display.setDisplayMode(new DisplayMode(Settings.windowWidth,
				Settings.windowHeight));
		// Display.setFullscreen(true);
		// Settings.windowWidth = Display.getWidth();
		// Settings.windowHeight = Display.getHeight();
		Display.setTitle("Colonies");
		Display.create();
		
	}
	
	public static void awtToGL(Color color) {
		
		GL11.glColor3b((byte) GUI.systemColor.getRed(), (byte) GUI.systemColor.getGreen(),
				(byte) GUI.systemColor.getBlue());
	}
}
