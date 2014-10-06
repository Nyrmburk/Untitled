package ai;

public abstract class Action implements Comparable<Action> {
	
	protected boolean complete = false;
	
	byte priority = 0;
	
	public int[] destination;
	
	public Action chainedAction = null;
	
	public abstract void update(int[] currentCoord);
	
	public boolean isComplete() {
		
		return complete;
	}

	@Override
	public int compareTo(Action otherAction) {
		
		return Byte.compare(this.priority, otherAction.priority);
	}
}
