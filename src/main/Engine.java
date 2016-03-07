package main;

import activity.CSSActivity;
import activity.MainMenuActivity;
import graphics.*;
import gui.GUIAppearance;
import gui.css.CSSCanvas;
import gui.css.CSSComposite;
import gui.css.CSSDocument;
import input.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import activity.Activity;
import world.Level;

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

			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			// render the scene
			renderEngine.render();

			renderUI();

			// check for graphics errors
			GLErrorHelper.checkError();

			// handle resizing the window
			if (Display.wasResized()) {

				Settings.windowWidth = renderEngine.getWidth();
				Settings.windowHeight = renderEngine.getHeight();
				GL11.glViewport(0, 0, renderEngine.getWidth(),
						 renderEngine.getHeight());

				graphics.Render.initGL();

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
		graphics.Render.initGL();
		AssetManager.loadAll();
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

		renderEngine = new SimpleOpenGL3_0RenderEngine(new RenderContext());
//		renderEngine = new SoftwareRenderEngine(new RenderContext());
		renderEngine.showWindow(Settings.windowWidth, Settings.windowHeight);

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
	 * Load the world
	 */
	private void initWorld() {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("C:\\Users\\Nyrmburk\\Documents\\GitHub\\untitled\\dev\\plain.9.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new NinePatch(image);

//		System.out.println("start");
//		org.fit.cssbox.demo.TextBoxes.main(new String[]{"file:///C:\\Users\\Nyrmburk\\Documents\\GitHub\\untitled\\dev\\Motherfucking Website.html"});
//		System.out.println("end");

//		new GraphicsFont(new Font("Roboto", Font.ITALIC, 72));

//		CSSDocument doc = new CSSDocument();
//		try {
//			doc.load(new File("C:\\Users\\Nyrmburk\\Documents\\GitHub\\untitled\\dev\\ui\\mainmenu.html"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//		long time = System.nanoTime();
//		canvas = new CSSCanvas(doc, new Rectangle(0, 0, Settings.windowWidth, Settings.windowHeight));
//		System.out.println(((float) System.nanoTime() - time) / 1000000);

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

//		LWJGLKeyInput keyInput = new LWJGLKeyInput();
//		keyInput.addInput(new Input("one") {
//			@Override
//			public void onUpdate(int delta) {
//
//			}
//		});
//
//		keyInput.addInput(new Input("two") {
//			@Override
//			public void onUpdate(int delta) {
//
//			}
//		});

//		level = new Level();
//		renderEngine.setContext(level.getRenderContext());

		Activity.createActivity(new MainMenuActivity());
//		long time = System.nanoTime();
//		Activity.createActivity(new CSSActivity(canvas));
//		System.out.println(((float) System.nanoTime() - time) / 1000000);
//		canvas.setBounds(new Dimension(100, 100));
	}

	private CSSCanvas canvas;
	public void renderUI() {

		Camera.UI();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		 GL11.glShadeModel(GL11.GL_SMOOTH);

//		 Draw the triangle of death
//		 GL11.glBegin(GL11.GL_TRIANGLES);
//		 GL11.glColor3f(1, 0, 0);
//		 GL11.glVertex2f(450, 660);
//		 GL11.glColor3f(0, 1, 0);
//		 GL11.glVertex2f(450, 140);
//		 GL11.glColor3f(0, 0, 1);
//		 GL11.glVertex2f(900, 400);
//		 GL11.glEnd();

		// Render.drawActionCircle(new int[] {Input.mouseX, Input.mouseY},
//		 new int[2], 5);

//		 GL11.glColor3f(1, 1, 1);
//		 GL11.glBegin(GL11.GL_LINE_LOOP);
//		 GL11.glVertex2i(1, 1);
//		 GL11.glVertex2i(1199, 1);
//		 GL11.glVertex2i(1199, 899);
//		 GL11.glVertex2i(1, 899);
//		 GL11.glEnd();

//		GUI.update();
//		GUI.revalidate();
//		GUI.render();
//		GUI2.update();

		if (Activity.currentActivity() != null)
			renderEngine.renderUI(Activity.currentActivity().getView());

//		((SimpleOpenGL3_0RenderEngine) renderEngine).renderUI2(canvas.getComposite());

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		Camera.perspective();
		// TEMORARY!
		Camera.look();
	}

	/**
	 * Render the scene in 3d using crossview
	 */
	public void render3D() {

		// render to only the left side of the screen
		GL11.glViewport(0, 0, Settings.windowWidth / 2, Settings.windowHeight);
		// set up the right eye fustrum
		Camera.rightEye();
		// render eye
//		render();
		// render to only the right side of the screen
		GL11.glViewport(Settings.windowWidth / 2, 0, Settings.windowWidth / 2,
				Settings.windowHeight);
		// set up the right eye fustrum
		Camera.leftEye();
		// render eye
//		render();

	}

	/**
	 * Calculate all the goings-on in the world
	 *
	 * @param delta
	 *            The amount of time since the last frame
	 */
	private void update(int delta) {

		for(InputInterface input : InputInterface.getInterfaces()) {

			input.update(delta);
		}

		InputContext.getCurrentContext().update(delta);

		Activity.update(delta);

		// translate camera up and down (zoom)
		int mousewheel = Mouse.getDWheel();
		Camera.moveZ(-mousewheel / 60);

		// actually translate the camera based on the previous commands
		Camera.look();

		if (level != null)
			level.update(delta);

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