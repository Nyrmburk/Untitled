package physics;

/**
 * Created by Nyrmburk on 2/10/2016.
 */
public interface PhysicsObject {

	enum Type {
		STATIC,
		KINEMATIC,
		DYNAMIC
	}

	void applyForce(float[] force, float[] point);

	void applyForceToCenter(float[] force);

	void applyTorque(float torque);

	void applyLinearImpulse(float[] impulse, float[] point);

	void applyAngularImpulse(float impulse);

	float[] getCenterOfGravity();

	float getMass();

	Type getPhysicsType();

	void setPhysicsType(Type physicsType);

	float getDensity();

	void setDensity(float density);

	float getRestitution();

	void setRestitution(float restitution);

	float getFriction();

	void setFriction(float friction);

	float[] getPosition();

	void setPosition(float... position);

	float[] getLinearVelocity();

	void setLinearVelocity(float... velocity);

	float getlinearDamping();

	void setLinearDamping(float damping);

	float getAngle();

	void setAngle(float angle);

	float getAngularVelocity();

	void setAngularVelocity(float angularVelocity);

	float getAngularDamping();

	void setAngularDamping(float angularDamping);
}
