package physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by Nyrmburk on 2/9/2016.
 */
public class JBox2DPhysicsObjectDef implements PhysicsObjectDef {

	BodyDef bodyDef;
	FixtureDef fixtureDef;
	PolygonShape shape;
	Body body;

	PhysicsObject.Type physicsType;

	public JBox2DPhysicsObjectDef(PhysicsObject.Type physicsType, Polygon polygon) {

		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		setPhysicsType(physicsType);

		bodyDef.allowSleep = true;

		//change based on analysis of the points
		shape = new PolygonShape();

		Vec2[] vec2Vertices = polygonToVec2Array(polygon);

		shape.set(vec2Vertices, vec2Vertices.length);

		fixtureDef.setShape(shape);
	}

	public Body applyToWorld (World world) {

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		return body;
	}

	@Override
	public PhysicsObject create(PhysicsEngine physicsEngine) {

		Body body = ((JBox2DPhysicsEngine) physicsEngine).getWorld().createBody(bodyDef);

		body.createFixture(fixtureDef);

		return new JBox2DPhysicsObject(body);
	}

	public Body getBody() {

		return body;
	}

	@Override
	public PhysicsObject.Type getPhysicsType() {
		return physicsType;
	}

	public void setPhysicsType(PhysicsObject.Type physicsType) {

		this.physicsType = physicsType;

		switch (physicsType) {
			case DYNAMIC:
				bodyDef.setType(BodyType.DYNAMIC);
				break;
			case KINEMATIC:
				bodyDef.setType(BodyType.KINEMATIC);
				break;
			case STATIC:
				bodyDef.setType(BodyType.STATIC);
				break;
		}
	}

	@Override
	public float getDensity() {
		return fixtureDef.getDensity();
	}

	@Override
	public void setDensity(float density) {
		fixtureDef.setDensity(density);
	}

	@Override
	public float getRestitution() {
		return fixtureDef.getRestitution();
	}

	@Override
	public void setRestitution(float restitution) {
		fixtureDef.setRestitution(restitution);
	}

	@Override
	public float getFriction() {
		return fixtureDef.getFriction();
	}

	@Override
	public void setFriction(float friction) {
		fixtureDef.setFriction(friction);
	}

	@Override
	public float[] getPosition() {
		Vec2 position = bodyDef.getPosition();
		return new float[]{position.x, position.y};
	}

	@Override
	public void setPosition(float... position) {
		bodyDef.setPosition(new Vec2(position[0], position[1]));
	}

	@Override
	public float[] getLinearVelocity() {
		Vec2 linearVelocity = bodyDef.getLinearVelocity();
		return new float[]{linearVelocity.x, linearVelocity.y};
	}

	@Override
	public void setLinearVelocity(float... velocity) {
		bodyDef.setLinearVelocity(new Vec2(velocity[0], velocity[1]));
	}

	@Override
	public float getlinearDamping() {
		return bodyDef.getLinearDamping();
	}

	@Override
	public void setDamping(float damping) {
		bodyDef.setLinearDamping(damping);
	}

	@Override
	public float getAngle() {
		return bodyDef.getAngle();
	}

	@Override
	public void setAngle(float angle) {
		bodyDef.setAngle(angle);
	}

	@Override
	public float getAngularVelocity() {
		return bodyDef.getAngularVelocity();
	}

	@Override
	public void setAngularVelocity(float angularVelocity) {
		bodyDef.setAngularVelocity(angularVelocity);
	}

	@Override
	public float getAngularDamping() {
		return bodyDef.getAngularDamping();
	}

	@Override
	public void setAngularDamping(float angularDamping) {
		bodyDef.setAngularDamping(angularDamping);
	}

	private Vec2[] polygonToVec2Array(Polygon polygon) {

		physics.Vec2[] vec2s = polygon.getVertices();
		Vec2[] otherVec2s = new Vec2[vec2s.length];

		for (int i = 0; i < vec2s.length; i++)
			otherVec2s[i] = new Vec2(vec2s[i].getX(), vec2s[i].getY());

		return otherVec2s;
	}
}
