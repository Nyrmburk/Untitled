package physics;

import matrix.Vec3;

public interface PhysicsObjectDef {

	//constraints/fixtures

	//collision callback

	PhysicsObject create(PhysicsEngine physicsEngine);

	PhysicsObject.Type getPhysicsType();

	void setPhysicsType(PhysicsObject.Type physicsType);

	Vec3 getPosition();

	void setPosition(Vec3 position);

	Vec3 getLinearVelocity();

	void setLinearVelocity(Vec3 velocity);

	float getlinearDamping();

	void setDamping(float damping);

	float getAngle();

	void setAngle(float angle);

	float getAngularVelocity();

	void setAngularVelocity(float angularVelocity);

	float getAngularDamping();

	void setAngularDamping(float angularDamping);
}
