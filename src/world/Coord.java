package world;

import java.awt.Point;

/**
 * Provide a simple means of holding a coordinate and add some helper methods
 * for them.
 * 
 * @author Christopher Dombroski
 *
 */
public class Coord extends Point {

	private static final long serialVersionUID = -3534420675265073007L;

	public Coord() {

		super();
	}

	public Coord(int x, int y) {

		super(x, y);
	}
	
	public int[] toArray() {
		
		return new int[]{x, y};
	}

	public static Coord locationToCoord(float[] location) {

		return new Coord(Math.round(location[0]), Math.round(location[1]));
	}

	public static Coord lowestCoord(Coord coord1, Coord coord2) {

		int lowestX = Math.min(coord1.x, coord2.x);
		int lowestY = Math.min(coord1.y, coord2.y);
		return new Coord(lowestX, lowestY);
	}

	public static Coord highestCoord(Coord coord1, Coord coord2) {

		int highestX = Math.max(coord1.x, coord2.x);
		int highestY = Math.max(coord1.y, coord2.y);
		return new Coord(highestX, highestY);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Coord) {

			Coord coord = (Coord) o;

			if (this.x == coord.x && this.y == coord.y) {

				return true;
			}
		}

		return false;
	}
}
