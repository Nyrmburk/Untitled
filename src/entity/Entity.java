package entity;

import graphics.InstanceAttributes;
import graphics.ModelLoader;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;
import game.Level;

/**
 * The binding glue of the different observable components. It binds orientation to the physics and graphics.
 * @author Christopher Dombroski
 *
 */
public class Entity {

	private boolean isActive = true;

	// position, rotation
	private float[] rotation = new float[3];
	private float[] location = new float[3];

	private ModelLoader model;
	private PhysicsObject physicsObject;
	private PhysicsObjectDef physicsObjectDef;
	private Level level;

	public void setLevel(Level level) {

		if (this.level != null) {

			this.level.removeEntity(this);

			if (model != null)
				this.level.getRenderContext().removeModel(model);

			if (physicsObject != null)
				this.level.physicsEngine.removePhysicsObject(physicsObject);
		}

		this.level = level;
		this.level.addEntity(this);

		if (model != null)
			setModel(model);

		if (physicsObjectDef != null)
			setPhysicsObject(physicsObjectDef);
	}

	public void update(int delta) {

		if (getPhysicsObject() != null)
			setLocation(getPhysicsObject().getPosition());
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

		if (physicsObject != null)
			physicsObject.setPosition(this.location);
	}

	public ModelLoader getModel() {
		return model;
	}

	public void setModel(ModelLoader model) {

		if (level != null) {
			if (this.model != null)
				level.getRenderContext().removeModel(this.model);
			level.getRenderContext().addModel(model, new InstanceAttributes(location, rotation));
		}

		this.model = model;
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}

	public void setPhysicsObject(PhysicsObjectDef physicsObjectDef) {

		if (level != null) {
			if (this.physicsObject != null)
				level.physicsEngine.removePhysicsObject(this.physicsObject);

			this.physicsObject = level.physicsEngine.createPhysicsObject(physicsObjectDef);
		}

		this.physicsObjectDef = physicsObjectDef;
	}
}
