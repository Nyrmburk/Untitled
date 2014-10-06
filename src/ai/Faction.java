package ai;

public class Faction {

	private String name;
	private boolean friendly;
	
	public Faction() {
		
		this("default");
	}
	
	Faction(String name) {
		
		this(name, true);
	}
	
	Faction(String name, boolean friendly) {
		
		this.name = name;
		this.friendly = friendly;
	}
	
	public String getName() {
		
		return name;
	}
	
	public boolean isFriendly() {
		
		return friendly;
	}
}
