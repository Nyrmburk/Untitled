package physics;

import matrix.Vec3;

/**
 * Created by Nyrmburk on 2/10/2016.
 */
public interface PhysicsObject {

	enum Type {
		STATIC,
		KINEMATIC,
		DYNAMIC
	}

	void createSensor(Sensor sensor);

	void createBody(Body body);

	void applyForce(Vec3 force, Vec3 point);

	void applyForceToCenter(Vec3 force);

	void applyTorque(float torque);

	void applyLinearImpulse(Vec3 impulse, Vec3 point);

	void applyAngularImpulse(float impulse);

	Vec3 getCenterOfGravity();

	float getMass();

	Type getPhysicsType();

	void setPhysicsType(Type physicsType);

	Vec3 getPosition();

	void setPosition(Vec3 position);

	Vec3 getLinearVelocity();

	void setLinearVelocity(Vec3 velocity);

	float getlinearDamping();

	void setLinearDamping(float damping);

	float getAngle();

	void setAngle(float angle);

	float getAngularVelocity();

	void setAngularVelocity(float angularVelocity);

	float getAngularDamping();

	void setAngularDamping(float angularDamping);
}
