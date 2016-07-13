package physics.JBox2D;

import matrix.Vec3;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import physics.PhysicsObject;
import physics.Sensor;

/**
 * Created by Nyrmburk on 2/10/2016.
 */
class JBox2DPhysicsObject implements PhysicsObject {

	private Body body;

	JBox2DPhysicsObject(Body body) {

		this.body = body;
	}

	Body getBody() {

		return body;
	}

	void createFixture() {

		body.createFixture(null);
	}

	@Override
	public void createSensor(Sensor sensor) {

		FixtureDef fixtureDef = ((JBox2DFixture) sensor).getFixtureDef();
		this.body.createFixture(fixtureDef);
	}

	@Override
	public void createBody(physics.Body body) {

		FixtureDef fixtureDef = ((JBox2DFixture) body).getFixtureDef();
		this.body.createFixture(fixtureDef);
	}

	@Override
	public void applyForce(Vec3 force, Vec3 point) {

		body.applyForce(new Vec2(force.x, force.y), new Vec2(point.x, point.y));
	}

	@Override
	public void applyForceToCenter(Vec3 force) {

		body.applyForceToCenter(new Vec2(force.x, force.y));
	}

	@Override
	public void applyTorque(float torque) {

		body.applyTorque(torque);
	}

	@Override
	public void applyLinearImpulse(Vec3 impulse, Vec3 point) {

		body.applyLinearImpulse(new Vec2(impulse.x, impulse.y), new Vec2(point.x, point.y), true);
	}

	@Override
	public void applyAngularImpulse(float impulse) {

		body.applyAngularImpulse(impulse);
	}

	@Override
	public Vec3 getCenterOfGravity() {

		Vec2 center = body.getWorldCenter();

		return new Vec3(center.x, center.y, 0);
	}

	@Override
	public float getMass() {

		return body.getMass();
	}

	@Override
	public Type getPhysicsType() {

		BodyType type = body.getType();
		PhysicsObject.Type physicsType = null;

		switch (type) {

			case STATIC:
				physicsType = Type.STATIC;
				break;
			case KINEMATIC:
				physicsType = Type.KINEMATIC;
				break;
			case DYNAMIC:
				physicsType = Type.DYNAMIC;
				break;
		}

		return physicsType;
	}

	@Override
	public void setPhysicsType(Type physicsType) {

		switch (physicsType) {

			case STATIC:
				body.setType(BodyType.STATIC);
				break;
			case KINEMATIC:
				body.setType(BodyType.KINEMATIC);
				break;
			case DYNAMIC:
				body.setType(BodyType.DYNAMIC);
				break;
		}
	}

	@Override
	public Vec3 getPosition() {

		Vec2 position = body.getPosition();
		return new Vec3(position.x, position.y, 0);
	}

	@Override
	public void setPosition(Vec3 position) {

		body.setTransform(new Vec2(position.x, position.y), body.getAngle());
	}

	@Override
	public Vec3 getLinearVelocity() {

		Vec2 velocity = body.getLinearVelocity();
		return new Vec3(velocity.x, velocity.y, 0);
	}

	@Override
	public void setLinearVelocity(Vec3 velocity) {

		body.setLinearVelocity(new Vec2(velocity.x, velocity.y));
	}

	@Override
	public float getlinearDamping() {

		return body.getLinearDamping();
	}

	@Override
	public void setLinearDamping(float damping) {

		body.setLinearDamping(damping);
	}

	@Override
	public float getAngle() {

		return body.getAngle();
	}

	@Override
	public void setAngle(float angle) {

		body.setTransform(body.getPosition(), angle);
	}

	@Override
	public float getAngularVelocity() {

		return body.getAngularVelocity();
	}

	@Override
	public void setAngularVelocity(float angularVelocity) {

		body.setAngularVelocity(angularVelocity);
	}

	@Override
	public float getAngularDamping() {

		return body.getAngularDamping();
	}

	@Override
	public void setAngularDamping(float angularDamping) {

		body.setAngularDamping(angularDamping);
	}
}
