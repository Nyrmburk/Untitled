package graphics;

import java.util.ArrayList;
import java.util.Random;

import main.GeometryHelper;
import world.World;

/**
 * Turn a heightmap into a mesh. TODO Add chunk system.
 * 
 * @author Christopher Dombroski
 */
public class MapMesh extends Model {
	
	Random random = new Random();
	
	private World world;
	
	private static int amountPerRow;
	private final float MID = 0.5f;
	
	public MapMesh(World world) {
		
		this.world = world;
		
		byte[][] worldHeight = world.getWorldHeight();
		amountPerRow = world.getX() * 2 + 1;
		
		listFill(verticesList, new float[] {-1, -1, -1}, world.getY()
				* amountPerRow + world.getX() + 1);
		listFill(colorAmbientList, new float[] {1, 1, 1}, world.getY()
				* amountPerRow + world.getX() + 1);
		listFill(colorDiffuseList, new float[] {1, 1, 1}, world.getY()
				* amountPerRow + world.getX() + 1);
		
		for (int y = 0; y < world.getY(); y++) {
			
			for (int x = 0; x < world.getX(); x++) {
				
				float tempHeight = worldHeight[x][y] + random.nextFloat() / 4
						- 0.125f;
				// float tempHeight = worldHeight[x][y];
				
				int bottomLeftIndex = x + y * amountPerRow;
				int bottomRightIndex = bottomLeftIndex + 1;
				int upperLeftIndex = bottomLeftIndex + amountPerRow;
				int upperRightIndex = upperLeftIndex + 1;
				int centerIndex = bottomLeftIndex + world.getX() + 1;
				
				float bottomLeftHeight = tempHeight;
				float bottomRightHeight = tempHeight;
				float upperLeftHeight = tempHeight;
				float upperRightHeight = tempHeight;
				float centerHeight = tempHeight;
				
				if (0 < x && 0 < y) {
					bottomLeftHeight = (tempHeight
							+ (float) worldHeight[x - 1][y]
							+ (float) worldHeight[x - 1][y - 1] + 
							(float) worldHeight[x][y - 1]) / 4;
				}
				
				if (x < world.getX() - 1 && 0 < y) {
					bottomRightHeight = (tempHeight
							+ (float) worldHeight[x + 1][y]
							+ (float) worldHeight[x + 1][y - 1] + 
							(float) worldHeight[x][y - 1]) / 4;
				}
				
				if (0 < x && y < world.getY() - 1) {
					upperLeftHeight = (tempHeight
							+ (float) worldHeight[x - 1][y]
							+ (float) worldHeight[x - 1][y + 1] + 
							(float) worldHeight[x][y + 1]) / 4;
				}
				
				if (x < world.getX() - 1 && y < world.getY() - 1) {
					upperRightHeight = (tempHeight
							+ (float) worldHeight[x + 1][y]
							+ (float) worldHeight[x + 1][y + 1] + 
							(float) worldHeight[x][y + 1]) / 4;
				}
				
				centerHeight = (bottomLeftHeight + bottomRightHeight
						+ upperLeftHeight + upperRightHeight) / 4;
				
				if (0 < x && x < world.getX() - 1 && 0 < y
						&& y < world.getY() - 1) {
					
					if (world.isBlocked(x, y)) {
						colorDiffuseList.set(centerIndex, new float[] {1f, 0f,
								0f});
						colorAmbientList.set(centerIndex, new float[] {1f, 0f,
								0f});
					} else {
						colorDiffuseList.set(centerIndex, new float[] {0f, 1f,
								0f});
						colorAmbientList.set(centerIndex, new float[] {0f, 1f,
								0f});
					}
				} else {
					
					colorDiffuseList.set(centerIndex, new float[] {0f, 0f, 1f});
					colorAmbientList.set(centerIndex, new float[] {0f, 0f, 1f});
				}
				
				verticesList.set(bottomLeftIndex, new float[] {x - MID,
						y - MID, bottomLeftHeight});
				verticesList.set(bottomRightIndex, new float[] {x + MID,
						y - MID, bottomRightHeight});
				verticesList.set(upperLeftIndex, new float[] {x - MID, y + MID,
						upperLeftHeight});
				verticesList.set(upperRightIndex, new float[] {x + MID,
						y + MID, upperRightHeight});
				verticesList.set(centerIndex, new float[] {x, y, centerHeight});
				
				indicesList.add(new int[] {bottomLeftIndex, bottomRightIndex,
						centerIndex});
				indicesList.add(new int[] {bottomRightIndex, upperRightIndex,
						centerIndex});
				indicesList.add(new int[] {upperRightIndex, upperLeftIndex,
						centerIndex});
				indicesList.add(new int[] {upperLeftIndex, bottomLeftIndex,
						centerIndex});
			}
		}
		
		super.vertexStride = 3;
		super.indicesStride = 3;
		
		super.initAll();
	}
	
	private static void listFill(ArrayList<float[]> list, float[] element,
			int size) {
		list.ensureCapacity(size);
		
		for (int i = 0; i < size; i++) {
			list.add(element);
		}
	}
	
	public float getHeight(float[] location) {
		
		int x = (int) Math.rint(location[0]);
		int y = (int) Math.rint(location[1]);
		
		int bottomLeftIndex = x + y * amountPerRow;
		int bottomRightIndex = bottomLeftIndex + 1;
		int upperLeftIndex = bottomLeftIndex + amountPerRow;
		int upperRightIndex = upperLeftIndex + 1;
		int centerIndex = bottomLeftIndex + world.getX() + 1;
		
		float[] bottomLeft = verticesList.get(bottomLeftIndex);
		float[] bottomRight = verticesList.get(bottomRightIndex);
		float[] upperLeft = verticesList.get(upperLeftIndex);
		float[] upperRight = verticesList.get(upperRightIndex);
		float[] center = verticesList.get(centerIndex);
		
		float[] point1;
		float[] point2;
		
		if (GeometryHelper.isLeft(bottomLeft, upperRight, location) > 0) {
			if (GeometryHelper.isLeft(upperLeft, bottomRight, location) > 0) {
				
				point1 = upperLeft;
				point2 = upperRight;
			} else {
				
				point1 = bottomLeft;
				point2 = upperLeft;
			}
		} else {
			if (GeometryHelper.isLeft(upperLeft, bottomRight, location) > 0) {
				
				point1 = upperRight;
				point2 = bottomRight;
			} else {
				
				point1 = bottomRight;
				point2 = bottomLeft;
			}
		}
		
		return GeometryHelper.triangleHeight(location, point1, point2, center);
	}
}
