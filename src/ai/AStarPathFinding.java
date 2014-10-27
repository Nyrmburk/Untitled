package ai;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import world.World;

/**
 * Use A* pathfinding to navigate the most efficient path in the given World.
 * Currently is dumb and will attempt to find a path even if there is no path,
 * which will result in searching the entire world.
 * 
 * @author Christopher Dombroski
 * @see {@link <a href="http://en.wikipedia.org/wiki/A*_search_algorithm"> AAlgorithm</a>}
 */
public class AStarPathFinding {

	/**
	 * The number of possible adjacent directions able to be taken.
	 * 
	 * <pre>
	 * 	7 0 4
	 * 	3   1
	 * 	6 2 5
	 * </pre>
	 */
	private static final byte ADJACENT_NODES = 8;

	/**
	 * Overloads findPath and set maxCost to Integer.MAX_VALUE.
	 * 
	 * @param world
	 *            World containing movement cost data and slope data.
	 * @param start
	 *            An array of length 2 designating the start coordinate, similar
	 *            to Point
	 * @param end
	 *            An array of length 2 designating the end coordinate, similar
	 *            to Point
	 * @return An ArrayList containing the coordinates of the found path. If no
	 *         path is found, returns null.
	 * @see main.World
	 */
	public static ArrayList<int[]> findPath(World world, int[] start, int[] end) {

		return findPath(world, start, end, Integer.MAX_VALUE);
	}

	/**
	 * Traverse through the nodes the the given World to find the optimal path
	 * between start and end. It uses the A* algorithm with a PriorityQueue as a
	 * binary heap for the open set. It supports multiple percentages of speed
	 * on terrain for obstacles such as roads and mountains. The path will not
	 * cut corners as it calculates combined cost for corners.
	 * 
	 * @param world
	 *            World containing movement cost data and slope data.
	 * @param start
	 *            An array of length 2 designating the start coordinate, similar
	 *            to Point
	 * @param end
	 *            An array of length 2 designating the end coordinate, similar
	 *            to Point
	 * @param maxCost
	 *            The maximum cost for a path
	 * @return An ArrayList containing the coordinates of the found path. If no
	 *         path is found, returns null.
	 * @see main.World
	 */
	public static ArrayList<int[]> findPath(World world, int[] start,
			int[] end, int maxCost) {

		if (start.length > 2)
			start = new int[] { start[0], start[1] };
		if (end.length > 2)
			end = new int[] { end[0], end[1] };

		/*
		 * TODO add areas check so the pathfinding does not search the entire
		 * map because the start and end are in 2 different enclosed areas.
		 */

		// intitialize variables
		// Where each coord came from
		int[][] cameFrom = new int[world.getX()][world.getY()];

		// The movement cost of each node
		int[][] movementCost = new int[world.getX()][world.getY()];
		// The toal score of each node
		int[][] pathScore = new int[world.getX()][world.getY()];

		// The open set
		// Is a binary heap
		PriorityQueue<Integer> openSet = new PriorityQueue<Integer>(
				new LowestTotalComparator(pathScore));
		// The closed set
		boolean[][] closedSet = new boolean[world.getX()][world.getY()];

		// Add start to openSet
		openSet.add(getIndex(start, world.getX()));

		// Fill cameFrom with -1 so reconstruct knows when to stop
		twoDimFill(cameFrom, -1);

		// Set start movement cost to 0
		setFromCoords(movementCost, start, 0);

		// Set start pathscore to calculated heuristic
		setFromCoords(pathScore, start, getFromCoords(movementCost, start)
				+ getHeuristic(start, end));

		// loop while nodes are still in the open set
		while (!openSet.isEmpty()) {

			// get and remove the lowest score in the open set
			int parentIndex = openSet.poll();
			int[] parent = getCoord(parentIndex, world.getX());

			// If reached the end, reconstruct path
			if (Arrays.equals(parent, end)) {
				// writeDebugImage(pathScore, "pathscore");
				// writeDebugImage(movementCost, "movementcost");
				// writeDebugImage(world.getMovementSpeed(), "speed");
				return reconstructPath(cameFrom, getIndex(end, world.getX()),
						pathScore);
			}

			setFromCoords(closedSet, parent, true);

			// iterate through all the adjacent nodes
			for (byte i = 0; i < ADJACENT_NODES; i++) {

				int[] adjacent = new int[2];
				getAdjacentNode(parent, i, adjacent);

				// Skip and continue if not a valid coordinate
				if (0 > adjacent[0] || adjacent[0] >= world.getX()
						|| 0 > adjacent[1] || adjacent[1] >= world.getY()) {
					continue;
				}

				// Skip and continue if present in the closed set
				if (getFromCoords(closedSet, adjacent)) {
					continue;
				}

				// Calculate relative movement cost and skip if it is blocked
				int relativeMoveCost = getRelativeCost(i, world, adjacent);
				if (relativeMoveCost == Integer.MAX_VALUE) {
					continue;
				}
				int tentativeMoveCost = getFromCoords(movementCost, parent)
						+ relativeMoveCost;

				// Skip and continue if movement cost is greater than maximum
				// allowed
				if (tentativeMoveCost >= maxCost)
					continue;

				int adjacentIndex = getIndex(adjacent, world.getX());

				/*
				 * If the open set contains the adjacent or the tentative
				 * movement cost is lower than the pervious value, add adjacent
				 * to the open set and update the values
				 */
				if (!openSet.contains(adjacentIndex)
						|| tentativeMoveCost < getFromCoords(movementCost,
								adjacent)) {

					setFromCoords(cameFrom, adjacent, parentIndex);
					setFromCoords(movementCost, adjacent, tentativeMoveCost);
					setFromCoords(pathScore, adjacent, getFromCoords(
							movementCost, adjacent)
							+ getHeuristic(adjacent, end));

					openSet.add(adjacentIndex);
				}
			}
		}

		// Failure in finding a path. This result means the entire map has been
		// explored.
		writeDebugImage(pathScore, "failure");
		return null;
	}

