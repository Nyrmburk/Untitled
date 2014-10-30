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

	public Zone(Coord startCoord, Coord endCoord) {

		Coord lowest = Coord.lowestCoord(startCoord, endCoord);
		Coord highest = Coord.highestCoord(startCoord, endCoord);

		this.x = lowest.x;
		this.y = lowest.y;
		this.width = highest.x - x;
		this.height = highest.y - y;
		occupied = new byte[width][height];
	}

	public int[] getCenter() {

		return new int[] { x + width / 2, y + height / 2 };
	}

	@Override
	public void draw() {

		Render.drawSelectionGrid(Engine.world, new Coord(x, y), new Coord(x
				+ width, y + height));
	}

	@Override
	public String toString() {

		return "Zone [" + x + ", " + y + ", " + width + ", " + height + "]";
	}
}