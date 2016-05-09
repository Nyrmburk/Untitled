package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.imageio.ImageIO;

import main.Settings;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Provide specialty rendering such as various ui elements. Might deprecate
 * soon.
 * 
 * @author Christopher Dombroski
 */
public class Render {

	//TODO remove E V E R Y T H I N G. At least move what's useful to where it belongs.
	String version = GL11.glGetString(GL11.GL_VERSION);
	
	public static void initGL() {
		
		// init opengl
		Camera.initialize();
		Camera.perspective();
		
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		
		FloatBuffer ambient = asFloatBuffer(new float[] {0.2f, 0.2f, 0.2f, 1f});
		FloatBuffer light = asFloatBuffer(new float[] {0.5f, 0.5f, 0.5f, 1f});
		
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, light);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
				asFloatBuffer(new float[] {127, 127, 500, 1}));
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// sky blue
		// GL11.glClearColor(0.5294117647058824f, 0.807843137254902f,
		// 0.9215686274509804f, 1f);
		GL11.glClearColor(0.25f, 0.25f, 0.25f, 1f);
		GL11.glLoadIdentity();
		
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		GL11.glPointSize(3);
		
	}
	
	static FloatBuffer asFloatBuffer(float[] floatArray) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
		buffer.put(floatArray);
		buffer.flip();
		return buffer;
	}
	
	public static void draw() {
		
	}
	
//	public static void drawSelectionGrid(World game, Coord startCoord,
//			Coord endCoord) {
//		
//		Coord lowest = Coord.lowestCoord(startCoord, endCoord);
//		Coord highest = Coord.highestCoord(startCoord, endCoord);
//		
//		// change from fill polygons to draw wireframe
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//		// flat shading
//		GL11.glShadeModel(GL11.GL_FLAT);
//		// offset the line depth so it does not collide with the other polygons
//		GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
//		// set the offset distance
//		GL11.glPolygonOffset(-2f, -2f);
//		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		
//		// left vertical
//		GL11.glBegin(GL11.GL_LINE_STRIP);
//		for (int y = lowest.y; y < highest.y + 1; y++) {
//			
//			GL11.glVertex3f(lowest.x, y, ((MapMesh) Engine.worldEntity.mdl)
//					.getHeightRatio(new float[] {lowest.x, y}));
//		}
//		GL11.glEnd();
//		
//		// right vertical
//		GL11.glBegin(GL11.GL_LINE_STRIP);
//		for (int y = lowest.y; y < highest.y + 1; y++) {
//			
//			GL11.glVertex3f(highest.x, y, ((MapMesh) Engine.worldEntity.mdl)
//					.getHeightRatio(new float[] {highest.x, y}));
//		}
//		GL11.glEnd();
//		
//		// bottom horizontal
//		GL11.glBegin(GL11.GL_LINE_STRIP);
//		for (int y = lowest.x; y < highest.x + 1; y++) {
//			
//			GL11.glVertex3f(y, lowest.y, ((MapMesh) Engine.worldEntity.mdl)
//					.getHeightRatio(new float[] {y, lowest.y}));
//		}
//		GL11.glEnd();
//		
//		// top horizontal
//		GL11.glBegin(GL11.GL_LINE_STRIP);
//		for (int y = lowest.x; y < highest.x + 1; y++) {
//			
//			GL11.glVertex3f(y, highest.y, ((MapMesh) Engine.worldEntity.mdl)
//					.getHeightRatio(new float[] {y, highest.y}));
//		}
//		GL11.glEnd();
//		
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glEnable(GL11.GL_LIGHTING);
//		// disable line offset
//		GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
//		// return polygon mode to fill
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
//	}
	
