package ai;

/**
 * Make the AI wait for a designated amount of time. It is primarily called by 
 * ActionWander.
 * @author Christopher Dombroski
 *
 */
public class ActionWait extends Action {
	
	private long endTime; 
	
	public ActionWait(long milliseconds) {
		
		endTime = System.currentTimeMillis() + milliseconds;
		
		priority = 0;
		temporary = true;
	}

	@Override
	public void update(int[] currentCoord) {
		
		if (System.currentTimeMillis() >= endTime) complete = true;
	}
}
