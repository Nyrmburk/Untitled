package graphics;

import main.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

public class Texture extends Resource {

	private String name;
	private ByteBuffer textureData;

	private int width;
	private int height;
	private int actualWidth;
	private int actualHeight;

	public ByteBuffer getTexture() {

		return textureData.asReadOnlyBuffer();
	}

	public void setTexture(ByteBuffer textureData) {

		if (textureData != null) {
			//TODO make updating possible
			System.err.println("cannot update data");
			return;
		}

		if (!textureData.isDirect())
			textureData = ByteBuffer.allocateDirect(textureData.capacity()).put(textureData);

		this.textureData = textureData;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	public int getActualWidth() {

		return actualWidth;
	}

	public int getActualHeight() {

		return actualHeight;
	}

	public float getWidthRatio() {

		return ((float) width) / actualWidth;
	}

	public float getHeightRatio() {

		return ((float) height) / actualHeight;
	}

	public float xRatio(int x) {

		return ((float) x) / width * getWidthRatio();
	}

	public float yRatio(int y) {

		return ((float) y) / height * getHeightRatio();
	}

	public void setDataFromImage(BufferedImage image) {

		width = image.getWidth();
		height = image.getHeight();
		actualWidth = getMinimumPowerOfTwo(width);
		actualHeight = getMinimumPowerOfTwo(height);

		textureData = ByteBuffer.allocateDirect(actualWidth * actualHeight * Integer.BYTES);

		IntBuffer pixels = textureData.asIntBuffer();

		// texture data is organized in rows starting at the bottom
		// texture data is also stored in rgba format
		for (int y = 0; y < height; y++) {

			pixels.position(y * actualWidth);
			for (int x = 0; x < width; x++) {

				int pixel = image.getRGB(x, height - y - 1);
				pixel = pixel << 8 | pixel >>> 24; //convert argb to rgba
				pixels.put(pixel);
			}
		}

		textureData.rewind();
	}

	public BufferedImage getImageFromData() {

		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {

				int pixel = textureData.get();
				pixel = pixel >>> 8 | pixel << 24; //convert rgba to argb
				image.setRGB(x, height - y - 1, pixel);
			}
		}

		textureData.rewind();

		return image;
	}

	private int getMinimumPowerOfTwo(int value) {

		int powerOfTwo = 2;// opengl minimum texture size is 64x64?

		while (powerOfTwo < value)
			powerOfTwo += powerOfTwo; // addition is faster than multiplication

		return powerOfTwo;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void save(Path path) throws IOException {

		ImageIO.write(getImageFromData(), "PNG", path.toFile());
	}

	@Override
	public void load(Path path) throws IOException {

		setDataFromImage(ImageIO.read(path.toFile()));
		name = path.getFileName().toString();
	}
}
