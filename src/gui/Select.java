package gui;

import java.nio.*;

import main.Manager;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import entity.Entity;
import entity.Selectable;

/**
 * Manage the selection of entities.
 * @author Christopher Dombroski
 *
 */
public class Select {

	public static float[] getCurrentCoord(int windowX, int windowY) {

		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
		FloatBuffer objectPosition = BufferUtils.createFloatBuffer(16);

		FloatBuffer z = BufferUtils.createFloatBuffer(1);
		GL11.glReadPixels(windowX, windowY, 1, 1, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, z);

		GLU.gluUnProject(windowX, windowY, z.get(0), modelMatrix, projMatrix,
				viewport, objectPosition);

		float[] coord = { objectPosition.get(0), objectPosition.get(1),
				objectPosition.get(2) };

		return coord;
	}
	
	public static void updateSelection(int[] start, int[] end) {
		
		for (Entity entity : Manager.entityList) {
			
			if (entity instanceof Selectable) {
				
				((Selectable) entity).checkSelected(start, end);
			}
		}
	}

}
