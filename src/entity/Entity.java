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

	// position, rotation
	//TODO remove these chumps
//	private float[] rotation = new float[3];
//	private float[] location = new float[3];

	private ModelLoader model;

	private InstanceAttributes instanceAttributes = new InstanceAttributes();
	private PhysicsObject physicsObject;
	private PhysicsObjectDef physicsObjectDef;
	private Level level;

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

	public void update(int delta) {

		if (getPhysicsObject() != null) {
			Mat4 transform = instanceAttributes.getTransform();
			Vec3 pos = Transform.getPosition(transform);
			float[] newPos = getPhysicsObject().getPosition();
			pos.x = newPos[0];
			pos.y = newPos[1];
			Transform.setPosition(transform, pos);
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
			Vec3 pos = Transform.getPosition(transform);
			physicsObject.setPosition(pos.x, pos.y);
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

	public void setPhysicsObject(PhysicsObjectDef physicsObjectDef) {

		if (level != null) {
			if (this.physicsObject != null)
				level.physicsEngine.removePhysicsObject(this.physicsObject);

			this.physicsObject = level.physicsEngine.createPhysicsObject(physicsObjectDef);
		}

		this.physicsObjectDef = physicsObjectDef;
	}
}
