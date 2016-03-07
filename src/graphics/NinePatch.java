package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nyrmburk on 3/6/2016.
 */
public class NinePatch {

	Rectangle stretch;
	Rectangle padding;

	TextureInterface texture;

	private static final int BLACK = 0xFF000000;

	public NinePatch (BufferedImage image) {

		Strip top = getRange(image.getRGB(1, 0, image.getWidth()-2, 1, null, 0, image.getWidth()-2));
		Strip left = getRange(image.getRGB(0, 1, 1, image.getHeight()-2, null, 0, 1));
		Strip bottom = getRange(image.getRGB(1, image.getHeight()-1, image.getWidth()-2, 1, null, 0, image.getWidth()-2));
		Strip right = getRange(image.getRGB(image.getWidth()-1, 1, 1, image.getHeight()-2, null, 0, 1));

		stretch = new Rectangle(top.start, left.start, top.length, left.length);
		padding = new Rectangle(bottom.start, right.start, bottom.length, right.length);
	}

	private static Strip getRange(int[] colorStrip) {

		int start = -1;
		int length = 0;
		for (int i = 0; i < colorStrip.length; i++) {

			if (colorStrip[i] == BLACK) {

				if (start == -1) {
					start = i;
				} else {
					length = i - start + 1;
				}
			}
		}

		return new Strip(start, length);
	}

	private static class Strip {

		int start;
		int length;

		public Strip(int start, int length) {
			this.start = start;
			this.length = length;
		}
	}
}
