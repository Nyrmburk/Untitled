package main;

import activity.MainMenuActivity;
import graphics.*;
import graphics.opengl.GLErrorHelper;
import graphics.opengl.ShaderProgram;
import graphics.opengl.SimpleOpenGL3_0RenderEngine;
import input.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

import input.InputContext;
import matrix.Vec2;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import activity.Activity;
import game.Level;
import org.lwjgl.opengl.GL11;

public class Engine {

	/** Time since last frame (milliseconds) **/
	public static int delta;

	private static int highestFrame = Integer.MIN_VALUE;
	private static int lowestFrame = Integer.MAX_VALUE;
	public static int frameQuality = 0;

	/** Frames per second **/
	public static int fps;
	public static int currentFPS;
	/** FPS cap **/
	public static int setFPS = 70;
	/** Last frame's FPS **/
	private static long lastTime = 0;
	/** Last frame's creation time **/
	private static long lastFrame = 0;

	/** Whether or not the application is closing **/
	public static boolean isClosing = false;

	public static RenderEngine renderEngine;
	public static PointerInput pointer;
	public static Level level;

	/**
	 * Starts the engine.
	 *
	 * @throws IOException
	 */
	public void start() throws IOException {

		try {
			// Set everything up
			initialise();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// set lastFPS to current Time
		lastTime = getTime();

		// Display.setVSyncEnabled(true);

		// the main engine loop
		while (!Display.isCloseRequested() && !isClosing) {

			// get the change in time since the last frame
			delta = getDelta();

//			if (!Display.isActive()) {
//
//				// switch to state based game
//				Display.update();
//				Display.sync(setFPS);
//				continue;
//			}

			// update the entities and whatnot in the engine
			update(delta);

			renderEngine.start();
			// render the scene
			RenderContext renderContext = level != null ? level.getRenderContext() : null;
			if (renderContext != null)
				renderEngine.render(renderContext);

			renderUI();

			// check for graphics errors
			GLErrorHelper.checkError();

			// handle resizing the window
			if (Display.wasResized()) {

				Rectangle viewport = new Rectangle();
				Settings.window = renderEngine.getWindow();
				viewport.setSize(renderEngine.getWindow().getSize());
				renderEngine.setViewport(viewport);

				Activity activity = Activity.currentActivity();
				if (activity != null)
					activity.getView().revalidate();

//				canvas.setBounds(new Rectangle(0, 0, renderEngine.getWidth(), renderEngine.getHeight()));
			}

			// update the window with the now rendered image
			Display.update();
//			UIRenderEngine.getUIRenderContext(GUI2.getView());

			// enact the fps cap
			Display.sync(setFPS);
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
	 * @throws IOException
	 * @throws LWJGLException
	 */
	private void initialise() throws IOException, LWJGLException {

		Settings.loadSettings();
		initSystem();
		initDisplay();
		printSystem();
//		AssetManager.loadAll();
		initWorld();
	}

	/**
	 * Set up the rendering and whatnot for various operating systems
	 */
	private void initSystem() {
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
	private void initDisplay() {

		renderEngine = new SimpleOpenGL3_0RenderEngine();
//		renderEngine = new LWGJLDummy();
//		renderEngine = new SoftwareRenderEngine(new RenderContext());
		renderEngine.createWindow(Settings.window);

		lastFrame = getTime();
	}

	private void printSystem() {

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
	private void initWorld() {

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
		physics.Polygon.isWindingCCW(winding);
		physics.Polygon.reverseWinding(winding);
		physics.Polygon.isWindingCCW(winding);

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
		physics.Polygon.convexHull(poly);
		physics.Polygon.approximateDecomposition(poly);

		//building .ini for keymap
		//temporary
		HashMap<String, String> map = new HashMap<>();

		map.put("jump", "LWJGLKeyInput:SPACE");
		map.put("left", "LWJGLKeyInput:A");
		map.put("right", "LWJGLKeyInput:D");
		map.put("up", "LWJGLKeyInput:W");
		map.put("crouch", "LWJGLKeyInput:S");

		INIWriter.write(map, new File("res\\input\\player.ini"));

		map.clear();
		map.put("primary", "LWJGLMouseInput:BUTTON_0");
		map.put("x_axis", "LWJGLMouseInput:X_COORD");
		map.put("y_axis", "LWJGLMouseInput:Y_COORD");
		INIWriter.write(map, new File("res\\input\\ui.ini"));

		LWJGLKeyInput input = new LWJGLKeyInput();
		Binding.load(new File("res\\input\\player.ini"));
		Binding.load(new File("res\\input\\ui.ini"));

		//start the main menu activity
		Activity.createActivity(new MainMenuActivity());
	}

	public void renderUI() {

//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(10, 0, 0);
//		GL11.glVertex2i(0, 0);
//		GL11.glColor3f(0, 10, 0);
//		GL11.glVertex2i(20, 0);
//		GL11.glColor3f(0, 0, 10);
//		GL11.glVertex2i(20, 20);
//		GL11.glEnd();

		Camera.UI();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		 GL11.glShadeModel(GL11.GL_SMOOTH);

//		if (Activity.currentActivity() != null)
//			renderEngine.renderUI(Activity.currentActivity().getView());

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

		Camera.perspective();
//		 TEMORARY!
		Camera.look();
	}

	/**
	 * Calculate all the goings-on in the game
	 *
	 * @param delta
	 *            The amount of time since the last frame
	 */
	private void update(int delta) {

		for(InputInterface input : InputInterface.getInterfaces()) {

			input.update(delta);
		}

		InputContext.getCurrentContext().update(delta);

		// translate camera up and down (zoom)
//		int mousewheel = Mouse.getDWheel();
//		Camera.moveZ(-mousewheel / 60);
//
//		// actually translate the camera based on the previous commands
//		Camera.look();

		if (level != null)
			level.update(delta);

		Activity.update(delta);

		ResourceManager.update(delta);

		// calculate fps and stuff
		updateFPS();
	}

	/**
	 * get the time in the highest precision possible in milliseconds
	 *
	 * @return The current time
	 */
	public static long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate how much time has passed since last called
	 *
	 * @return change in time in milleconds
	 */
	private static int getDelta() {
		// Get the amount of milliseconds that has passed since the last frame.
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		if (delta == 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delta = 1;
		}

		if (delta > highestFrame)
			highestFrame = delta;
		if (delta < lowestFrame)
			lowestFrame = delta;

		// System.out.println("delta: " + delta);
		return delta;
	}

	/**
	 * Calculate the FPS
	 */
	private void updateFPS() {
		// Calculate the FPS
		if (getTime() - lastTime > 1000) {
			// System.out.println("FPS: " + fps);
			frameQuality = highestFrame - lowestFrame;
			highestFrame = Integer.MIN_VALUE;
			lowestFrame = Integer.MAX_VALUE;
			currentFPS = fps;
			fps = 0; // reset the FPS counter
			lastTime += 1000; // add 1 second
		}
		fps++;
	}
}