	/**
	 * Actually create the final path from a 2 dimensional array containing the
	 * index of the coordinate it came from.
	 * 
	 * @param cameFrom
	 *            2 dimensional array containing the index of the coordinate it
	 *            came from.
	 * @param currentIndex
	 *            The current ccord index
	 * @param movementCost
	 *            2 dimensional array of movement cost
	 * @return An ArrayList containing the coordinates of the found path. If no
	 *         path is found, returns null.
	 */
	private static ArrayList<int[]> reconstructPath(int[][] cameFrom,
			int currentIndex, int[][] movementCost) {

		ArrayList<int[]> steps = new ArrayList<int[]>();

		do {

			int[] step = getCoord(currentIndex, cameFrom[0].length);
			steps.add(0, step);
			currentIndex = getFromCoords(cameFrom, step);

		} while (currentIndex != -1);

		return steps;
	}

	/**
	 * Gets the adjacent node from the index shown below. Straight adjacent are
	 * retrieved for the first 4 indices and the latter 4 return the diagonals.
	 * This is done so the diagonals can properly calculate their cost based on
	 * the straight ones. It prevents cutting corners. Use in a for loop that
	 * iterates from 0 to {@link #ADJACENT_NODES}.
	 * 
	 * <pre>
	 * 	7 0 4
	 * 	3   1
	 * 	6 2 5
	 * </pre>
	 * 
	 * @param parent
	 *            The center to base adjacency off of
	 * @param index
	 *            Which adjacent to get.
	 * @param result
	 *            The coord of the result. Providing your own array is way
	 *            faster.
	 * @see #ADJACENT_NODES
	 */
	private static void getAdjacentNode(int[] parent, byte index, int[] result) {

		switch (index) {
		case 0:
			// up
			result[0] = parent[0];
			result[1] = parent[1] + 1;
			break;
		case 1:
			// right
			result[0] = parent[0] + 1;
			result[1] = parent[1];
			break;
		case 2:
			// down
			result[0] = parent[0];
			result[1] = parent[1] - 1;
			break;
		case 3:
			// left
			result[0] = parent[0] - 1;
			result[1] = parent[1];
			break;
		case 4:
			// upper right
			result[0] = parent[0] + 1;
			result[1] = parent[1] + 1;
			break;
		case 5:
			// lower right
			result[0] = parent[0] + 1;
			result[1] = parent[1] - 1;
			break;
		case 6:
			// lower left
			result[0] = parent[0] - 1;
			result[1] = parent[1] - 1;
			break;
		case 7:
			// upper left
			result[0] = parent[0] - 1;
			result[1] = parent[1] + 1;
			break;
		}
	}

	/**
	 * Calculate the relative movement cost for a coordinate.
	 * 
	 * @param index
	 *            Which side to calculate: {@link #ADJACENT_NODES}
	 * @param world
	 *            {@link main.World}
	 * @param coord
	 *            2 dimension array containing a coordinate
	 * @return relative movement cost or or Integer.MAX_VALUE if impassible.
	 */
	private static int getRelativeCost(int index, World world, int[] coord) {

		final int STRAIGHT = 100;

		int gridValue = STRAIGHT;

		if (index > 3) {

			gridValue = getDiagonalCost(index, world, coord);
		} else if (world.getMovementSpeed(coord) != 0) {

			gridValue /= world.getMovementSpeed(coord);
		} else {

			gridValue = Integer.MAX_VALUE;
		}

		// System.out.println(gridValue + ", " + Arrays.toString(coord));

		return gridValue;
	}

