package graphics;

import main.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Texture extends Resource {

	private String name;
	private BufferedImage texture;

	public BufferedImage getTexture() {

		return texture;
	}

	public void setTexture(BufferedImage texture) {

		if (this.texture == null) {

			this.texture = texture;
		} else {

			//TODO make an update listener
			System.err.println("cannot update texture after it has already been set");
		}
	}

	public int getWidth() {

		return texture.getWidth();
	}

	public int getHeight() {

		return texture.getHeight();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void save(Path path) throws IOException {

		ImageIO.write(texture, "PNG", path.toFile());
	}

	@Override
	public void load(Path path) throws IOException {

		texture = ImageIO.read(path.toFile());
		name = path.getFileName().toString();
	}
}
