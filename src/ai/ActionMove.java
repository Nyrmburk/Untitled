package ai;

import java.util.Arrays;
import java.util.Random;

/**
 * Move to a location when the mouse is clicked.
 * @author Christopher Dombroski
 *
 */
public class ActionMove extends Action {

	private Random random = new Random();
	
	public ActionMove(int[] coord) {
		
		destination = coord;
		priority = 10;
		temporary = true;
	}
	
	@Override
	public void update(int[] currentCoord) {
		
		complete = Arrays.equals(currentCoord, destination);
		
		if (complete) {
			
			chainedAction = new ActionWait(random.nextInt(1001) + 2000);
		}
	}
}
