package graphics;

import main.Engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;

/**
 * Add smarter camera control. Currently under development.
 * @author Christopher Dombroski
 *
 */
public class Camera {
	
	public static float aspectRatio;
	public static float near = 1f;
	public static float far = 2000;
	public static float fov = 75;

	public static float eyeSeparation = 10;
	public static float convergence = 200;

	public static float[] eye = {0, 0, 10};
	public static float[] center = {0, 0, 0};
	public static final float[] UP = {0, 1, 0};

	public static void initialize() {

		aspectRatio = 1.7777777777777777f;
	}

	public static void moveX(float f) {

		eye[0] += f;
		center[0] = eye[0];
	}

	public static void moveY(float amountY) {

		eye[1] += amountY;
		center[1] = eye[1] + 100;
//		center[1] = eye[1];
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

	public static void perspective() {

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(Camera.fov, Camera.aspectRatio, Camera.near, Camera.far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void rightEye() {

		float top, bottom, left, right;

		top = (float) (near * Math.tan(fov / 2));
		bottom = -top;

		float a = (float) (aspectRatio / 2 * Math.tan(fov / 2) * convergence);

		float b = a - eyeSeparation / 2;
		float c = a + eyeSeparation / 2;

		left = -b * near / convergence;
		right = c * near / convergence;

		// Set the Projection Matrix
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(right, left, top, bottom, near, far);

		// Displace the game to right
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		GL11.glLoadIdentity();
		GL11.glTranslatef(eyeSeparation / 2, 0.0f, 0.0f);

	}

	public static void leftEye() {

		float top, bottom, left, right;

		top = (float) (near * Math.tan(fov / 2));
		bottom = -top;

		float a = (float) (aspectRatio / 2 * Math.tan(fov / 2) * convergence);

		float b = a - eyeSeparation / 2;
		float c = a + eyeSeparation / 2;

		left = -c * near / convergence;
		right = b * near / convergence;

		// Set the Projection Matrix
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(right, left, top, bottom, near, far);

		// Displace the game to left
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		GL11.glLoadIdentity();
		GL11.glTranslatef(-eyeSeparation / 2, 0.0f, 0.0f);

	}

	public static void UI() {

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		Dimension viewport = Engine.renderEngine.getViewport().getSize();
		GL11.glOrtho(0, viewport.width, viewport.height, 0, -10, 10);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
}
