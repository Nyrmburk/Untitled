package entity;

import graphics.Model;


public class Item extends Entity{
	
	public enum itemTypes {
		
		ROCK,
		FOOD,
		DEAD_BODY
	}
	
	itemTypes itemType;

	public Item(String name, itemTypes itemType, float[] location, Model model) {
		super(name, types.ITEM, location, model);
		this.itemType = itemType;
	}
}
