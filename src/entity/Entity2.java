package entity;

import graphics.InstanceAttributes;
import graphics.ModelLoader;
import main.Engine;
import physics.PhysicsShape;

/**
 * The binding glue of the different observable components. It binds orientation to the physics and graphics.
 * @author Christopher Dombroski
 *
 */
public class Entity2 {

	private String name;

	private boolean isActive = true;

	// position, rotation
	private float[] rotation = new float[3];
	private float[] location = new float[3];

	private ModelLoader model;
	private PhysicsShape shape;

	public Entity2(String name) {
		this.setName(name);
	}

	public void update(int delta) {

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
		this.rotation = rotation;
	}

	public float[] getLocation() {
		return location;
	}

	public void setLocation(float[] location) {
		this.location = location;
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

	public PhysicsShape getShape() {
		return shape;
	}

	public void setShape(PhysicsShape shape) {

		if (this.shape != null)
			Engine.level.physicsEngine.removeShape(this.shape);
		Engine.level.physicsEngine.addShape(shape);
		this.shape = shape;
	}
}
