package uuid;

import java.util.ArrayList;

public class IDList <E>{
	
	IDGenerator generator;
	ArrayList<E> data;
	
	public void add(E element) {
		
		data.add(generator.genID(), element);
	}
	
	public void remove(int id) {
		
		data.remove(id);
		generator.removeID(id);
	}
}
