package entity;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import world.World;

public class AI {
	
	public static ArrayList<int[]> Pathfind(int[] start, int[] end, World world) {
		//A* algorithm
		
		ArrayList<int[]> step = new ArrayList<int[]>();
		
		//The parent coordinate at any given step
		int[] current = start.clone();
		//The adjacent coordinate
		int[] result = new int[2];
		
		//The total path cost
		int[][] total = new int[world.getX()][world.getY()];
		//The movement cost for each coord
		int[][] grid = new int[world.getX()][world.getY()];
		//The manhattan distance for each coord
		int[][] heuristic = new int[world.getX()][world.getY()];
		//Whether or not the path has been traversed
		boolean[][] closed = new boolean[world.getX()][world.getY()];
		
		//While not current is equal to end
		while (current[0] != end[0] || current[1] != end[1]) {
			
			if (step.size() > 5000) break;
			
			//For each of the adjacent nodes
			for (int i = 0; i < 8; i++) {
				
				//Get the adjacent node for the adjacent index
				getAdjacentNode(current, i, result);
				
				//Skip and continue if not a valid coordinate
				if (0 > result[0] || result[0] > world.getX()
						|| 0 > result[1] || result[1] > world.getY()) 
					continue;
				
				//If the node is blocked, skip and continue
				if (world.isBlocked(result[0], result[1])) {
					continue;
				}
				
				//If the node is closed, skip and continue
				if (closed[result[0]][result[1]]) {
					continue;
				}
				
				//Adjacent movement cost = Parent movement cost plus relative cost
				grid[result[0]][result[1]] = (grid[current[0]][current[1]] + getGrid(i));
				//Adjacent heuristic = manhattan distance to end 
				heuristic[result[0]][result[1]] = getManhattan(result, end);
				//Total = movement cost + heuristic
				total[result[0]][result[1]] = 
						grid[result[0]][result[1]] + 
						heuristic[result[0]][result[1]];
			}
			
			//The lowest cost per out of all adjacent tiles
			int lowest = Integer.MAX_VALUE;
			//What index the adjacent node is at
			int index = -1;
			
			//For each adjacent node
			for (int i = 0; i < 8; i++) {
				
				//Get the adjacent node
				getAdjacentNode(current, i, result);
				
				//Skip and continue if not a valid coordinate
				if (0 > result[0] || result[0] > world.getX()
						|| 0 > result[1] || result[1] > world.getY()) 
					continue;
				
				//If the node is blocked, skip and continue
				if (world.isBlocked(result[0], result[1])) {
					continue;
				}
				
				//If the node is closed, skip and continue
				if (closed[result[0]][result[1]]) {
					continue;
				}
				
				//If the adjacent total cost is lowest out of all the others checked,
				//set lowest to the cost and set index to the current index
				if (total[result[0]][result[1]] < lowest) {
					lowest = total[result[0]][result[1]];
					index = i;
				}
			}
			
			//if (index < 0) break;
			
			//Add the current node to the closed list
			closed[current[0]][current[1]] = true;
			//Set current to the lowest movement cost
			getAdjacentNode(current, index, current);
			
		}
		
//		closed = new boolean[world.getX()][world.getY()];
//		current = end.clone();
		
		while (current[0] != start[0] || current[1] != start[1]) {
			
			//The lowest cost per out of all adjacent tiles
			int lowest = Integer.MAX_VALUE;
			//What index the adjacent node is at
			int index = -1;
			
			//For each of the adjacent nodes
			for (int i = 0; i < 8; i++) {
				
				//Get the adjacent node for the adjacent index
				getAdjacentNode(current, i, result);
				
				//Skip and continue if not a valid coordinate
				if (0 > result[0] || result[0] > world.getX()
						|| 0 > result[1] || result[1] > world.getY()) 
					continue;
				
				//If the node is blocked, skip and continue
				if (world.isBlocked(result[0], result[1])) {
					continue;
				}
				
				//If the node is closed, skip and continue
				if (!closed[result[0]][result[1]]) {
					continue;
				}
				
				//If the adjacent total cost is lowest out of all the others checked,
				//set lowest to the cost and set index to the current index
				if (total[result[0]][result[1]] < lowest) {
					lowest = total[result[0]][result[1]];
					index = i;
				}
			}
			
			if (index < 0) break;
			
			//Add the current node to the closed list
			closed[current[0]][current[1]] = false;
			//Set current to the lowest movement cost
			getAdjacentNode(current, index, current);
			//Add the next step
			step.add(current.clone());
		}
		
		int totalMax = 0;
		int gridMax = 0;
		int heurMax = 0;
		
		for (int y = 0; y < world.getY(); y++) {
			
			for (int x = 0; x < world.getX(); x++) {
				
				if (total[x][y] > totalMax) totalMax = total[x][y];
				if (grid[x][y] > gridMax) gridMax = grid[x][y];
				if (heuristic[x][y] > heurMax) heurMax = heuristic[x][y];
			}
		}
		
		BufferedImage totalImage = new BufferedImage(world.getX(), world.getY(), BufferedImage.TYPE_INT_RGB);
		BufferedImage gridImage = new BufferedImage(world.getX(), world.getY(), BufferedImage.TYPE_INT_RGB);
		BufferedImage heurImage = new BufferedImage(world.getX(), world.getY(), BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < world.getY(); y++) {
			
			for (int x = 0; x < world.getX(); x++) {
				
				int traversable = 0x7F00;
				int totalPixel = (int)(((float)total[x][y] / totalMax) * 200) + 55;
				int gridPixel = (int)(((float)grid[x][y] / gridMax) * 200) + 55;
				int heurPixel = (int)(((float)heuristic[x][y] / heurMax) * 200) + 55;
				
				if (world.isBlocked(x, y)) {
					traversable = 0x7F0000;
				}
				
				if (total[x][y] > 0) {
					totalImage.setRGB(x, world.getY() - y-1,totalPixel);
				} else {
					totalImage.setRGB(x, world.getY() - y-1, traversable);
				}
				if (total[x][y] > 0) {
					gridImage.setRGB(x, world.getY() - y-1,gridPixel);
				} else {
					gridImage.setRGB(x, world.getY() - y-1, traversable);
				}
				if (total[x][y] > 0) {
					heurImage.setRGB(x, world.getY() - y-1,heurPixel);
				} else {
					heurImage.setRGB(x, world.getY() - y-1, traversable);
				}
			}
		}
		
		try {
			ImageIO.write(totalImage, "png", new File("Total.png"));
			ImageIO.write(gridImage, "png", new File("Grid.png"));
			ImageIO.write(heurImage, "png", new File("Heuristic.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return step;
	}
	
	private static void getAdjacentNode(int[] parent, int index, int[] result) {
		
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
	
	private static byte getGrid(int index) {
		
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
	
	private static int getManhattan(int[] current, int[] end) {
		
		int distance = 0;
		distance += absDistance(current[0], end[0]);
		distance += absDistance(current[1], end[1]);
		
		return distance * 10;
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
	
	private static int getFromCoords(int[][] toGetFrom, int[] coords) {
		
		return toGetFrom[coords[0]][coords[1]];
	}
	
	private static void setFromCoords(int[][] toSet, int[] coords, int value) {
		
		toSet[coords[0]][coords[1]] = value;
	}
	
//	private static void getMovementCost() {
//		
//	}
	
	private static boolean arrayContains(Collection<int[]> c, int[] array) {
		
		Iterator<int[]> iterate = c.iterator();
		
		while (iterate.hasNext()) {
			
			int[] nextArray = iterate.next();
			
			if (Arrays.equals(array, nextArray)) {
				return true;
			}
		}
		
		return false;
	}
}

class LowestTotalComparator implements Comparator<int[]> {

	int[][] score;
	
	protected LowestTotalComparator(int[][] score) {
		
		this.score = score;
	}
	
	@Override
	public int compare(int[] node1, int[] node2) {
		
		if (score[node1[0]][node1[1]] < score[node2[0]][node1[1]]) {
			return -1;
		} else if (score[node1[0]][node1[1]] > score[node2[0]][node1[1]]) {
			return 1;
		} else {
			return 0;
		}
	}
}
