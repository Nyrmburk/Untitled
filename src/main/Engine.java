package main;

import entity.*;
import graphics.*;
import gui.*;

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import world.World;

/**
 * The main brunt of the program. I dont know what else to put here.
 * @author Christopher Dombroski
 *
 */
public class Engine {
	
	/**	Time since last frame (milliseconds)**/
	public static int delta;
	
	/**	Frames per second**/
	public static int fps; // Actual frames per second
	/**	FPS cap**/
	public static int setFPS;
	/**	Last frame's FPS**/
	private static long lastFPS = 0; // last frame's fps
	/**	Last frame's creation time**/
	private static long lastFrame = 0;
	
	/**	Whether or not the application is closing**/
	public static boolean closing = false;
	
	/**	The world data**/
	public static World world;
	/**	The drawable, mesh-bearing entity that is the map**/
	public static Entity worldEntity;
	
	/**
	 * Starts the engine.
	 * @throws IOException
	 */
	public void start() throws IOException {

		try {
			//Set everything up
			initialise();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// set lastFPS to current Time
		lastFPS = getTime(); 
		
		//the main engine loop
		while (!Display.isCloseRequested()) {
			
			//get the change in time since the last frame
			delta = getDelta();
			
			//update the entities and whatnot in the engine
			update(delta);
			
			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			//render the scene
			if (Settings.threeD) {
				render3D();
			} else {
				render();
			}
			
//			if (Display.wasResized()) {
//				Settings.windowWidth= GUI.cnvsDisplay.getWidth();
//				Settings.windowHeight = GUI.cnvsDisplay.getHeight();
//				GL11.glViewport(0, 0, Settings.windowWidth, Settings.windowHeight);
//				initGL();
//			}
			
			//enact the fps cap
			Display.sync(setFPS);
			//update the window with the now rendered image
			Display.update();
		}
		
		close();
	}
	
	/**
	 * Safely close down the engine 
	 */
	public static void close() {
		
		// dispose of the graphics
		Display.destroy();
		//shut down the jvm
		System.exit(0);
	}

	/**
	 * Initialize everything
	 * @throws IOException
	 * @throws LWJGLException
	 */
	private void initialise() throws IOException, LWJGLException {
		
		Settings.loadSettings();
		initSystem();
		initDisplay();
		initWorld();
		graphics.Render.initGL();
		Input.load();

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
		
		String natives = System.getProperty("user.dir") + 
				File.separator + "lib" + 
				File.separator + "natives-" + os;
		System.setProperty("org.lwjgl.librarypath", natives);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to load default look and feel");
		}
		
//		Dir.initPaths();
//		
//		lastFrame = getTime();
		
	}
	
	/**
	 * Builds the window and creates opengl context
	 */
	private void initDisplay() {
		
		try {
			GUI.initialize();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		lastFrame = getTime();
	}
	
	/**
	 * Load the world
	 */
	public static void initWorld() {
		
		world = new World(256, 256);
		world.loadFromImage();
		
		try {
			world.saveWorld("test");
			world.loadWorld("test");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		worldEntity = new Entity("world", Entity.types.WORLD, new float[3], new MapMesh(world));
	}
	
	/**
	 * Render the scene using openGL
	 */
	public void render() {
		
		//smooth shading
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		//render
		worldEntity.draw();
		
		//change color to 20% white
		GL11.glColor3f(0.2f, 0.2f, 0.2f);
		//change from fill polygons to draw wireframe
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
		//flat shading
		GL11.glShadeModel(GL11.GL_FLAT);
		//offset the line depth so it does not collide with the other polygons
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
		//set the offset distance
		GL11.glPolygonOffset( -1f, -1f );
		
		//render wireframe
		worldEntity.draw();
		
		//disable line offset
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
		//return polygon mode to fill
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
		//set the color to 80% white
		GL11.glColor3f(0.8f, 0.8f, 0.8f);
		
		//get mouse cursor in worldspace
		float[] temp = Select.getCurrentCoord(Mouse.getX(), Mouse.getY());
		int[] highlight = new int[temp.length];
		
		//render the entities
		Manager.render();
		
		//change the cursor from a float to int
		for (int i = 0; i < highlight.length; i++) {
			highlight[i] = (int) Math.rint(temp[i]);
		}
		
		//set the color to 75% white
		GL11.glColor3f(0.75f, 0.75f, 0.75f);
		//render the selection grid
		Render.drawGrid(world, highlight[0], highlight[1], 6);
//		System.out.println(Arrays.toString(highlight));
		
	}
	
	/**
	 * Render the scene in 3d using crossview
	 */
	public void render3D() {
		
		//render to only the left side of the screen
		GL11.glViewport(0, 0, Settings.windowWidth / 2, Settings.windowHeight);
		//set up the right eye fustrum
		Camera.rightEye();
		//render eye
		render();
		//render to only the right side of the screen
		GL11.glViewport(Settings.windowWidth / 2, 0, Settings.windowWidth / 2, Settings.windowHeight);
		//set up the right eye fustrum
		Camera.leftEye();
		//render eye
		render();
		
	}

	/**
	 * Calculate all the goings-on in the world
	 * @param delta The amout of time since the last frame
	 */
	private void update(int delta) {
		
		//get the cursor location in world space
		float[] temp = Select.getCurrentCoord(Mouse.getX(), Mouse.getY());
		//get the amount to scale by for mouse movement
		float scaleRatio =  (float)Math.tan(Math.toRadians(Camera.fov / 2)) * (Camera.eye[2] - temp[2]) / (Settings.windowHeight / 2);
		//get the amount to scale by for key movement
		float keyScaleRatio =  (float)Math.tan(Math.toRadians(Camera.fov / 2)) * Camera.eye[2] / (Settings.windowHeight / 2);
		
		//if middle-click, then translate the scene
		if(Mouse.isButtonDown(2)) {
			Camera.moveX(-Mouse.getDX() * scaleRatio);
			Camera.moveY(-Mouse.getDY() * scaleRatio);
		}
		
		//poll the inputs
		Input.refresh();
		
		//move the camera from keystrokes
		if(Input.keyDown.get("forward")) {
			Camera.moveY((float)delta/5 * keyScaleRatio);
		}
		if (Input.keyDown.get("backward")) {
			Camera.moveY(-(float)delta/5 * keyScaleRatio);
		}
		if(Input.keyDown.get("right")) {
			Camera.moveX((float)delta/5 * keyScaleRatio);
		}
		if (Input.keyDown.get("left")) {
			Camera.moveX(-(float)delta/5 * keyScaleRatio);
		}
		
		//add entity on release of left-click
		if (Input.mouseChanged[0] == Input.RELEASED) {
			Manager.addEntity(new Mob("testing", temp, new Model("icosahedron")));
		}
		
		//translate camera up and down (zoom)
		int mousewheel = Mouse.getDWheel();
		Camera.moveZ(-mousewheel / 60);
		
		//actually translate the camera based on the previous commands
		Camera.look();
		
		//update the entity list
		Manager.update(delta);
		
		//calculate fps and stuff
		updateFPS();
	}
	
	/**
	 * get the time in the highest precision possible in milliseconds
	 * @return The current time
	 */
	public static long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate how much time has passed since last called
	 * @return change in time in milleconds
	 */
	public static int getDelta() {
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

		// System.out.println("delta: " + delta);
		return delta;

	}

	/**
	 * Calculate the FPS
	 */
	public void updateFPS() {
		// Calculate the FPS
		if (getTime() - lastFPS > 1000) {
			System.out.println("FPS: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add 1 second
		}
		fps++;
	}

}
