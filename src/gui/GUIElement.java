package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import activity.Activity;
import graphics.TextureInterface;
import graphics.UIRenderEngine;
import main.Engine;

// TODO make the validity system work better and add documentation.
public abstract class GUIElement {

	/**
	 * Whether or not the element has changed a state that affect size or
	 * position.
	 */
	protected boolean valid = false;

	/**
	 * Whether or not the element needs to be painted
	 */
	protected boolean repaint = true;

	/**
	 * Whether or not the element is to be drawn.
	 */
	private boolean visible = true;

	/**
	 * Whether or not the element responds to user input.
	 */
	protected boolean enabled = true;

	/**
	 * The name of the element. A good name consists of a prefix and a name. An
	 * example is 'btn_menu'.
	 */
	private String name;

	/**
	 * Position of the element.
	 */
	protected int x, y;

	/**
	 * Size of the element.
	 */
	protected int width, height;

	//TODO remove
	/**
	 * Allows an element to set it's own width.
	 */
	protected boolean autoWidth = true;

	//TODO remove
	/**
	 * Allows an element to set it's own height.
	 */
	protected boolean autoHeight = true;
	
	
	public enum layout {
		
		FILL_PARENT,
		WRAP_CONTENT,
		DISCRETE
	}
	
	protected layout widthLayout = layout.WRAP_CONTENT;
	protected layout heightLayout = layout.WRAP_CONTENT;

	/**
	 * The parent container that this element belongs to. 'null' means it is the
	 * root element.
	 */
	Container parent;

	ArrayList<ActionListener> listeners;

	public static final String UPDATE = "update";

	/**
	 * Initialize a new GUIElement with size [0,0] and position at [0,0].
	 */
	protected GUIElement() {

		x = 0;
		y = 0;
		width = 64;
		height = 64;
		listeners = new ArrayList<ActionListener>();
	}

	
	/**
	 * TODO remove because this functionality is bad
	 */
	public void update() {

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, UPDATE));
	}

	public void addActionListener(ActionListener actionListener) {

		this.listeners.add(actionListener);
	}

	/**
	 * Render the element as intended.
	 */
	public abstract void draw();
	
	public abstract BufferedImage render(UIRenderEngine renderEngine);

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

		switch (widthLayout) {
		case DISCRETE:
			break;
		case FILL_PARENT:
			
			if (parent != null) {
				x = parent.getX();
				width = parent.getWidth();
			} else {
				x = 0;
				width = Engine.renderEngine.getWidth();
			}
			break;
		case WRAP_CONTENT:
//			pack();
			break;
		}
		
		switch (heightLayout) {
		case DISCRETE:
			break;
		case FILL_PARENT:
			
			if (parent != null) {
				y = parent.getY();
				height = parent.getHeight();
			} else {
				y = 0;
				height = Engine.renderEngine.getHeight();
			}
			break;
		case WRAP_CONTENT:
//			pack();
			break;
		}
			
		valid = true;

		if (repaint) {

			TextureInterface texture = Engine.renderEngine.getTextureFromImage(this.render(Engine.UIRenderEngine));
			Activity.currentActivity().getRenderContext().putElement(this, texture);
			repaint = false;
		}

		// System.out.println(getName() + " Revalidated");
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

		return getName();
	}

	/**
	 * Return the name of the element. If a name has not been set, It generates
	 * one identical to Java's default object <code>toString()</code>.
	 * 
	 * @return The GUIElement's name.
	 */
	public String getName() {

		if (name != null) {

			return name;
		} else {

			return getClass().getName() + '@' + Integer.toHexString(hashCode());
		}
	}

	/**
	 * Set the element's name.
	 * 
	 * @param name
	 *            The name to set the element to.
	 */
	public void setName(String name) {

		this.name = name;
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

		int tempX = this.x;
		int tempY = this.y;

		this.x = x;
		this.y = y;

		invalidate();

		onPositionChange(tempX, tempY);
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

		int tempWidth = this.width;
		int tempHeight = this.height;

		this.width = width;
		this.height = height;

		if (this.width != tempWidth)
			autoWidth = false;
		if (this.height != tempHeight)
			autoHeight = false;

		this.repaint = true;
		invalidate();

		onSizeChange(tempWidth, tempHeight);
	}

	public Dimension getSize() {

		return new Dimension(width, height);
	}

	public void setAutoWidth(boolean auto) {

		this.autoWidth = auto;
	}

	public void setAutoHeight(boolean auto) {

		this.autoHeight = auto;
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
	
	public void setWidthLayout(layout widthLayout) {
		
		this.widthLayout = widthLayout;
		invalidate();
	}
	
	public void setHeightLayout(layout heightLayout) {
		
		this.heightLayout = heightLayout;
		invalidate();
	}
	
	public void pack() {
		
	}

	public boolean isVisible() {

		return visible;
	}

	public void setVisible(boolean visible) {

		this.visible = visible;
	}

	protected abstract void onPositionChange(int oldX, int oldY);

	protected abstract void onSizeChange(int oldWidth, int oldHeight);
}
