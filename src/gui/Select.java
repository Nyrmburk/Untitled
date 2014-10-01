package gui;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Select {
	
	public static float[] getCurrentCoord(int windowX, int windowY) {
		
//		System.out.println("mouse at " + windowX + ", " + windowY);
		
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
		FloatBuffer objectPosition = BufferUtils.createFloatBuffer(16);
		
		FloatBuffer z = BufferUtils.createFloatBuffer(1);
		GL11.glReadPixels(windowX, windowY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, z);
		
		GLU.gluUnProject(windowX, windowY, z.get(0), modelMatrix, projMatrix, viewport, objectPosition);
		
//		System.out.println(z.get(0));
		
		float[] coord = {
				objectPosition.get(0),
				objectPosition.get(1),
				objectPosition.get(2)};
		
//		System.out.println(Arrays.toString(coord));
		
		return coord;
	}

}
