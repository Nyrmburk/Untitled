package entity;

public interface Movable {
	
	public boolean isAllowed();
	
	public float getMoveSpeedPercent();
	
	public void pickUp();
	
	public void drop();

}
