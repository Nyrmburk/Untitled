package physics;

import matrix.Vec2;

public interface PhysicsEngine {

	void update(int milliseconds);
	
	PhysicsObject createPhysicsObject(PhysicsObjectDef objectDef);

	boolean removePhysicsObject(PhysicsObject object);
	
	String[] settingsNames();
	String settingOptions(int setting);
	boolean setSetting(int setting, Object option);

	PhysicsObjectDef newPhysicsObjectDef(PhysicsObject.Type physicsType);

	Body newBody();

	Sensor newSensor();

	Shape2 newShape2(Shape2.ShapeType shapeType, Vec2[] vertices);
}
