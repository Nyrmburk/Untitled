package gui.css;

import graphics.TextureInterface;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSImageBox extends CSSBox {

	private TextureInterface[] textures;
	private int[] delays;//milliseconds before updating
	private int frameIndex = 0;
	private boolean loop;//loop the though the images
	private long time = 0;
	private int frameTime = 0;

	{
		transparent = true;
	}

	public CSSImageBox(TextureInterface... texture) {

		this.textures = texture;
		delays = null;
	}

	public CSSImageBox(TextureInterface[] textures, int[] delays) {

		this.textures = textures;
		this.delays = delays;
	}

	/**
	 * Return the current frame for the current time.
	 *
	 * @param delta    The amount of time that has passed since the last call
	 * @return	current frame
	 */
	public TextureInterface getTexture(int delta) {

		if (time > frameTime) {
			frameTime += delays[frameIndex];
			frameIndex++;
		}

		if (frameIndex > textures.length) {
			if (loop) {
				frameIndex = 0;
			} else {
				frameIndex--;
			}
		}

		time += delta;
		return textures[frameIndex];
	}

	public void setLooping(boolean loop) {

		this.loop = loop;
	}
}
