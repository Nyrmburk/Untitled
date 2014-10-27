package ai;

import java.util.Arrays;
import java.util.Random;

import main.Engine;

/**
 * Pick a random destination location within a 10 block radius, and then chain 
 * ActionWait for a random amount of time between 0.5 and 2.5 seconds.
 * @author Christopher Dombroski
 *
 */
public class ActionWander extends Action {
	
	private Random random = new Random();

	public ActionWander(int[] currentLocation) {

		priority = 0;
		temporary = true;

		do {
			destination = new int[] {
					currentLocation[0] + random.nextInt(21) - 10,
					currentLocation[1] + random.nextInt(21) - 10 };

		} while (Engine.world.isBlocked(destination[0], destination[1]) || 
				Engine.world.getMovementSpeed(new int[]{destination[0], destination[1]}) <= 0);
	}

	public void update(int[] currentCoord) {

		complete = Arrays.equals(currentCoord, destination);
		
		if (complete) {
			chainedAction = new ActionWait(random.nextInt(2001) + 500);
		}
	}
}
