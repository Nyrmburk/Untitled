package main;

import activity.Activity;
import activity.SpaceGameActivity;
import game.Level;
import graphics.RenderContext;
import graphics.RenderEngine;
import graphics.opengl.ShaderProgram;
import graphics.opengl.SimpleOpenGL3_0RenderEngine;
import input.*;
import matrix.Vec2;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import physics.Polygon;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Engine {

	private static Timer timer = new Timer();

	/** Whether or not the application is closing **/
	public static boolean isClosing = false;

	public static RenderEngine renderEngine;
	public static PointerInput pointer;
	public static Level level;

	public static void main(String[] args) {

		start();
	}

	/**
	 * Starts the engine.
	 *
	 * @throws IOException
	 */
	public static void start() {

		// Set everything up
		initialise();

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// the main engine loop
		while (!Display.isCloseRequested() && !isClosing) {

			long delta = timer.tick();
			// update the entities and whatnot in the engine
			update(Timer.nanosToSeconds(delta));

			renderEngine.start();
			// render the scene
			RenderContext renderContext = level != null ? level.getRenderContext() : null;
			if (renderContext != null)
				renderEngine.render(renderContext);

			renderUI();

			// update the window with the now rendered image
			Display.update();

			// enact the fps cap
			timer.sync(renderEngine.getCurrentDisplay().getRefreshRate());
		}

		close();
	}

	/**
	 * Safely close down the engine
	 */
	public static void close() {

		System.out.println();
		System.out.println("shutting down");

		System.out.print("deleting shaders... ");
		ShaderProgram.deleteAll();
		System.out.println("done");

		// dispose of the graphics
		System.out.print("destroying display... ");
		Display.destroy();
		System.out.println("done");

		// dispose of the window
		System.out.print("disposing window... ");
//		GUI.frmMain.dispose();
		System.out.println("done");

		// shut down the jvm
		System.out.println("exiting... ");
		System.exit(0);
	}

	/**
	 * Initialize everything
	 *
	 */
	private static void initialise() {

		Settings.loadSettings();
		initSystem();
		initDisplay();
		printSystem();
		initWorld();
	}

	/**
	 * Set up the rendering and whatnot for various operating systems
	 */
	private static void initSystem() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.startsWith("win")) {
			os = "win";
		} else if (os.startsWith("mac")) {
			os = "mac";
		} else if (os.startsWith("linux")) {
			os = "linux";
		}

		String natives = System.getProperty("user.dir") + File.separator
				+ "lib" + File.separator + "natives-" + os;
		System.setProperty("org.lwjgl.librarypath", natives);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to load default look and feel");
		}

		pointer = new LWJGLMouseInput();

	}

	/**
	 * Builds the window and creates opengl context
	 */
	private static void initDisplay() {

		renderEngine = new SimpleOpenGL3_0RenderEngine();
		renderEngine.createWindow(Settings.window);
	}

	private static void printSystem() {

		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("Java version " + System.getProperty("java.version"));
		System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
		System.out.println("Renderer version " + renderEngine.getRendererVersion());
		System.out.println("Render Device " + renderEngine.getRenderDevice());
		System.out.println();
	}

	/**
	 * Load the game
	 */
	private static void initWorld() {

		//testing for NinePatch
		//temporary
//		BufferedImage image = null;
//		try {
//			image = ImageIO.read(new File("dev\\plain.9.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		new NinePatch(image);

		//testing for convex hull and decomposition
		Vec2[] winding = {
			new Vec2(5, 0),
			new Vec2(6, 4),
			new Vec2(4, 5),
			new Vec2(1, 5),
			new Vec2(1, 0),
		};
		Polygon.minimize(winding, 3);
		Polygon.isWindingCCW(winding);
		Polygon.reverseWinding(winding);
		Polygon.isWindingCCW(winding);

		//temporary
		Vec2[] poly = {
				new Vec2(-6.0507107f, 3.2494562f),
				new Vec2(-6.12422f, -0.42606285f),
				new Vec2(-1.7870836f, -0.5363283f),
				new Vec2(-1.8605931f, 0.750104f),
				new Vec2(-3.7718687f, 0.7868591f),
				new Vec2(-3.7718687f, 2.2938218f),
				new Vec2(2.4030337f, 1.9997791f),
				new Vec2(2.1089957f, 0.60308206f),
				new Vec2(-0.5741484f, 0.56632686f),
				new Vec2(-0.610903f, -0.7201042f),
				new Vec2(4.3510737f, -0.64659387f),
				new Vec2(3.8732522f, 3.5434983f),
		};
		Polygon.convexHull(poly);
		Polygon.approximateDecomposition(poly);

		//building .ini for keymap
		//temporary
//		HashMap<String, String> map = new HashMap<>();
//
//		map.put("jump", "LWJGLKeyInput:SPACE");
//		map.put("left", "LWJGLKeyInput:A");
//		map.put("right", "LWJGLKeyInput:D");
//		map.put("up", "LWJGLKeyInput:W");
//		map.put("down", "LWJGLKeyInput:S");
//		map.put("crouch", "LWJGLKeyInput:SHIFT");
//		map.put("zoom", "LWJGLMouseInput:X_SCROLL");
//		INIWriter.write(map, new File("res\\input\\player.ini"));
//
//		map.clear();
//		map.put("primary", "LWJGLMouseInput:BUTTON_0");
//		map.put("secondary", "LWJGLMouseInput:BUTTON_1");
//		map.put("x_axis", "LWJGLMouseInput:X_COORD");
//		map.put("y_axis", "LWJGLMouseInput:Y_COORD");
//		INIWriter.write(map, new File("res\\input\\ui.ini"));

		LWJGLKeyInput input = new LWJGLKeyInput();
		Binding.load(new File("res\\input\\player.ini"));
		Binding.load(new File("res\\input\\ui.ini"));

		//start the main menu activity
//		Activity.createActivity(new GUITestActivity());
//		Activity.createActivity(new MainMenuActivity());
		Activity.createActivity(new SpaceGameActivity());
	}

	public static void renderUI() {

//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(10, 0, 0);
//		GL11.glVertex2i(0, 0);
//		GL11.glColor3f(0, 10, 0);
//		GL11.glVertex2i(20, 0);
//		GL11.glColor3f(0, 0, 10);
//		GL11.glVertex2i(20, 20);
//		GL11.glEnd();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		 GL11.glShadeModel(GL11.GL_SMOOTH);

		if (Activity.currentActivity() != null) {

			renderEngine.render(Activity.currentActivity().getRenderContext());
		}

//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(1, 0, 0);
//		GL11.glVertex2i(100, 100);
//		GL11.glColor3f(0, 0, 1);
//		GL11.glVertex2i(150, 150);
//		GL11.glColor3f(0, 1, 0);
//		GL11.glVertex2i(150, 100);
//		GL11.glEnd();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	/**
	 * Calculate all the goings-on in the game
	 *
	 * @param delta
	 *            The amount of time since the last frame
	 */
	private static void update(float delta) {

		for(InputInterface input : InputInterface.getInterfaces())
			input.update(delta);

		// It might be possible to have inputs processed on it's own thread.
		if (InputContext.getCurrentContext() != null)
			InputContext.getCurrentContext().update(delta);

		if (level != null)
			level.update(delta);

		Activity.update(delta);

		ResourceManager.update(delta);
	}
}