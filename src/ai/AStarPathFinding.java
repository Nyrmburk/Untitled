package ai;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import world.World;

public class AStarPathFinding {
	
	private static final byte ADJACENT_NODES = 8;
	
	public static ArrayList<int[]> findPath(World world, int[] start, int[] end) {
		
		return findPath(world, start, end, Integer.MAX_VALUE);
	}
	
	public static ArrayList<int[]> findPath(World world, int[] start, int[] end, int maxCost) {
		
		if (start.length > 2) start = new int[]{start[0], start[1]};
		if (end.length > 2) end = new int[]{end[0], end[1]};
		
		if (!isSafeSpot(world, start)) return null;
		if (!isSafeSpot(world, end)) return null;
		
		//intitialize variables
		int[][] cameFrom = new int[world.getX()][world.getY()];
		
		int[][] movementCost = new int[world.getX()][world.getY()];
		int[][] pathScore = new int[world.getX()][world.getY()];
		
		SortedSet<Integer> openSet = new TreeSet<Integer>(new LowestTotalComparator(pathScore));
		boolean[][] closedSet = new boolean[world.getX()][world.getY()];
		
		openSet.add(getIndex(start, world.getX()));
		twoDimFill(movementCost, -1);
		setFromCoords(movementCost, start, 0);
		setFromCoords(pathScore, start, getFromCoords(movementCost, start) + getHeuristic(start, end));
		
		boolean[] allowDiagonal = new boolean[4];
		
		while (!openSet.isEmpty()) {
			
			int parentIndex = openSet.first();
			int[] parent = getCoord(parentIndex, world.getX());
			
			if (Arrays.equals(parent, end)) {
				return reconstructPath(cameFrom, getIndex(end, world.getX()), pathScore);
			}
			
			arrayRemove(openSet, parentIndex);
			setFromCoords(closedSet, parent, true);
			
			Arrays.fill(allowDiagonal, true);
			
			for (byte i = 0; i < ADJACENT_NODES; i++) {
				
				if (i > 3 && !allowDiagonal[i - ADJACENT_NODES / 2]) continue; 
				
				int[] adjacent = new int[2];
				getAdjacentNode(parent, i, adjacent);
				
				
				//Skip and continue if not a valid coordinate
				if (0 > adjacent[0] || adjacent[0] >= world.getX()
						|| 0 > adjacent[1] || adjacent[1] >= world.getY()) {
					
					blockDiagonal(i, allowDiagonal);
					continue;
				}
				
				if (world.isBlocked(adjacent[0], adjacent[1])) {
					blockDiagonal(i, allowDiagonal);
					continue;
				}
				
				if (getFromCoords(closedSet, adjacent)) continue;
				
				//TODO make getRelativeCost more dynamic.
				int tentativeMoveCost = getFromCoords(movementCost, parent) + getRelativeCost(i);
				
				if (tentativeMoveCost >= maxCost) continue;
				
				int adjacentIndex = getIndex(adjacent, world.getX());
				
				if (!arrayContains(openSet, adjacentIndex) || 
						tentativeMoveCost < getFromCoords(movementCost, adjacent)) {
					
					setFromCoords(cameFrom, adjacent, parentIndex);
					setFromCoords(movementCost, adjacent, tentativeMoveCost);
					setFromCoords(pathScore, adjacent, getFromCoords(movementCost, adjacent) + getHeuristic(adjacent, end));
					
					if (!arrayContains(openSet, adjacentIndex)) openSet.add(adjacentIndex);
				}
			}
		}
		
		writeDebugImage(pathScore);
		return null;
	}
	
	private static ArrayList<int[]> reconstructPath(int[][] cameFrom, int currentIndex, int[][] movementCost) {
		
		ArrayList<int[]> steps = new ArrayList<int[]>();
		
		do {
			
			int[] step = getCoord(currentIndex, cameFrom[0].length);
			steps.add(0, step);
			currentIndex = getFromCoords(cameFrom, step);
			
		} while (currentIndex != 0);
		
		return steps;
	}
	
	
	
	private static void getAdjacentNode(int[] parent, byte index, int[] result) {
		
		switch (index) {
		case 0:
			//up
			result[0] = parent[0];
			result[1] = parent[1] + 1;
			break;
		case 1:
			//right
			result[0] = parent[0] + 1;
			result[1] = parent[1];
			break;
		case 2:
			//down
			result[0] = parent[0];
			result[1] = parent[1] - 1;
			break;
		case 3:
			//left
			result[0] = parent[0] - 1;
			result[1] = parent[1];
			break;
		case 4:
			//upper right
			result[0] = parent[0] + 1;
			result[1] = parent[1] + 1;
			break;
		case 5:
			//lower right
			result[0] = parent[0] + 1;
			result[1] = parent[1] - 1;
			break;
		case 6:
			//lower left
			result[0] = parent[0] - 1;
			result[1] = parent[1] - 1;
			break;
		case 7:
			//upper left
			result[0] = parent[0] - 1;
			result[1] = parent[1] + 1;
			break;
		}
		
//		if (index < 3) {
//			index--;
//			result[0] = parent[0] + index;
//			result[1] = parent[1] + 1;
//		} else if (index < 5) {
//			if (index == 3) {
//				result[0] = parent[0] - 1;
//			} else {
//				result[0] = parent[0] + 1;
//			}
//			result[1] = parent[1];
//		} else if (index < 8) {
//			index -= 6;
//			result[0] = parent[0] + index;
//			result[1] = parent[1] - 1;
//		}
	}
	