//	public static void drawLocalGrid(World game, int centerX, int centerY,
//			int radius) {
//		
//		final float MID = 0.5f;
//		
//		byte[][] worldHeight = game.getWorldHeight();
//		
//		GL11.glPushMatrix();
//		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		// GL11.glLineWidth(2);
//		
//		for (int x = -radius; x <= radius + 1; x++) {
//			GL11.glBegin(GL11.GL_LINE_STRIP);
//			
//			int y = (int) Math.sqrt(((float) radius + MID)
//					* ((float) radius + MID) - ((float) x - MID)
//					* ((float) x - MID));
//			
//			for (int i = -y + 1; i <= y; i++) {
//				if (x + centerX < game.getX() && x + centerX >= 0
//						&& i + centerY < game.getY() && i + centerY >= 0) {
//					
//					GL11.glVertex3f(x + centerX - MID, i + centerY - MID,
//							worldHeight[x + centerX][i + centerY]);
//				}
//			}
//			GL11.glEnd();
//		}
//		
//		for (int y = -radius; y <= radius + 1; y++) {
//			GL11.glBegin(GL11.GL_LINE_STRIP);
//			
//			int x = (int) Math.sqrt(((float) radius + MID)
//					* ((float) radius + MID) - ((float) y - MID)
//					* ((float) y - MID));
//			
//			for (int i = -x + 1; i <= x; i++) {
//				if (y + centerY < game.getY() && y + centerY >= 0
//						&& i + centerX < game.getX() && i + centerX >= 0) {
//					
//					GL11.glVertex3f(i + centerX - MID, y + centerY - MID,
//							worldHeight[i + centerX][y + centerY]);
//				}
//			}
//			GL11.glEnd();
//		}
//		
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glPopMatrix();
//	}
	
	public static void drawActionCircle(int[] cursorStart, int cursorEnd[],
			int options) {
		
		final int degreesInCircle = 360;
		final int circlePoints = 32;
		final int innerRadius = 50;
		final int outerRadius = 100;
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBegin(GL11.GL_QUAD_STRIP);
		for (int i = 0; i <= circlePoints; i++) {
			float angle = (float) Math
					.toRadians(((float) degreesInCircle / circlePoints) * i);
			
			float inX = cursorStart[0] + innerRadius * (float) Math.cos(angle);
			float inY = cursorStart[1] + innerRadius * (float) Math.sin(angle);
			float outX = cursorStart[0] + outerRadius * (float) Math.cos(angle);
			float outY = cursorStart[1] + outerRadius * (float) Math.sin(angle);
			
			GL11.glVertex3f(inX, inY, 0);
			GL11.glVertex3f(outX, outY, 0);
		}
		GL11.glEnd();
	}
	
	public static void screenshot() {
		
		ByteBuffer array = BufferUtils.createByteBuffer(3
				* Settings.windowWidth * Settings.windowHeight);
		GL11.glReadPixels(0, 0, Settings.windowWidth, Settings.windowHeight,
				GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, array);
		
		new Thread(new ScreenshotRunnable(array.slice())).start();
	}
}

class ScreenshotRunnable implements Runnable {
	
	ByteBuffer array;
	
	ScreenshotRunnable(ByteBuffer array) {
		
		this.array = array;
	}
	
	@Override
	public void run() {
		
		BufferedImage image = new BufferedImage(Settings.windowWidth,
				Settings.windowHeight, BufferedImage.TYPE_INT_RGB);
		
		int x = 0;
		int y = Settings.windowHeight - 1;
		while (array.hasRemaining()) {
			short r = (short) (array.get() & 0xFF);
			short g = (short) (array.get() & 0xFF);
			short b = (short) (array.get() & 0xFF);
			int color = 0;
			color |= b;
			color |= g << 8;
			color |= r << 16;
			image.setRGB(x, y, color);
			x++;
			if (x == Settings.windowWidth) {
				x = 0;
				y--;
			}
		}
		
		File screenshotFolder = new File("screenshots");
		if (!screenshotFolder.exists()) screenshotFolder.mkdir();
		
		java.util.Calendar now = java.util.Calendar.getInstance();
		File file = new File("screenshots" + File.separator
				+ String.format("%04d", now.get(Calendar.YEAR)) + "-"
				+ String.format("%02d", now.get(Calendar.MONTH)) + "-"
				+ String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + "T"
				+ String.format("%02d", now.get(Calendar.HOUR)) + "."
				+ String.format("%02d", now.get(Calendar.MINUTE)) + "."
				+ String.format("%02d", now.get(Calendar.SECOND)) + ".png");
		System.out.println("screenshot: " + file);
		
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
