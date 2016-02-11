package physics;

import java.awt.*;

public interface PhysicsObjectDef {

	//constraints/fixtures

	//collision callback

	PhysicsObject create(PhysicsEngine physicsEngine);

	PhysicsObject.Type getPhysicsType();

	void setPhysicsType(PhysicsObject.Type physicsType);

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

	void setDamping(float damping);

	float getAngle();

	void setAngle(float angle);

	float getAngularVelocity();

	void setAngularVelocity(float angularVelocity);

	float getAngularDamping();

	void setAngularDamping(float angularDamping);
}