	private static void blockDiagonal(int index, boolean[] allowDiagonal) {
		
		if (index > 3) return;
		
		allowDiagonal[index] = false;
		
		index--;
		
		if (index < 0) index += 4;
		allowDiagonal[index] = false;
	}
	
	private static byte getRelativeCost(int index) {
		
		final byte STRAIGHT = 10;
		final byte DIAGONAL = 14;
		
		byte gridValue = STRAIGHT;
		
		if (index > 3) {
			gridValue = DIAGONAL;
		}
		
		return gridValue;
	}
	
	private static int getHeuristic(int[] current, int[] end) {
		
		int distance = 0;
		distance += absDistance(current[0], end[0]);
		distance += absDistance(current[1], end[1]);
		
		return distance * 10;
		
//		int dx, dy;
//		dx = Math.abs(current[0] - end[0]);
//		dy = Math.abs(current[1] - end[1]);
//		
//		return (int) (10 * Math.sqrt(dx * dx + dy * dy));
	}
	
	private static int absDistance(int a, int b) {
		int distance = 0;
		if (a > b) {
			distance = a - b;
		} else {
			distance = b - a;
		}
		return distance;
	}
	
	private static boolean arrayContains(Collection<Integer> c, int index) {
		
		Iterator<Integer> it = c.iterator();
		
		while (it.hasNext()) {
			
			if (it.next() == index) return true;
		}
		
		return false;
	}
	
	private static boolean arrayRemove(Collection<Integer> c, int index) {
		
		Iterator<Integer> it = c.iterator();
		
		while (it.hasNext()) {
			
			if (it.next() == index) {
				
				it.remove();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get the index of an element in a two dimensional array.
	 * Use in conjunction with getCoord();
	 * 
	 * @param coord The coordinate in the array.
	 * @param width The width of the array.
	 * @return index The index in the array.
	 */
	private static int getIndex(int[] coord, int width) {
		
		int index = coord[0] + coord[1] * width;
		
//		System.out.println("converted " + Arrays.toString(coord) + " to " + index);
		
		return index;
	}
	
	/**
	 * Get the coordinate from the index in a two dimensional array.
	 * Use in conjunction with getIndex();
	 * 
	 * @param index The index in the array.
	 * @param width The width of the array.
	 * @return The coordinate in the array.
	 */
	protected static int[] getCoord(int index, int width) {
		int[] coord = new int[2];
		
		coord[1] = Math.floorDiv(index, width);
		coord[0] = index - coord[1] * width;
		
//		System.out.println("converted " + index + " to " + Arrays.toString(coord));
		
		return coord;
	}
	
	private static int getFromCoords(int[][] toGetFrom, int[] coords) {
		
		return toGetFrom[coords[0]][coords[1]];
	}
	
	private static boolean getFromCoords(boolean[][] toGetFrom, int[] coords) {
		
		return toGetFrom[coords[0]][coords[1]];
	}
	
	private static void setFromCoords(int[][] toSet, int[] coords, int value) {
		
		toSet[coords[0]][coords[1]] = value;
	}
	
	private static void setFromCoords(boolean[][] toSet, int[] coords, boolean value) {
		
		toSet[coords[0]][coords[1]] = value;
	}
	
	public static boolean isSafeSpot(World world, int[] coord) {
		
		boolean safe = true;
		int[] checkCoord = coord.clone();
		
		safe = !world.isBlocked(checkCoord[0], checkCoord[1]);
//		if (safe) {
//			for (byte i = 4; i < ADJACENT_NODES; i++) {
//				
//				getAdjacentNode(checkCoord, i, checkCoord);
//				if (world.isBlocked(checkCoord[0], checkCoord[1])) {
//					safe = false;
//				}
//			}
//		}
		
		return safe;
	}
	
	private static void twoDimFill(int[][] array, int value) {
		for (int y = 0; y < array.length; y++) {
			
			for (int x = 0; x < array[0].length; x++) {
				
				array[x][y] = value;
			}
		}
	}
	
	private static void writeDebugImage(int[][] map) {
		
		BufferedImage image = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_RGB);
		
		int max = 0;
		
		for (int y = 0; y < map[0].length; y++) {
			
			for (int x = 0; x < map.length; x++) {
				
				if (map[x][y] > max) max = map[x][y];
			}
		}
		
		for (int y = 0; y < map[0].length; y++) {
			
			for (int x = 0; x < map.length; x++) {
				
				int pixel = (int)(((float)map[x][y] / max) * 200) + 55;
				
				if (map[x][y] > 0) {
					image.setRGB(x, map[0].length - y-1, pixel);
				}
			}
		}
		
		try {
			ImageIO.write(image, "png", new File("debug.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class LowestTotalComparator implements Comparator<Integer> {

	int[][] score;
	
	protected LowestTotalComparator(int[][] score) {
		
		this.score = score;
	}
	
	@Override
	public int compare(Integer coord1, Integer coord2) {
		int[] node1 = AStarPathFinding.getCoord(coord1, score[0].length);
		int[] node2 = AStarPathFinding.getCoord(coord2, score[0].length);
		
		if (score[node1[0]][node1[1]] < score[node2[0]][node2[1]]) {
			return -1;
		} else if (score[node1[0]][node1[1]] > score[node2[0]][node2[1]]) {
			return 1;
		} else if (coord1 == coord2){
			return 0;
		} else {
			return 1;
		}
	}
}
