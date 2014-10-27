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

import ai.ActionMove;
import world.World;
import world.Zone;

/**
 * The main brunt of the program. I dont know what else to put here.
 * @author Christopher Dombroski
 *
 */
public class Engine {
	
	/**	Time since last frame (milliseconds)**/
	public static int delta;
	
	/**	Frames per second**/
	public static int fps;
	/**	FPS cap**/
	public static int setFPS = 0;
	/**	Last frame's FPS**/
	private static long lastFPS = 0;
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
			
//			renderUI();
			
			//check for graphics errors
//			GLErrorHelper.checkError();
			
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
		
		ShaderProgram.deleteAll();
		
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
		AssetManager.loadAll();

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
	private static void initWorld() {
		
		world = new World(256, 256);
		world.loadFromImage();
		
//		try {
//			world.saveWorld("test");
//			world.loadWorld("test");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
		
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
		Render.drawLocalGrid(world, highlight[0], highlight[1], 6);
//		System.out.println(Arrays.toString(highlight));
		
//		System.out.println(Arrays.toString(start) + ", " + Arrays.toString(end));
		
		if (Input.mouseDown[0]) {
			
			Render.drawSelectionGrid(world, start, end);
		}
		
		if (Input.keyDown.get("spawn4")) {
			
			Render.drawSelectionGrid(world, start, end);
		}
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBegin(GL11.GL_POINTS);
		for (int y = 0; y < world.getY(); y++) {
			for (int x = 0; x < world.getX(); x++) {
				int[] colorCoord = {x, y}; 
				GL11.glColor3f(world.getMovementSpeed(colorCoord), (float) world.getDesirePath(colorCoord) / 63f, 0);
				GL11.glVertex3i(x, y, world.getWorldHeight()[x][y]);
			}
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void renderUI() {
		
		Camera.UI();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
//		Draw the triangle of death
//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(1, 0, 0);
//		GL11.glVertex3f(450, 660, 1);
//		GL11.glColor3f(0, 1, 0);
//		GL11.glVertex3f(450, 140, 1);
//		GL11.glColor3f(0, 0, 1);
//		GL11.glVertex3f(900, 400, 1);
//		GL11.glEnd();
		
		Render.drawActionCircle(new int[]{Input.mouseX, Input.mouseY}, new int[2], 5);
		
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(1, 1);
		GL11.glVertex2i(1199, 1);
		GL11.glVertex2i(1199, 899);
		GL11.glVertex2i(1, 899);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		Camera.perspective();
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

	int[] start = new int[2];
	int[] end = new int[2];
	
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
		
		if (Input.mouseChanged[0] == Input.PRESSED) {
			
			start[0] = (int) Math.rint(temp[0]);
			start[1] = (int) Math.rint(temp[1]); 
		}
		if (Input.mouseDown[0]){
			
			end[0] = (int) Math.rint(temp[0]);
			end[1] = (int) Math.rint(temp[1]);
			
			Select.updateSelection(start, end);
		}
		
		int[] highlight = new int[3];
		for (int i = 0; i < temp.length; i++) {
			highlight[i] = (int) Math.rint(temp[i]);
		}
		
		if (Input.mouseChanged[1] == Input.RELEASED) {

			for (Entity entity : Manager.entityList) {
				if (entity instanceof Mob && entity instanceof Selectable) {
					if (((Selectable) entity).isSelected()) {
						
						((Mob) entity).addAction(new ActionMove(new int[]{highlight[0], highlight[1]}));
					}
				}
			}
		}
		
		if (Input.keyChanged.get("spawn0") == Input.RELEASED) {
			
			System.out.println("movementSpeed = " + world.getMovementSpeed(highlight));
		}
		
		if (Input.keyChanged.get("spawn1") == Input.RELEASED) {
			
			Manager.addEntity(new Mob("testing", temp, AssetManager.getModel("monkey.obj")));
			System.out.println(Manager.entityList.size() + " entities");
		}
		
		if (Input.keyChanged.get("spawn2") == Input.RELEASED) {
			
			Manager.addEntity(new Item("testing", Item.itemTypes.ROCK, 
					0.25f, highlight, AssetManager.getModel("rocks.obj")));
			System.out.println(Manager.entityList.size() + " entities");
		}
		
		if (Input.keyChanged.get("spawn3") == Input.RELEASED) {
			
			Manager.addEntity(new Item("testing", Item.itemTypes.ROCK, 
					highlight, AssetManager.getModel("crate.obj")));
			System.out.println(Manager.entityList.size() + " entities");
		}
		
		if (Input.keyChanged.get("spawn5") == Input.RELEASED) {
			
			Manager.addEntity(new Item("testing", Item.itemTypes.ROCK, 
					highlight, AssetManager.getModel("untitled.obj")));
			System.out.println(Manager.entityList.size() + " entities");
		}
		
		if (Input.keyChanged.get("spawn4") == Input.PRESSED) {
			
			start = highlight;
		}
		
		if (Input.keyDown.get("spawn4")) {
			
			end = highlight;
		}
		
		if (Input.keyChanged.get("spawn4") == Input.RELEASED) {
			
			Manager.addZone(new Zone(start, end));
		}
		
		if (Input.keyChanged.get("refresh") == Input.RELEASED) {
			
			System.out.println("refreshing shaders");
//			for (Shader shader : AssetManager.shaderMap.values()) {
//				shader.refresh();
//			}
			for (ShaderProgram shaderProgram : ShaderProgram.programList) {
				
				shaderProgram.rebuild();
			}
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
