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
		
		if (start.length > 2) start = new int[]{start[0], start[1]};
		if (end.length > 2) end = new int[]{end[0], end[1]};
		
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
		
		while (!openSet.isEmpty()) {
			
			int parentIndex = openSet.first();
			int[] parent = getCoord(parentIndex, world.getX());
			
			if (Arrays.equals(parent, end)) {
				return reconstructPath(cameFrom, getIndex(end, world.getX()), pathScore);
			}
			
			arrayRemove(openSet, parentIndex);
			setFromCoords(closedSet, parent, true);
			
			
			for (byte i = 0; i < ADJACENT_NODES; i++) {
				
				int[] adjacent = new int[2];
				getAdjacentNode(parent, i, adjacent);
				
				
				//Skip and continue if not a valid coordinate
				if (0 > adjacent[0] || adjacent[0] >= world.getX()
						|| 0 > adjacent[1] || adjacent[1] >= world.getY()) 
					continue;
				
				if (world.isBlocked(adjacent[0], adjacent[1])) {
					continue;
				}
				
				if (getFromCoords(closedSet, adjacent)) continue;
				
				//TODO make getRelativeCost more dynamic.
				int tentativeMoveCost = getFromCoords(movementCost, parent) + getRelativeCost(i);
				
				
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
		
		return null;
	}
	
	private static ArrayList<int[]> reconstructPath(int[][] cameFrom, int currentIndex, int[][] movementCost) {
		
		ArrayList<int[]> steps = new ArrayList<int[]>();
		
//		BufferedImage totalImage = new BufferedImage(cameFrom.length, cameFrom[0].length, BufferedImage.TYPE_INT_RGB);
//		
//		int totalMax = 0;
//		
//		for (int y = 0; y < cameFrom[0].length; y++) {
//			
//			for (int x = 0; x < cameFrom.length; x++) {
//				
//				if (movementCost[x][y] > totalMax) totalMax = movementCost[x][y];
//			}
//		}
//		
//		for (int y = 0; y < cameFrom[0].length; y++) {
//			
//			for (int x = 0; x < cameFrom.length; x++) {
//				
//				int totalPixel = (int)(((float)movementCost[x][y] / totalMax) * 200) + 55;
//				
//				if (movementCost[x][y] > 0) {
//					totalImage.setRGB(x, cameFrom[0].length - y-1,totalPixel);
//				}
//			}
//		}
//		
//		try {
//			ImageIO.write(totalImage, "png", new File("MovementCost.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		do {
			
			int[] step = getCoord(currentIndex, cameFrom[0].length);
			steps.add(0, step);
			currentIndex = getFromCoords(cameFrom, step);
			
		} while (currentIndex != 0);
		
		return steps;
	}
	
	
	
	private static void getAdjacentNode(int[] parent, byte index, int[] result) {
		
		if (index < 3) {
			index--;
			result[0] = parent[0] + index;
			result[1] = parent[1] + 1;
		} else if (index < 5) {
			if (index == 3) {
				result[0] = parent[0] - 1;
			} else {
				result[0] = parent[0] + 1;
			}
			result[1] = parent[1];
		} else if (index < 8) {
			index -= 6;
			result[0] = parent[0] + index;
			result[1] = parent[1] - 1;
		}
	}
	
	private static byte getRelativeCost(int index) {
		
		final byte STRAIGHT = 10;
		final byte DIAGONAL = 14;
		
		byte gridValue = STRAIGHT;
		
		switch (index) {
		case 0:
			gridValue = DIAGONAL;
		case 2:
			gridValue = DIAGONAL;
		case 5:
			gridValue = DIAGONAL;
		case 7:
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
	
	private static void twoDimFill(int[][] array, int value) {
		for (int y = 0; y < array.length; y++) {
			
			for (int x = 0; x < array[0].length; x++) {
				
				array[x][y] = value;
			}
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
