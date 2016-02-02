package physics;

public class PhysicsShape {
	
	float density;
	float restitiution;
	float friction;
	
	float[] position;
	float[] velocity;
	float damping;
	
	float angle;
	float angularVelocity;
	float angularDamping;
	
	public enum Type {
		STATIC,
		KINEMATIC,
		DYNAMIC
	}
	
	Type physicsType;
	
	float[] points;
	
	public PhysicsShape(Type physicsType, float[] points) {
		
		this.physicsType = physicsType;
		
		//TODO recursive splitting to convex
		
		this.points = points;
	}
	
	
	//constraints/fixtures
	
	//collision callback

}
