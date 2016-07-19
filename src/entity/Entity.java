package entity;

import graphics.InstanceAttributes;
import graphics.ModelLoader;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec3;
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

	private ModelLoader model;

	private InstanceAttributes instanceAttributes = new InstanceAttributes();
	private PhysicsObject physicsObject;
	private PhysicsObjectDef physicsObjectDef;
	private Level level;

	public Level getLevel() {

		return level;
	}

	public void setLevel(Level level) {

		if (this.level != null) {

			this.level.removeEntity(this);

			// FIXME: 5/9/2016 
//			if (model != null)
//				this.level.getRenderContext().removeInstance(model);

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

	public void update(float delta) {

		if (getPhysicsObject() != null) {
			Mat4 transform = instanceAttributes.getTransform();

			Vec3 pos = Transform.getPosition(transform);
			Transform.setPosition(transform, getPhysicsObject().getPosition());

			float rotation = getPhysicsObject().getAngle();
			Transform.rotate(transform, new Vec3(0, 0, 1), rotation);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public Mat4 getTransform() {

		return new Mat4(instanceAttributes.getTransform());
	}

	public void setTransform(Mat4 transform) {

		instanceAttributes.setTransform(transform);

		// inform physics object of change
		if (physicsObject != null) {
			physicsObject.setPosition(Transform.getPosition(transform));
		}
	}

	public ModelLoader getModel() {
		return model;
	}

	public void setModel(ModelLoader model) {

		if (level != null) {
			// FIXME: 5/9/2016 
//			if (this.model != null)
//				level.getRenderContext().removeInstance(this.model);
			level.getRenderContext().getModelGroup().addInstance(model, instanceAttributes);
		}

		this.model = model;
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}

	public PhysicsObject setPhysicsObject(PhysicsObjectDef physicsObjectDef) {

		if (level != null) {
			if (this.physicsObject != null)
				level.physicsEngine.removePhysicsObject(this.physicsObject);

			this.physicsObject = level.physicsEngine.createPhysicsObject(physicsObjectDef);
		}

		this.physicsObjectDef = physicsObjectDef;

		return physicsObject;
	}
}
