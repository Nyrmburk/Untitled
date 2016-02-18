package entity;

import graphics.InstanceAttributes;
import graphics.ModelLoader;
import main.Engine;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;

import java.util.ArrayList;

/**
 * The binding glue of the different observable components. It binds orientation to the physics and graphics.
 * @author Christopher Dombroski
 *
 */
public class Entity {

	private static ArrayList<Entity> entities = new ArrayList<Entity>();

	private String name;

	private boolean isActive = true;

	// position, rotation
	private float[] rotation = new float[3];
	private float[] location = new float[3];

	private ModelLoader model;
	private PhysicsObject physicsObject;

	public Entity(String name) {

		this.setName(name);
		entities.add(this);
	}

	public static void update(int delta) {

		for (Entity entity : entities) {

			entity.setLocation(entity.getPhysicsObject().getPosition());
//			entity.onUpdate(delta);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public float[] getRotation() {
		return rotation;
	}

	public void setRotation(float[] rotation) {

		for (int i = 0; i < location.length; i++) {

			this.rotation[i] = rotation[i];
		}
	}

	public float[] getLocation() {
		return location;
	}

	public void setLocation(float[] location) {

		for (int i = 0; i < location.length; i++) {

			this.location[i] = location[i];
		}
	}

	public ModelLoader getModel() {
		return model;
	}

	public void setModel(ModelLoader model) {

		if (this.model != null)
			Engine.renderEngine.removeModel(this.model);
		Engine.renderEngine.addModel(model, new InstanceAttributes(location, rotation));
		this.model = model;
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}

	public void setPhysicsObject(PhysicsObjectDef physicsObjectDef) {

		if (this.physicsObject != null)
			Engine.level.physicsEngine.removePhysicsObject(this.physicsObject);
		this.physicsObject = Engine.level.physicsEngine.createPhysicsObject(physicsObjectDef);
	}
}
