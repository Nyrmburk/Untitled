package ai;

public class ActionWait extends Action {
	
	private long endTime; 
	
	public ActionWait(long milliseconds) {
		
		endTime = System.currentTimeMillis() + milliseconds;
		
		priority = 0;
	}

	@Override
	public void update(int[] currentCoord) {
		
		if (System.currentTimeMillis() >= endTime) complete = true;
	}
}
