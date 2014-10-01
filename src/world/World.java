package world;

import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

/**
 * Hold the world information, save it, and load it.
 * 
 * @author Christopher Dombroski
 * 
 */
public class World {
	
	public World(int worldX, int worldY) {
		this.worldX = worldX;
		this.worldY = worldY;
		
		worldHeight = new byte[worldX][worldY];
		worldType = new byte[worldX][worldY];
		structureType = new ArrayList<Byte>();
		items = new ArrayList<Byte>();
		minedBlocks = new ArrayList<Byte>();
		
		Random random = new Random();
		for (int y = 0; y < worldY; y++) {
			for (int x = 0; x < worldX; x++) {
				
				worldHeight[x][y] = (byte) (random.nextFloat() * 3);
			}
		}
	}

	// int world x, int world y, int structure length, int items length, int mined length.
	private static final byte HEADER_SIZE = 4 + 4 + 4 + 4 + 4;

	//world size
	private int worldX, worldY;

	private byte[][] worldHeight;
	private byte[][] worldType;

	private ArrayList<Byte> structureType;
	private ArrayList<Byte> items;
	private ArrayList<Byte> minedBlocks;

	private byte[] getHeader() {

		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);

		buffer.putInt(worldX);
		buffer.putInt(worldY);
		buffer.putInt(structureType.size());
		buffer.putInt(minedBlocks.size());

		return buffer.array();
	}

	public void saveWorld(String fileName) throws IOException {

		FileOutputStream out = null;

		fileName += ".world";

		try {
			out = new FileOutputStream(fileName);

			out.write(getHeader());

			for (byte[] data : worldHeight) {
				out.write(data);
			}
			for (byte[] data : worldType) {
				out.write(data);
			}
			for (Byte data : structureType) {
				out.write(data.byteValue());
			}
			for (Byte data : items) {
				out.write(data.byteValue());
			}
			for (Byte data : minedBlocks) {
				out.write(data.byteValue());
			}

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	public void loadWorld(String fileName) throws IOException {
		
		FileInputStream in = null;

		fileName += ".world";
		
		try {
			in = new FileInputStream(fileName);
			
			worldX = readInt(in);
			worldY = readInt(in);
			
			int structureLength = readInt(in);
			int itemsLength = readInt(in);
			int minedLength = readInt(in);
			
			worldHeight = new byte[worldX][worldY];
			for (int y = 0; y < worldY; y++) {
				for(int x = 0; x < worldX; x++) {
					
					worldHeight[x][y] = (byte) in.read();
				}
			}
			
			worldType = new byte[worldX][worldY];
			for (int y = 0; y < worldY; y++) {
				for(int x = 0; x < worldX; x++) {
					
					worldType[x][y] = (byte) in.read();
				}
			}
			
			structureType = new ArrayList<Byte>(structureLength);
			for (int i = 0; i < structureLength; i++) {
				structureType.add(new Byte((byte)in.read()));
			}
			
			items = new ArrayList<Byte>(itemsLength);
			for (int i = 0; i < structureLength; i++) {
				items.add(new Byte((byte)in.read()));
			}
			
			minedBlocks = new ArrayList<Byte>(minedLength);
			for (int i = 0; i < minedLength; i++) {
				minedBlocks.add(new Byte((byte)in.read()));
			}

		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	public void loadFromImage() {
		
		BufferedImage bImage = null;
		try {
			bImage = ImageIO.read(new File("res\\heightmap.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		worldX = bImage.getWidth();
		worldY = bImage.getHeight();
		
		worldHeight = new byte[worldX][worldY];
		
		for (int y = 0; y < worldY; y++) {
			for (int x = 0; x < worldX; x++) {
				
				worldHeight[x][y] = (byte) ((bImage.getRGB(x, y) / 2) & 0x7F);
			}
		}
	}
	
	private static int readInt(FileInputStream in) throws IOException {
		return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
	}
	
	public int getX() {
		
		return worldX;
	}
	
	public int getY() {
		
		return worldY;
	}
	
	public byte[][] getWorldHeight() {
		
		return worldHeight.clone();
	}
}