	/**
	 * Called by {@link #getRelativeCost} to calculate the diagonals.
	 * 
	 * @param index
	 *            Which side to calculate: {@link #ADJACENT_NODES}
	 * @param world
	 *            {@link main.World}
	 * @param coord
	 *            coord 2 dimension array containing a coordinate
	 * @return relative diagonal movement cost or Integer.MAX_VALUE if
	 *         impassible.
	 */
	private static int getDiagonalCost(int index, World world, int[] coord) {
		
		final int DIAGONAL = 141; // sqrt(2)*100

		int gridValue = 0;

		int[] coord1 = new int[2];
		int[] coord2 = new int[2];

		switch (index) {

		case 4:
			coord1 = new int[] { coord[0] - 1, coord[1] };
			coord2 = new int[] { coord[0], coord[1] - 1 };
			break;
		case 5:
			coord1 = new int[] { coord[0], coord[1] + 1 };
			coord2 = new int[] { coord[0] - 1, coord[1] };
			break;
		case 6:
			coord1 = new int[] { coord[0] + 1, coord[1] };
			coord2 = new int[] { coord[0], coord[1] + 1 };
			break;
		case 7:
			coord1 = new int[] { coord[0], coord[1] - 1 };
			coord2 = new int[] { coord[0] + 1, coord[1] };
			break;
		}

		int cornerCost;
		int diagonalCost;
		if (world.getMovementSpeed(coord) != 0
				&& world.getMovementSpeed(coord1) != 0
				&& world.getMovementSpeed(coord2) != 0) {

			cornerCost = (int) (1 / world.getMovementSpeed(coord1) + 1
					/ world.getMovementSpeed(coord2));
			
			diagonalCost = (int) (DIAGONAL / world.getMovementSpeed(coord));
		} else {

			return Integer.MAX_VALUE;
		}

		gridValue = (int) ((cornerCost / 2f) * diagonalCost);

		return gridValue;
	}

	/**
	 * calculate the
	 * {@link <a href="http://en.wikipedia.org/wiki/Taxicab_geometry"> Manhattan Distance</a>}
	 * between the two coords. Used as a directional helper to make the
	 * pathfinder take the most direct path by favoring nodes closer to the end.
	 * 
	 * @param current
	 *            2 dimension array containing a coordinate.
	 * @param end2
	 *            dimension array containing a coordinate.
	 * @return The
	 *         {@link <a href="http://en.wikipedia.org/wiki/Taxicab_geometry"> Manhattan Distance</a>}
	 */
	private static int getHeuristic(int[] current, int[] end) {

		// Manhattan Distance (Straight)
		// Cheapest
		int distance = 0;
		distance += absDistance(current[0], end[0]);
		distance += absDistance(current[1], end[1]);

		return distance * 100;

		// Euclidean Distance
		// int dx, dy;
		// dx = absDistance(current[0], end[0]);
		// dy = absDistance(current[1], end[1]);
		//
		// return (int) (100 * Math.sqrt(dx * dx + dy * dy));

		// Chebyshev Distance (Diagonal)
		// int dx, dy;
		// dx = absDistance(current[0], end[0]);
		// dy = absDistance(current[1], end[1]);
		// return 100 * Math.max(dx, dy);
	}

	/**
	 * Same as <code>Math.abs(a - b);</code> Called by {@link #getHeuristic}.
	 * 
	 * @param a
	 *            delta A
	 * @param b
	 *            delta B
	 * @return distance between
	 */
	private static int absDistance(int a, int b) {
		int distance = 0;
		if (a > b) {
			distance = a - b;
		} else {
			distance = b - a;
		}
		return distance;
	}

	/**
	 * Get the index of an element in a two dimensional array. Use in
	 * conjunction with getCoord();
	 * 
	 * @param coord
	 *            The coordinate in the array.
	 * @param width
	 *            The width of the array.
	 * @return index The index in the array.
	 * @see #getCoord
	 */
	private static int getIndex(int[] coord, int width) {

		int index = coord[0] + coord[1] * width;

		return index;
	}

	/**
	 * Get the coordinate from the index in a two dimensional array. Use in
	 * conjunction with getIndex();
	 * 
	 * @param index
	 *            The index in the array.
	 * @param width
	 *            The width of the array.
	 * @return The coordinate in the array.
	 * @see #getIndex
	 */
	protected static int[] getCoord(int index, int width) {
		int[] coord = new int[2];

		coord[1] = Math.floorDiv(index, width);
		coord[0] = index - coord[1] * width;

		return coord;
	}

