package world;

public class Zone {
	
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

	public int[] getCenter() {
		
		return new int[]{x + width / 2, y + height / 2};
	}
}