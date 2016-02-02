package entity;

import graphics.ModelLoader;

/**
 * Contain information about the item such as the type and attributes. 
 * @author Christopher Dombroski
 *
 */
public class Item extends Entity {
	
	boolean selected = false;
	
	public Item(String name, float[] coord, ModelLoader model) {
		
		super(name, coord, model);
	}
}
