package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import main.Engine;
import main.Settings;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
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

	public static JFrame frmMain;
	public static Canvas display;

	private static ArrayList<GUIElement> elements = new ArrayList<GUIElement>();
	private static Menu currentMenu;

	public static void addElement(GUIElement element) {

		elements.add(element);
	}

	public static GUIElement getElement(String name) {

		for (GUIElement element : elements) {

			if (element.getName().equals(name)) {

				return element;

			} else if (element instanceof Container) {

				GUIElement child = ((Container) element).getChild(name);
				if (child != null)
					return child;
			}
		}

		return null;
	}

	public static void update() {

		for (GUIElement element : elements) {

			if (element.isVisible()) {

				element.update();

				if (element instanceof Container)
					((Container) element).updateChildren();

				if (!element.isValid())
					element.revalidate();
			}

		}
	}

	public static void revalidate() {

		for (GUIElement element : elements) {

			element.revalidate();
		}
	}

	public static void render() {

		for (GUIElement element : elements) {

			if (element.isVisible())
				element.draw();
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

		frmMain = new JFrame();
		display = new Canvas();
		display.setBackground(Color.BLACK);
		display.setSize(Settings.windowWidth, Settings.windowHeight);
		frmMain.add(display);
		frmMain.pack();
		frmMain.setTitle("Colonies");

		frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Engine.isClosing = true;
			}
		});

		if (Settings.fullscreen)
			Display.setFullscreen(true);
		else {
			Display.setParent(display);
			frmMain.setVisible(true);
		}
		Display.create();

		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("Java version " + System.getProperty("java.version"));
		System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
		System.out.println("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println();

		mainFont = new FormattedFont(Settings.awtFont, Settings.AAFonts);
	}

	public static void loadGUI() {

		// TODO move call out of assetmanager and into initgui when graphics
		// engine complete
		elements.addAll(GUIParser.loadGUI().values());
	}

	public static void awtToGL(Color color) {

		GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
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
