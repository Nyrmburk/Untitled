package graphics;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import world.World;

public class Render {
	
	public static void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		GL11.glOrtho(0, 341, 0, 256, Camera.near, Camera.far);
		GLU.gluPerspective(Camera.fov, Camera.aspectRatio, Camera.near, Camera.far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		FloatBuffer ambient = asFloatBuffer(new float[]{0.2f, 0.2f, 0.2f, 1f});
		FloatBuffer light = asFloatBuffer(new float[]{0.5f, 0.5f, 0.5f, 1f});
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
				asFloatBuffer(new float[]{0, 0, 50, 1}));
		GL11.glLoadIdentity();
		GLU.gluLookAt(200, 200, 100, 50, 50, 0, 0, 1, 0);
		
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);//line point fill\
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
	
	public static void drawGrid(World world, int centerX, int centerY, int radius) {
		
		byte[][] worldHeight = world.getWorldHeight();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glLineWidth(2);
		
		for (int x = -radius+1; x < radius; x++) {
			GL11.glBegin(GL11.GL_LINE_STRIP);
			int y = (int) Math.sqrt(radius * radius - x * x);
			
			for (int i = -y+1; i < y; i++) {
				if (x + centerX < world.getX() && x + centerX > 0 &&
					i + centerY < world.getY() && i + centerY > 0) {
					
					GL11.glVertex3f(x + centerX, i + centerY, worldHeight[i + centerY][x + centerX]);
				}
			}
			GL11.glEnd();
		}
		
		for (int y = -radius+1; y < radius; y++) {
			GL11.glBegin(GL11.GL_LINE_STRIP);
			int x = (int) Math.sqrt(radius * radius - y * y);
			
			for (int i = -x+1; i < x; i++) {
				if (y + centerY < world.getY() && y + centerY > 0 &&
					i + centerX < world.getX() && i + centerX > 0) {
					
					GL11.glVertex3f(i + centerX, y + centerY, worldHeight[y + centerY][i + centerX]);
				}
			}
			GL11.glEnd();
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
}
