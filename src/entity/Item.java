package entity;

import main.Engine;
import graphics.MapMesh;
import graphics.Model;

/**
 * Contain information about the item such as the type and attributes. 
 * @author Christopher Dombroski
 *
 */
public class Item extends Entity implements Selectable, Movable {
	
	float moveSpeedPercent;
	
	boolean selected = false;
	
	public enum itemTypes {
		
		ROCK,
		FOOD,
		DEAD_BODY
	}
	
	itemTypes itemType;
	
	public Item(String name, itemTypes itemType, int[] coord, Model model) {
		
		this(name, itemType, 1f, coord, model);
	}

	public Item(String name, itemTypes itemType, float moveSpeedPercent, int[] coord, Model model) {
		super(name, types.ITEM, new float[]{coord[0], coord[1], 
				((MapMesh) Engine.worldEntity.mdl)
				.getHeight(new float[]{coord[0], coord[1]})}, model);
		super.addSelectable(this);
		this.itemType = itemType;
		this.moveSpeedPercent = moveSpeedPercent;
		Engine.world.setMovementSpeed(coord, moveSpeedPercent);
	}

	@Override
	public boolean isAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getMoveSpeedPercent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pickUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSelected() {
		
		return selected;
	}

	@Override
	public void selected(boolean selected) {
		
		this.selected = selected;
	}
}
