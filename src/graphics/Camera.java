package graphics;

import main.Settings;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Camera {
	
	public static float aspectRatio = (float)Settings.windowWidth/Settings.windowHeight;
	public static float near = 1;
	public static float far = 5000;
	public static float fov = 75;
	
	public static float[] eye = {50, 50, 100};
	public static float[] center = {50, 50, 0};
	public static final float[] UP = {0, 1, 0};
	
	public static void moveX(float f) {
		
		eye[0] += f;
		center[0] = eye[0];
	}
	
	public static void moveY(float amountY) {
		
		eye[1] += amountY;
		center[1] = eye[1] + 200;
	}
	
	public static void moveZ(float amountZ) {
		
		eye[2] += amountZ;
		center[2] = 0;
	}
	
	public static void look() {
		
		GL11.glLoadIdentity();
		GLU.gluLookAt(eye[0], eye[1], eye[2], 
				center[0], center[1], center[2], 
				UP[0], UP[1], UP[2]);
	}

}
