package main;

import entity.Entity;
import graphics.*;
import input.*;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import activity.Activity;
import activity.MainMenuActivity;
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
	public static UIRenderEngine UIRenderEngine;
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
				activity.setView(activity.getView());
				//GUI2.setView(GUI2.getView());
//				GUI.revalidate();
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
		UIRenderEngine = new SimpleUIRenderEngine();

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

		Activity.createActivity(new MainMenuActivity());
	}

	public void renderUI() {

		Camera.UI();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 1);

		// GL11.glShadeModel(GL11.GL_SMOOTH);

//		 Draw the triangle of death
//		 GL11.glBegin(GL11.GL_TRIANGLES);
//		 GL11.glColor3f(1, 0, 0);
//		 GL11.glVertex3f(450, 660, 1);
//		 GL11.glColor3f(0, 1, 0);
//		 GL11.glVertex3f(450, 140, 1);
//		 GL11.glColor3f(0, 0, 1);
//		 GL11.glVertex3f(900, 400, 1);
//		 GL11.glEnd();

		// Render.drawActionCircle(new int[] {Input.mouseX, Input.mouseY},
		// new int[2], 5);
		//
		// GL11.glColor3f(1, 1, 1);
		// GL11.glBegin(GL11.GL_LINE_LOOP);
		// GL11.glVertex2i(1, 1);
		// GL11.glVertex2i(1199, 1);
		// GL11.glVertex2i(1199, 899);
		// GL11.glVertex2i(1, 899);
		// GL11.glEnd();

//		GUI.update();
//		GUI.revalidate();
//		GUI.render();
//		GUI2.update();

		renderEngine.renderUI(Activity.currentActivity().getRenderContext());

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

		for(InputInterface input : InputInterface.inputInterfaces) {

			input.update(delta);
		}

		for (Input input : InputContext.getCurrentContext().inputs)
			input.onUpdate(delta);

		Activity.update(delta);

		// translate camera up and down (zoom)
		int mousewheel = Mouse.getDWheel();
		Camera.moveZ(-mousewheel / 60);

		// actually translate the camera based on the previous commands
		Camera.look();

		if (level != null) {

			level.physicsEngine.update(delta);
//			float[] coords = level.test.getPosition();
//			System.out.println(coords[0] + ", " + coords[1]);
		}
		Entity.update(delta);

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