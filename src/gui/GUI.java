package gui;

import java.awt.Color;
import java.util.HashMap;

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
	public static int content = 16;
	
	public static Color systemColor = Color.BLACK;
	public static Color iconColor = Color.WHITE;
	public static Color focusColor = Color.LIGHT_GRAY;
	public static Color unfocusColor = Color.GRAY;
	public static Color textColor = Color.BLACK;
	
	public static FormattedFont mainFont;
	
	private static HashMap<String, GUIElement> elements = new HashMap<String, GUIElement>();
	private static Menu currentMenu;
	
	public static void addElement(String name, GUIElement element) {
		
		elements.put(name, element);
	}
	
	public static GUIElement getElement(String name) {
		
		return elements.get(name);
	}
	
	public static void render() {
		
		for (GUIElement element : elements.values()) {
			
			if (element.isVisible()) element.draw();
		}
	}
	
	public static void setCurrentMenu(Menu menu) {
		
		if (currentMenu != null && currentMenu != menu)
			currentMenu.setVisible(false);
		currentMenu = menu;
	}
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws LWJGLException
	 */
	public static void initialize() throws LWJGLException {
		
		if (Settings.fullscreen)
			Display.setFullscreen(true);
		else
			Display.setDisplayMode(new DisplayMode(Settings.windowWidth,
					Settings.windowHeight));
		Display.setTitle("Colonies");
		Display.create();
		
		mainFont = new FormattedFont(Settings.awtFont, Settings.AAFonts);
	}
	
	public static void awtToGL(Color color) {
		
		GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color
				.getBlue() / 255f);
	}
	
	public static void drawQuad(GUIElement element) {
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(element.x, element.y);
		GL11.glVertex2i(element.x, element.y + element.height);
		GL11.glVertex2i(element.x + element.width, element.y + element.height);
		GL11.glVertex2i(element.x + element.width, element.y);
		GL11.glEnd();
	}
	
	public static void drawBorder(GUIElement element) {
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(element.x, element.y);
		GL11.glVertex2i(element.x, element.y + element.height);
		GL11.glVertex2i(element.x + element.width, element.y + element.height);
		GL11.glVertex2i(element.x + element.width, element.y);
		GL11.glEnd();
	}
}
