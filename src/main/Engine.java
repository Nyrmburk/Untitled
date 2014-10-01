package main;

import graphics.Camera;
import graphics.MapMesh;
import graphics.Render;
import gui.GUI;
import gui.Select;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.swing.UIManager;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import world.World;

public class Engine {
	
	public static int delta;
	
	public static int fps; // Actual frames per second
	public static int setFPS;
	private static long lastFPS = 0; // last frame's fps
	private static long lastFrame = 0;
	
	public static boolean closing = false;
	
	World world;
	
	public void start() throws IOException {

		try {
			initialise();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		lastFPS = getTime(); // set lastFPS to current Time

//		Manager.addEntity("test", Entity.SHAPE, new float[3], "monkey");
		
		world = new World(256, 256);
		world.loadFromImage();
		Manager.addEntity("world", Entity.SHAPE, new MapMesh(world));
		
		try {
			world.saveWorld("test");
			world.loadWorld("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (!Display.isCloseRequested()) {
			
			delta = getDelta();
			
			update(delta);
			render();
			
//			if (Display.wasResized()) {
//				Settings.windowWidth= GUI.cnvsDisplay.getWidth();
//				Settings.windowHeight = GUI.cnvsDisplay.getHeight();
//				GL11.glViewport(0, 0, Settings.windowWidth, Settings.windowHeight);
//				initGL();
//			}
			
			Display.sync(setFPS);
			Display.update();
		}
		
		close();
	}
	
	public static void close() {
		
		Display.destroy();
		System.exit(0);
	}

	private void initialise() throws IOException, LWJGLException {
		
		Settings.loadSettings();
		initSystem();
		initDisplay();
		graphics.Render.initGL();

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
	
	private void initDisplay() {
		
		try {
			GUI.initialize();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		lastFrame = getTime();
	}
	
	public void render() {
		
		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(0.8f, 0.8f, 0.8f);
		Manager.render();
		
		float[] temp = Select.getCurrentCoord(Mouse.getX(), Mouse.getY());
		int[] highlight = new int[temp.length];
		
		
		for (int i = 0; i < highlight.length; i++) {
			highlight[i] = (int) temp[i];
		}
		GL11.glColor3f(0.75f, 0.75f, 0.75f);
		Render.drawGrid(world, highlight[0], highlight[1], 10);
//		System.out.println(Arrays.toString(highlight));
		
		
		
//		Draw the triangle of death
//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(1, 0, 0);
//		GL11.glVertex3f(450, 660, 1);
//		GL11.glColor3f(0, 1, 0);
//		GL11.glVertex3f(450, 140, 1);
//		GL11.glColor3f(0, 0, 1);
//		GL11.glVertex3f(900, 400, 1);
//		GL11.glEnd();
		
	}
	
	private void update(int delta) {
		
		float scaleRatio =  (float)Math.tan(Math.toRadians(Camera.fov / 2)) * Camera.eye[2] / (Settings.windowHeight / 2);
		
		if(Mouse.isButtonDown(2)) {
			Camera.moveX(-Mouse.getDX() * scaleRatio);
			Camera.moveY(-Mouse.getDY() * scaleRatio);
		}
		
		int mousewheel = Mouse.getDWheel();
		Camera.moveZ(-mousewheel / 60);
		
		Camera.look();
		
		Manager.update(delta);
		updateFPS();
	}
	
	public static long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();

	}

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
