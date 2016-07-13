package physics.JBox2D;

import matrix.Vec3;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import physics.*;

/**
 * Created by Nyrmburk on 2/9/2016.
 */
public class JBox2DPhysicsObjectDef implements PhysicsObjectDef {

	BodyDef bodyDef;
	PolygonShape shape;
	physics.Body body;

	PhysicsObject.Type physicsType;

	JBox2DPhysicsObjectDef(PhysicsObject.Type physicsType) {

		bodyDef = new BodyDef();
		setPhysicsType(physicsType);
//		fixtureDef = new FixtureDef();

		bodyDef.allowSleep = true;

//		//change based on analysis of the points
//		shape = new PolygonShape();
//
//		Vec2[] vec2Vertices = vec2ArrayConvert(polygon);
//
//		shape.set(vec2Vertices, vec2Vertices.length);
//
//		fixtureDef.setShape(shape);
	}

	org.jbox2d.dynamics.Body applyToWorld (World world) {

		org.jbox2d.dynamics.Body body = world.createBody(bodyDef);
//		body.createFixture(fixtureDef);

		return body;
	}

	@Override
	public PhysicsObject create(PhysicsEngine physicsEngine) {

		org.jbox2d.dynamics.Body body = ((JBox2DPhysicsEngine) physicsEngine).getWorld().createBody(bodyDef);

//		body.createFixture(fixtureDef);

		return new JBox2DPhysicsObject(body);
	}

	public physics.Body getBody() {

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
	public Vec3 getPosition() {
		Vec2 position = bodyDef.getPosition();
		return new Vec3(position.x, position.y, 0);
	}

	@Override
	public void setPosition(Vec3 position) {
		bodyDef.setPosition(new Vec2(position.x, position.y));
	}

	@Override
	public Vec3 getLinearVelocity() {
		Vec2 linearVelocity = bodyDef.getLinearVelocity();
		return new Vec3(linearVelocity.x, linearVelocity.y, 0);
	}

	@Override
	public void setLinearVelocity(Vec3 velocity) {
		bodyDef.setLinearVelocity(new Vec2(velocity.x, velocity.y));
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

	private Vec2[] vec2ArrayConvert(Vec2[] vec2s) {

		Vec2[] otherVec2s = new Vec2[vec2s.length];

		for (int i = 0; i < vec2s.length; i++)
			otherVec2s[i] = new Vec2(vec2s[i].x, vec2s[i].y);

		return otherVec2s;
	}
}
