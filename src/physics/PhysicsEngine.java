package physics;

public interface PhysicsEngine {

	void update(float delta);
	
	PhysicsObject createPhysicsObject(PhysicsObjectDef objectDef);

	boolean removePhysicsObject(PhysicsObject object);
	
	String[] settingsNames();
	String settingOptions(int setting);
	boolean setSetting(int setting, Object option);

	PhysicsObjectDef newPhysicsObjectDef(PhysicsObject.Type physicsType);

	Body newBody();

	Sensor newSensor();
}
