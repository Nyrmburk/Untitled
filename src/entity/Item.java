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
		this.itemType = itemType;
		this.moveSpeedPercent = moveSpeedPercent;
		Engine.world.setMovementSpeed(coord, moveSpeedPercent);
	}

	@Override
	public int[] getCoord() {
		
		return locationToCoord(location);
	}
	
	private int[] locationToCoord(float[] location) {
		int[] coord = new int[2];

		coord[0] = (int) Math.rint(location[0]);
		coord[1] = (int) Math.rint(location[1]);

		return coord;
	}

	@Override
	public int getSelectionPriority() {
		
		return 1;
	}

	@Override
	public boolean isSelected() {
		
		return selected;
	}

	@Override
	public void checkSelected(int[] startCoord, int[] endCoord) {

		int[] coord = getCoord();
		int[] lowestCoord = { Math.min(startCoord[0], endCoord[0]),
				Math.min(startCoord[1], endCoord[1]) };
		;
		int[] highestCoord = { Math.max(startCoord[0], endCoord[0]),
				Math.max(startCoord[1], endCoord[1]) };
		;

		if (lowestCoord[0] <= coord[0] && coord[0] <= highestCoord[0]
				&& lowestCoord[1] < coord[1] && coord[1] < highestCoord[1]) {

			selected = true;
		} else {

			selected = false;
		}
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
}
