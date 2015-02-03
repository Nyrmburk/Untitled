package entity;

import graphics.Drawable;
import graphics.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.AssetManager;
import world.Coord;

/**
 * Holds data about every entity such as the model, location, rotation, and
 * others. It also contains a method to draw the model and update it.
 * 
 * @author Christopher Dombroski
 *
 */
public class Entity implements Drawable {

	public String name;

	public boolean isActive = true;
	private Selectable selectable;

	// position, rotation
	public float[] rotation = new float[3];
	public float[] location = new float[3];

	public Model mdl;

	public enum types {

		WORLD, MOB, STRUCTURE, ITEM
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

	public Entity(String name, types entityType, float[] location,
			String fileName) {
		this.name = name;
		this.entityType = entityType;
		this.location = location.clone();
		mdl = AssetManager.getModel(fileName);
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
			// GL11.glDisable(GL11.GL_TEXTURE_ARRAY); //something like that
		}

		FloatBuffer verticesBuffer = toFloatBuffer(mdl.vertices);
		VBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);
//		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

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
		
		// Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

		IntBuffer indicesBuffer = toIntBuffer(mdl.indices);
		VBOIID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBOIID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL30.glBindVertexArray(0);

//		GL30.glBindVertexArray(0);

	}

	/**
	 * Draws the Object using OpenGL 1.1 to OpenGL 3
	 */
	public void draw() {

		GL11.glPushMatrix();

		GL11.glTranslatef(location[0], location[1], location[2]);
		GL11.glRotatef(rotation[0], 1, 0, 0);
		GL11.glRotatef(rotation[1], 0, 1, 0);
		GL11.glRotatef(rotation[2], 0, 0, 1);

		GL30.glBindVertexArray(VAOID);
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBOIID);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, mdl.indices.length,
				GL11.GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL20.glDisableVertexAttribArray(3);
//		GL20.glDisableVertexAttribArray(2);
//		GL20.glDisableVertexAttribArray(1);
//		GL20.glDisableVertexAttribArray(0);
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

	public void update(int delta) {

	}

	/**
	 * Sets the location of the object
	 * 
	 * @param location
	 */
	public void setLocation(float[] location) {

		this.location = location;
	}

	public void proccessSelectable(Coord startCoord, Coord endCoord) {
		if (selectable == null) {
			return;
		}
		boolean selected = false;
		
		if (checkSelected(startCoord, endCoord, Coord.locationToCoord(location)))
			selected = true;
		
		selectable.selected(selected);
	}

	public void addSelectable(Selectable selectable) {

		this.selectable = selectable;
	}

	private static boolean checkSelected(Coord startCoord, Coord endCoord,
			Coord currentCoord) {

		boolean selected = false;
		Coord lowestCoord = Coord.lowestCoord(startCoord, endCoord);
		Coord highestCoord = Coord.highestCoord(startCoord, endCoord);

		if (lowestCoord.x <= currentCoord.x && currentCoord.x <= highestCoord.x
				&& lowestCoord.y < currentCoord.y
				&& currentCoord.y < highestCoord.y) {

			selected = true;
		}

		return selected;
	}

	public String toString() {

		return "Name: \"" + name + "\", Type: " + entityType + ", Location: "
				+ Arrays.toString(location);
	}
}
