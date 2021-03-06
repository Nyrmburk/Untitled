package entity;

import graphics.InstanceAttributes;
import graphics.ModelLoader;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec3;
import game.Level;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import physics.JBox2D;
import tool.Selectable;
import tool.Selection;

/**
 * The binding glue of the different observable componentLists. It binds orientation to the physics and graphics.
 * @author Christopher Dombroski
 *
 */
public class Entity implements Selectable {

	private boolean isActive = true;

	private ModelLoader model;

	private InstanceAttributes instanceAttributes = new InstanceAttributes();
	private Body physicsObject;
	private BodyDef physicsObjectDef;
	private Level level;

	private int layer = 0;
	private int thickness = 1;

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
				this.level.physicsEngine.destroyBody(physicsObject);
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
			Transform.setPosition(transform, JBox2D.convert(
					getPhysicsObject().getPosition()).asVec3(-layer));

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
			physicsObject.setTransform(JBox2D.convert(
					Transform.getPosition(transform)), physicsObject.getAngle());
		}
	}

	public InstanceAttributes getInstanceAttributes() {
		return instanceAttributes;
	}

	public ModelLoader getModel() {
		return model;
	}

	public void setModel(ModelLoader model) {

		if (level != null) {
			if (this.model != null)
				level.getRenderContext().getModelGroup().removeInstance(this.model, instanceAttributes);
			level.getRenderContext().getModelGroup().addInstance(model, instanceAttributes);
		}

		this.model = model;
	}

	public Body getPhysicsObject() {
		return physicsObject;
	}

	public Body setPhysicsObject(BodyDef physicsObjectDef) {

		if (level != null) {
			if (this.physicsObject != null)
				level.physicsEngine.destroyBody(this.physicsObject);

			this.physicsObject = level.physicsEngine.createBody(physicsObjectDef);
		}

		this.physicsObjectDef = physicsObjectDef;

		physicsObject.setUserData(this);

		setFilter();

		return physicsObject;
	}

	public int getLayer() {
		return layer;
	}

	public int getThickness() {
		return thickness;
	}

	public void setLayer(int layer, int thickness) {

		if (thickness < 1)
			thickness = 1;
		if (thickness > level.getLayerCount())
			thickness = level.getLayerCount();

		if (layer < 0)
			layer = 0;

		if (layer >= level.getLayerCount())
			layer = level.getLayerCount() - thickness;

		if (layer + thickness > level.getLayerCount())
			thickness = level.getLayerCount() - layer;

		this.layer = layer;
		this.thickness = thickness;
		setFilter();
	}

	private void setFilter() {

		if (physicsObject == null)
			return;

		Filter filter = new Filter();

		// This here bit twiddling first fills lowest bits with 1s for each layer of thickness.
		// (1 << getThickness()) - 1 ...
		// After that it shifts it over to the layer it starts on.
		// (...) << getLayer()
		filter.categoryBits = ((1 << getThickness()) - 1) << getLayer();
		filter.maskBits = filter.categoryBits;

		Fixture fixture = physicsObject.getFixtureList();

		while (fixture != null) {

			fixture.setFilterData(filter);
			fixture = fixture.getNext();
		}
	}
}
