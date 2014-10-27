package world;

import main.Engine;
import graphics.Drawable;
import graphics.Render;

public class Zone implements Drawable {

	int x;
	int y;
	int width;
	int height;

	long alloweditems;

	byte[][] occupied;

	public Zone(int x, int y, int width, int height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		occupied = new byte[width][height];
	}
	
	public Zone(int[] startCoord, int[] endCoord) {
		
		int[] lowest = { Math.min(startCoord[0], endCoord[0]),
				Math.min(startCoord[1], endCoord[1]) };
		int[] highest = { Math.max(startCoord[0], endCoord[0]),
				Math.max(startCoord[1], endCoord[1]) };

		this.x = lowest[0];
		this.y = lowest[1];
		this.width = highest[0] - x;
		this.height = highest[1] - y;
		occupied = new byte[width][height];
	}

	public int[] getCenter() {

		return new int[] { x + width / 2, y + height / 2 };
	}

	@Override
	public void draw() {

		Render.drawSelectionGrid(Engine.world, new int[] { x, y }, new int[] {
				x + width, y + height });
	}
	
	@Override
	public String toString() {
		
		return "Zone [" + x + ", " + y + ", " + width + ", " + height + "]";
	}
}