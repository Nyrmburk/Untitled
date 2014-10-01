package graphics;

import world.World;

public class MapMesh extends Model {

	public MapMesh(World world) {

		byte[][] worldHeight = world.getWorldHeight();

		int i = 0;
		for (int y = 0; y < world.getY(); y++) {

			for (int x = 0; x < world.getX(); x++) {

				super.verticesList.add(new float[] { x, y,
						worldHeight[x][y]});
				
//				super.colorDiffuseList.add(new float[]{1f, 0f, 0f});
//				super.colorAmbientList.add(new float[]{1f, 0f, 0f});

				if (y < world.getY() - 1 && x < world.getX() - 1) {
					super.indicesList.add(new int[] { i, 1 + i,
							world.getX() + i });
					super.indicesList.add(new int[] { world.getX() + i, 1 + i,
							world.getX() + 1 + i });
					
					if (Math.abs(worldHeight[x][y] - worldHeight[x+1][y+1]) > 1
							| Math.abs(worldHeight[x][y] - worldHeight[x+1][y]) > 1
							| Math.abs(worldHeight[x][y] - worldHeight[x][y+1]) > 1) {
						super.colorDiffuseList.add(new float[]{1f, 0f, 0f});
						super.colorAmbientList.add(new float[]{1f, 0f, 0f});
					} else {
						super.colorDiffuseList.add(new float[]{0f, 1f, 0f});
						super.colorAmbientList.add(new float[]{0f, 1f, 0f});
					}
				} else {
					super.colorDiffuseList.add(new float[]{0f, 1f, 0f});
					super.colorAmbientList.add(new float[]{0f, 1f, 0f});
				}
				i++;
			}
		}

		super.vertexStride = 3;
		super.indicesStride = 3;

		super.initAll();
	}
}
