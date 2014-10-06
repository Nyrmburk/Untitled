package ai;

import java.util.Arrays;
import java.util.Random;

import main.Engine;

public class ActionWander extends Action {
	
	private Random random = new Random();

	public ActionWander(int[] currentLocation) {

		priority = 0;

		do {
			destination = new int[] {
					currentLocation[0] + random.nextInt(21) - 10,
					currentLocation[1] + random.nextInt(21) - 10 };

		} while (Engine.world.isBlocked(destination[0], destination[1]));
	}

	public void update(int[] currentCoord) {

		complete = Arrays.equals(currentCoord, destination);
		
		if (complete) {
			chainedAction = new ActionWait(random.nextInt(1001) + 500);
		}
	}
}