	/**
	 * A shorthand way of writing toGetFrom[coord[0]][coord[1]]
	 * 
	 * @param toGetFrom
	 *            A 2 dimensional array
	 * @param coord
	 *            An array of length 2 designating a coordinate, similar to
	 *            Point
	 * @return value at toGetFrom[coord[0]][coord[1]]
	 */
	private static int getFromCoords(int[][] toGetFrom, int[] coord) {

		return toGetFrom[coord[0]][coord[1]];
	}

	/**
	 * A shorthand way of writing toGetFrom[coord[0]][coord[1]]
	 * 
	 * @param toGetFrom
	 *            A 2 dimensional array
	 * @param coord
	 *            An array of length 2 designating a coordinate, similar to
	 *            Point
	 * @return value at toGetFrom[coord[0]][coord[1]]
	 */
	private static boolean getFromCoords(boolean[][] toGetFrom, int[] coord) {

		return toGetFrom[coord[0]][coord[1]];
	}

	/**
	 * A shorthand way of writing toSet[coord[0]][coord[1]] = value
	 * 
	 * @param toSet
	 *            A 2 dimensional array
	 * @param coord
	 *            An array of length 2 designating a coordinate, similar to
	 *            Point
	 */
	private static void setFromCoords(int[][] toSet, int[] coords, int value) {

		toSet[coords[0]][coords[1]] = value;
	}

	/**
	 * A shorthand way of writing toSet[coord[0]][coord[1]] = value
	 * 
	 * @param toSet
	 *            A 2 dimensional array
	 * @param coord
	 *            An array of length 2 designating a coordinate, similar to
	 *            Point
	 */
	private static void setFromCoords(boolean[][] toSet, int[] coords,
			boolean value) {

		toSet[coords[0]][coords[1]] = value;
	}

	/**
	 * Fill a 2 dimensional array with the given value.
	 * 
	 * @param array
	 *            Array to fill
	 * @param value
	 *            value to fill with
	 */
	private static void twoDimFill(int[][] array, int value) {
		for (int y = 0; y < array[0].length; y++) {

			for (int x = 0; x < array.length; x++) {

				array[x][y] = value;
			}
		}
	}

	/**
	 * Make an image showing the path taken by the pathfinder Saves the image as
	 * name.png in the project directory
	 * 
	 * @param map
	 *            2 dimensional array to save image of
	 * @param name
	 *            file name
	 */
	private static void writeDebugImage(int[][] map, String name) {
		float[][] floatMap = new float[map.length][map[0].length];
		for (int y = 0; y < map[0].length; y++) {

			for (int x = 0; x < map.length; x++) {

				floatMap[x][y] = map[x][y];
			}
		}

		writeDebugImage(floatMap, name);
	}

	/**
	 * Make an image showing the path taken by the pathfinder Saves the image as
	 * name.png in the project directory
	 * 
	 * @param map
	 *            2 dimensional array to save image of
	 * @param name
	 *            file name
	 */
	private static void writeDebugImage(float[][] map, String name) {

		BufferedImage image = new BufferedImage(map.length, map[0].length,
				BufferedImage.TYPE_INT_RGB);

		float max = 0;

		for (int y = 0; y < map[0].length; y++) {

			for (int x = 0; x < map.length; x++) {

				if (map[x][y] > max)
					max = map[x][y];
			}
		}

		for (int y = 0; y < map[0].length; y++) {

			for (int x = 0; x < map.length; x++) {

				int pixel = (int) (((float) map[x][y] / max) * 200) + 55;

				if (map[x][y] > 0) {
					image.setRGB(x, map[0].length - y - 1, pixel);
				}
			}
		}

		try {
			ImageIO.write(image, "png", new File(name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * A Comparator that compares the movement cost of 2 coordinates for sorting the
 * open set
 * 
 * @author Christopher Dombroski
 *
 */
class LowestTotalComparator implements Comparator<Integer> {

	int[][] score;

	protected LowestTotalComparator(int[][] score) {

		this.score = score;
	}

	@Override
	public int compare(Integer coord1, Integer coord2) {
		int[] node1 = AStarPathFinding.getCoord(coord1, score.length);
		int[] node2 = AStarPathFinding.getCoord(coord2, score.length);

		if (score[node1[0]][node1[1]] < score[node2[0]][node2[1]]) {
			return -1;
		} else if (score[node1[0]][node1[1]] > score[node2[0]][node2[1]]) {
			return 1;
		} else if (coord1.equals(coord2)) {
			return 0;
		} else {
			return 1;
		}
	}
}