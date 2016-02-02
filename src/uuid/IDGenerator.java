package uuid;

import java.util.LinkedList;
import java.util.Queue;

public class IDGenerator {

	private int nextID = 0;
	private Queue<Integer> recycledIDs = new LinkedList<Integer>();
	
	public IDGenerator() {
		
	}
	
	public int genID() {
		
		int id = -1;
		
		if (!recycledIDs.isEmpty()) {
			
			id = recycledIDs.poll();
		} else {
			
			id = nextID++;
		}
		return id;
	}
	
	public void removeID(int id) {
		
		recycledIDs.add(id);
	}
}
