package gui;

import java.awt.*;
import java.util.ArrayList;

// TODO make the validity system work better and add documentation.
public abstract class GUIElement {

	/**
	 * Whether or not the element has changed a state that affect size or
	 * position.
	 */
	protected boolean valid = false;

	/**
	 * Whether or not the element is to be drawn.
	 */
	private boolean visible = true;

	/**
	 * Whether or not the element responds to user input.
	 */
	protected boolean enabled = true;

	/**
	 * Indicates whether this Element can be focused.
	 */
	boolean focusable;

	/**
	 * Position of the element.
	 */
	private int x, y;

	/**
	 * Size of the element.
	 */
	private int width, height;

	protected Dimension minSize;

	protected Dimension prefSize;

	protected Dimension maxSize;

	private Insets insets;

	/**
	 * The parent container that this element belongs to. 'null' means it is the
	 * root element.
	 */
	protected Container parent;

	private Color backgroundColor;
	private Color foregroundColor;

	private ContextBox box;

	ArrayList<ActionListener> listeners;

	/**
	 * Initialize a new GUIElement with size [0,0] and position at [0,0].
	 */
	protected GUIElement() {

		x = 0;
		y = 0;
		width = 0;
		height = 0;
		insets = new Insets(0, 0, 0, 0);
		listeners = new ArrayList<>();
	}

	public void addActionListener(ActionListener actionListener) {

		actionListener.setParent(this);
		this.listeners.add(actionListener);
	}

	/**
	 * Check if the element has valid properties.
	 * 
	 * @return Validity of the element
	 */
	public boolean isValid() {

		return valid;
	}

	/**
	 * Ensure that the element is valid by changing size or position. TODO
	 * elaborate
	 */
	public void revalidate() {

		invalidate();

		Container root = parent;
		if (parent == null) {

			validate();
		} else {

			while (true) {

				if (root.parent == null)
					break;

				root = root.parent;
			}

			root.validate();
		}
	}

	protected void validate() {

		layout();
		box = createBox();
		valid = true;
	}

	protected void layout() {

		setSize(getPreferredSize());
	}

	/**
	 * Invalidate the element. Also invalidate the parent if not already done.
	 * TODO elaborate
	 */
	public void invalidate() {

		valid = false;
		
		if (parent != null) {

			if (parent.isValid()) {

				parent.invalidate();
			}
		}
	}

	/**
	 * Return the name of the element.
	 */
	public String toString() {

		return "[" + x + ", " + y + ", " + width + ", " + height + "]";
	}
	
	public GUIElement getParent() {
		
		return parent;
	}

	/**
	 * Recursively get the topmost element that contains the mouse cursor.
	 * 
	 * @return The topmost element
	 */
	public GUIElement getMouseOver(Point point) {

		if (!isVisible())
			return null;
		if (!this.containsPoint(point))
			return null;

		if (this instanceof Container) {

			for (GUIElement element : ((Container) this).children) {

				GUIElement temp = element.getMouseOver(point);
				if (temp != null)
					return temp;
			}
		}

		return this;
	}

	/**
	 * Whether or not the element bounds a point.
	 * 
	 * @param p
	 *            A point to test
	 * @return Whether the point is contained.
	 */
	public boolean containsPoint(Point p) {

		return containsPoint(p.x, p.y);
	}

	/**
	 * Whether or not the element bounds a point.
	 * 
	 * @param x
	 *            The x value of a point
	 * @param y
	 *            The y value of a point
	 * @return Whether the point is contained.
	 */
	public boolean containsPoint(int x, int y) {

		if ((x <= this.x + width && x >= this.x) && (y <= this.y + height && y >= this.y))
			return true;

		return false;
	}

	public int getX() {

		return x;
	}

	public int getY() {

		return y;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	public void setPosition(Point p) {

		setPosition(p.x, p.y);
	}

	public void setPosition(int x, int y) {

		if (this.x == x && this.y == y)
			return;

		this.x = x;
		this.y = y;

		invalidate();
	}

	public Point getPosition() {

		return new Point(x, y);
	}

	public void translate(int x, int y) {

		setPosition(this.x + x, this.y + y);
	}

	public void setSize(Dimension dim) {

		setSize(dim.width, dim.height);
	}

	public void setSize(int width, int height) {

		if (this.width == width && this.height == height)
			return;

		this.width = width;
		this.height = height;

		invalidate();
	}

	public Dimension getSize() {

		return new Dimension(width, height);
	}

	public Dimension getPreferredSize() {

		Dimension size = prefSize;
		if (size == null)
			size = getSize();
		return size;
	}

	public void setPreferredSize(Dimension prefSize) {

		this.prefSize = prefSize;
	}

	public Dimension getMinimumSize() {

		return minSize;
	}

	public Dimension getMaximumSize() {

		return maxSize;
	}

	public void setBounds(Rectangle rect) {

		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	public void setBounds(int x, int y, int width, int height) {

		setPosition(x, y);
		setSize(width, height);
	}

	public void setBounds(GUIElement element) {

		setBounds(element.x, element.y, element.width, element.height);
	}

	public Rectangle getBounds() {

		return new Rectangle(x, y, width, height);
	}

	public Insets getInsets() {

		return insets;
	}

	public void setInsets(Insets insets) {

		this.insets = insets;
	}

	public boolean isVisible() {

		return visible;
	}

	public void setVisible(boolean visible) {

		this.visible = visible;
	}


	public void setBackgroundColor(Color backgroundColor) {

		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() {

		return backgroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {

		this.foregroundColor = foregroundColor;
	}

	public Color getForegroundColor() {

		return foregroundColor;
	}

	protected abstract ContextBox createBox();

	public ContextBox getBox() {

		if (box == null)
			revalidate();
		return box;
	}
}
