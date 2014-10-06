package entity;

import graphics.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;


/**
 * Holds data about every entity such as the model, location, rotation, 
 * and others. It also contains a method to draw the model and update it.
 * @author Christopher Dombroski
 *
 */
public class Entity {

	public String name;

	public boolean isActive = true;
	
	// position, rotation
	public float[] rotation = new float[3];
	public float[] location = new float[3];

	public Model mdl;
	
	public enum types {
		
		WORLD,
		MOB,
		STRUCTURE,
		ITEM
	}

	types entityType;

	int VAOID;
	int VBOID;
	int NBOID;
	int CBOID;
	int VBOIID;

	boolean normals;
	boolean textures;
	boolean colors;

	public Entity(String name, types entityType, float[] location, String fileName) {
		this.name = name;
		this.entityType = entityType;
		this.location = location.clone();
		mdl.load(fileName);
		initModel();
	}
	
	public Entity(String name, types entityType, float[] location, Model model) {
		this.name = name;
		this.entityType = entityType;
		this.location = location;
		this.mdl = model;
		initModel();
	}

	/**
	 * Initialize the model.
	 */
	private void initModel() {

		VAOID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAOID);
		
		GL11.glEnable(GL11.GL_VERTEX_ARRAY);
		
		if (mdl.normalsList != null) {
			normals = true;
			GL11.glEnable(GL11.GL_NORMAL_ARRAY);
		} else {
			GL11.glDisable(GL11.GL_NORMAL_ARRAY);
		}
		if (mdl.colorAmbient != null) {
			colors = true;
			GL11.glEnable(GL11.GL_COLOR_ARRAY);
		} else {
			GL11.glDisable(GL11.GL_COLOR_ARRAY);
		}
		if (mdl.textureCoords != null) {
			textures = true;
		} else {
//			GL11.glDisable(GL11.GL_TEXTURE_ARRAY); //something like that
		}

		FloatBuffer verticesBuffer = toFloatBuffer(mdl.vertices);
		VBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		if (normals) {
			FloatBuffer normalsBuffer = toFloatBuffer(mdl.normals);
			NBOID = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, NBOID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer,
					GL15.GL_STATIC_DRAW);
			GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
		}

		if (colors) {
			FloatBuffer colorBuffer = toFloatBuffer(mdl.colorAmbient);
			CBOID = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CBOID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer,
					GL15.GL_STATIC_DRAW);
			GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
		}

		if (textures) {
			// TODO
		}

		IntBuffer indicesBuffer = toIntBuffer(mdl.indices);
		VBOIID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOIID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, indicesBuffer,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);

		GL30.glBindVertexArray(0);

	}

	/**
	 * Draws the Object using OpenGL 1.1 to OpenGL 3
	 */
	public void draw() {

		GL11.glPushMatrix();

		GL11.glTranslatef(location[0], location[1], location[2]);
//		GL11.glScalef(10, 10, 10);

		GL30.glBindVertexArray(VAOID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBOIID);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, mdl.indices.length,
				GL11.GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		GL11.glPopMatrix();
	}

	private FloatBuffer toFloatBuffer(float[] floatArray) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
		buffer.put(floatArray);
		buffer.flip();

		return buffer;
	}

	private IntBuffer toIntBuffer(int[] intArray) {

		IntBuffer buffer = BufferUtils.createIntBuffer(intArray.length);
		buffer.put(intArray);
		buffer.flip();

		return buffer;
	}

	public void update (int delta) {
		
	}

	/**
	 * Sets the location of the object
	 * @param location
	 */
	public void setLocation(float[] location) {

		this.location = location;
	}
	
	public String toString() {
		
		return "Name: \"" + name + 
				"\", Type: " + entityType + 
				", Location: " + Arrays.toString(location); 
	}
}
