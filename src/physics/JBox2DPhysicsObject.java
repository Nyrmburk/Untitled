package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

/**
 * Created by Nyrmburk on 2/10/2016.
 */
public class JBox2DPhysicsObject implements PhysicsObject {

	Body body;

	public JBox2DPhysicsObject(Body body) {

		this.body = body;
	}

	public Body getBody() {

		return body;
	}

	@Override
	public void applyForce(float[] force, float[] point) {

		body.applyForce(new Vec2(force[0], force[1]), new Vec2(point[0], point[1]));
	}

	@Override
	public void applyForceToCenter(float[] force) {

		body.applyForceToCenter(new Vec2(force[0], force[1]));
	}

	@Override
	public void applyTorque(float torque) {

		body.applyTorque(torque);
	}

	@Override
	public void applyLinearImpulse(float[] impulse, float[] point) {

		body.applyLinearImpulse(new Vec2(impulse[0], impulse[1]), new Vec2(point[0], point[1]), true);
	}

	@Override
	public void applyAngularImpulse(float impulse) {

		body.applyAngularImpulse(impulse);
	}

	@Override
	public float[] getCenterOfGravity() {

		Vec2 center = body.getWorldCenter();

		return new float[]{center.x, center.y};
	}

	@Override
	public float getMass() {

		return body.getMass();
	}

	@Override
	public Type getPhysicsType() {

		BodyType type = body.getType();

		return null;
	}

	@Override
	public void setPhysicsType(Type physicsType) {

		body.setType(null);
	}

	@Override
	public float getDensity() {

		return body.getFixtureList().getDensity();
	}

	@Override
	public void setDensity(float density) {

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {

			if (!fixture.isSensor())
				fixture.setDensity(density);

			fixture = fixture.getNext();
		}

		body.resetMassData();
	}

	@Override
	public float getRestitution() {
		return body.getFixtureList().getRestitution();
	}

	@Override
	public void setRestitution(float restitution) {

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {

			if (!fixture.isSensor())
				fixture.setRestitution(restitution);

			fixture = fixture.getNext();
		}
	}

	@Override
	public float getFriction() {
		return body.getFixtureList().getFriction();
	}

	@Override
	public void setFriction(float friction) {

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {

			if (!fixture.isSensor())
				fixture.setFriction(friction);

			fixture = fixture.getNext();
		}
	}

	@Override
	public float[] getPosition() {

		Vec2 position = body.getPosition();
		return new float[]{position.x, position.y};
	}

	@Override
	public void setPosition(float... position) {

		body.setTransform(new Vec2(position[0], position[1]), body.getAngle());
	}

	@Override
	public float[] getLinearVelocity() {

		Vec2 velocity = body.getLinearVelocity();
		return new float[]{velocity.x, velocity.y};
	}

	@Override
	public void setLinearVelocity(float... velocity) {

		body.setLinearVelocity(new Vec2(velocity[0], velocity[1]));
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
