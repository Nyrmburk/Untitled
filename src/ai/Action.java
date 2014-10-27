package ai;

/**
 * The most basic part of the AI. Each action has a priority and destination. It
 * also has a nested action so chaining actions is possible. It is designed to
 * be extended for more sophisticated functions. The boolean temprorary is used
 * to determine if the action can be replaced by a higher ranking priority
 * action. A sample Action called "ActionGetFood" might look have update() set
 * the current destination to the nearest dource of food and chain another
 * action to eat the food.
 * 
 * @author Christopher Dombroski
 *
 */
public abstract class Action implements Comparable<Action> {
	
	protected boolean complete = false;
	protected boolean temporary = false;
	
	byte priority = 0;
	
	public int[] destination;
	
	public Action chainedAction = null;
	
	public abstract void update(int[] currentCoord);
	
	public boolean isComplete() {
		
		return complete;
	}
	
	public byte getPriority() {
		
		return priority;
	}
	
	public boolean isTemporary() {
		
		return temporary;
	}

	@Override
	public int compareTo(Action otherAction) {
		
		return Byte.compare(otherAction.priority,this.priority);
	}
}
