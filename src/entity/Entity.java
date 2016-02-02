package entity;

import graphics.ModelLoader;

import java.util.Arrays;

import main.AssetManager;
import physics.PhysicsShape;

/**
 * Holds data about every entity such as the model, location, rotation, and
 * others.
 * 
 * @author Christopher Dombroski
 *
 */
public class Entity {

	public String name;

	public boolean isActive = true;

	// position, rotation
	public float[] rotation = new float[3];
	public float[] location = new float[3];

	public ModelLoader mdl;
	public PhysicsShape shape;

	public Entity(String name, float[] location, String fileName) {
		this(name, location, AssetManager.getModel(fileName));
	}

	public Entity(String name, float[] location, ModelLoader model) {
		this.name = name;
		this.location = location;
		this.mdl = model;
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

	public String toString() {

		return "Name: \"" + name + "\", Location: " + Arrays.toString(location);
	}
}
