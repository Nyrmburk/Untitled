package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Nyrmburk on 2/26/2016.
 */
public class GraphicsFont {

	private static final int CHARS = 256;

	private Texture atlas;

	private FontMetrics fontMetrics;

	private Font font;

	private Rectangle[] bounds = new Rectangle[CHARS];

	private static final int OFFSET = 2;

	public GraphicsFont(Font font) {

		this.setFont(font);

		//create a Graphics2D to get the character sizes
		Graphics2D fontGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
		fontGraphics.setFont(font);
		fontGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		setFontMetrics(fontGraphics.getFontMetrics());

		// create an array of all the characters from 0 - 255
		char[] chars = new char[CHARS];
		for (char i = 0; i < CHARS; i++)
			chars[i] = i;

		// create a list to sort and hold the bounds and character
		SortedSet<CharRect> sortedRects = new TreeSet<CharRect>();

		//for all the characters (except invisible ones) get their sizes
		for (char i = 0; i < CHARS; i++) {

			if (Character.isWhitespace(i) || !font.canDisplay(i))
				continue;

			getBounds()[i] = new Rectangle(0, 0, getFontMetrics().charWidth(i), getFontMetrics().getHeight() + OFFSET);
			getBounds()[i].width += OFFSET;//fix an issue with chars overlapping
			sortedRects.add(new CharRect(i, getBounds()[i]));
		}

		//Pack the character bounds into the smallest multiple of 2 rectangle
		Packer packer = new Packer();
		packer.fit(sortedRects);

		int width = packer.getSize().width;
		int height = packer.getSize().height;

		// create an intermediate image to render the text to
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = image.createGraphics();
		gr.setFont(font);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		gr.setColor(Color.WHITE);

		//render each char and also update the char bounds
		for (CharRect rect : sortedRects) {

//			bounds[rect.c].width -= 2;//fix an issue with chars overlapping
//			gr.setColor(Color.GRAY);
//			gr.fillRect(rect.x, rect.y, rect.width, rect.height);
			gr.setColor(Color.WHITE);
			if (rect.c == 'j')
				rect.x++;
			gr.drawChars(new char[]{rect.c}, 0, 1, rect.x + 1, rect.y + getFontMetrics().getAscent());
			rect.width -= OFFSET - 1;
			rect.height -= OFFSET - 1;
			getBounds()[rect.c].setBounds(rect);
		}

		setAtlas(new Texture());
		getAtlas().setTexture(image);

//		try {
//			ImageIO.write(image, "png", new File(font.toString() + ".png"));
//			System.out.println("picture done");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("failed");
//		}
	}

	/**
	 * The texture that holds the characters
	 */
	public Texture getAtlas() {
		return atlas;
	}

	public void setAtlas(Texture atlas) {
		this.atlas = atlas;
	}

	/**
	 * FontMetrics for getting the advance of the characters
	 */
	public FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public void setFontMetrics(FontMetrics fontMetrics) {
		this.fontMetrics = fontMetrics;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * pixel bounds of each character
	 */
	public Rectangle[] getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle[] bounds) {
		this.bounds = bounds;
	}

	private class Packer {

		Node root;
		int width = 1;
		int height = 1;

		public Packer() {

			root = new Node();
		}

		public Dimension getSize() {

			return new Dimension(width, height);
		}

		public void fit(SortedSet<CharRect> rects) {

			Node node;
			for (CharRect rect : rects) {

				if ((node = findNode(root, rect.width, rect.height, 0)) != null) {
					rect.setLocation(splitNode(node, rect.width, rect.height));
				} else {
					rect.setLocation(growNode(rect.width, rect.height));
				}
			}
		}

		private Node findNode(Node node, int width, int height, int depth) {

			Node foundNode = null;
			if (node.used) {
				foundNode = findNode(node.right, width, height, ++depth);
				depth--;
				if (foundNode == null && depth == 0)
					foundNode = findNode(node.down, width, height, depth);
			} else if (width <= (this.width - node.x) && (height <= this.height - node.y)) {
				foundNode = node;
			}
			return foundNode;
		}

		private Node splitNode(Node node, int width, int height) {

			node.used = true;
			node.down = new Node(node);
			node.down.translate(0, height);
			node.right = new Node (node);
			node.right.translate(width, 0);
			return node;
		}

		private Node growNode(int width, int height) {

			Node grown = null;

			int minWidth = powerOfTwo(this.width + width);
			int minHeight = powerOfTwo(this.height + height);

			boolean growRight = this.width <= this.height;
			boolean growDown = !growRight;

			if (width > this.width && height > this.height) {
				growDown = true;
				growRight = true;
			}

			if (growDown) {
				this.height = minHeight;
			}
			if (growRight) {
				this.width = minWidth;
			}

			if ((grown = findNode(root, width, height, 0)) != null) {
				grown = splitNode(grown, width, height);
			} else {
				grown = null;
			}

			return grown;
		}

		private int powerOfTwo(int v) {

			v--;
			v |= v >> 1;
			v |= v >> 2;
			v |= v >> 4;
			v |= v >> 8;
			v |= v >> 16;
			v++;
			return v;
		}

		private class Node extends Point {

			boolean used = false;
			Node right = null;
			Node down = null;

			public Node () {
				super();
			}

			public Node (Point point) {
				super(point);
			}
		}
	}

	private class CharRect extends Rectangle implements Comparable<CharRect> {

		char c;

		public CharRect(char c, Rectangle rect) {

			super(rect);
			this.c = c;
		}

		@Override
		//comparator is backward so that the list sorts from biggest to smallest
		public int compareTo(CharRect rect) {
			int compare = rect.width - width;
			if (compare == 0)
				compare = rect.c - c;
			return compare;
		}
	}
}
