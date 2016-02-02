package physics;

public interface PhysicsEngine {

	public void update(int milliseconds);
	
	public void addShape(PhysicsShape shape);
	public void removeShape(PhysicsShape shape);
	
	public String[] settingsNames();
	public String settingOptions(int setting);
	public boolean setSetting(int setting, Object option);
}
