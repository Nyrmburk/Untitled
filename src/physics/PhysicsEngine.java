package physics;

public interface PhysicsEngine {

	void update(int milliseconds);
	
	PhysicsObject createPhysicsObject(PhysicsObjectDef objectDef);

	boolean removePhysicsObject(PhysicsObject object);
	
	String[] settingsNames();
	String settingOptions(int setting);
	boolean setSetting(int setting, Object option);

	PhysicsObjectDef getPhysicsObjectDef(PhysicsObject.Type physicsType, Polygon polygon);
}
