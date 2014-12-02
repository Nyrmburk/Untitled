package gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import main.Input;
import main.Settings;

import graphics.Drawable;

public abstract class GUIElement implements Drawable {
	
	protected boolean valid = false;
	
	private boolean visible = true;
	
	private String name;
	
	protected int x, y;
	protected int width, height;
	
	protected GUIElement() {
		
		x = 0;
		y = 0;
		width = 1;
		height = 1;
	}
	
	public abstract void draw();
	
	public boolean isValid() {
		
		return valid;
	}
	
	public void revalidate() {
		
		valid = true;
	}
	
	public String toString() {
		
		return getName();
	}
	
	public String getName() {
		
		if (name != null) {
			
			return name;
		} else {
			
			return getClass().getName() + '@' + Integer.toHexString(hashCode());
		}
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public GUIElement getMouseOver() {
		
		if (!isVisible()) return null;
		if (!this.containsPoint(Input.mouseX, Settings.windowHeight - Input.mouseY))
			return null;
		
		if (this instanceof Container) {
			
			for (GUIElement element : ((Container) this).children) {
				
				GUIElement temp = element.getMouseOver();
				if (temp != null) return temp;
			}
		}
		
		return this;
	}
	
	public boolean containsPoint(Point p) {
		
		return containsPoint(p.x, p.y);
	}
	
	public boolean containsPoint(int x, int y) {
		
		if ((x <= this.x + width && x >= this.x) && 
				(y <= this.y + height && y >= this.y))
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
		
		onSizeChange(tempWidth, tempHeight);
	}
	
	public Dimension getSize() {
		
		return new Dimension(width, height);
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
	
	public boolean isVisible() {
		
		return visible;
	}
	
	public void setVisible(boolean visible) {
		
		this.visible = visible;
	}
	
	protected abstract void onPositionChange(int oldX, int oldY);
	protected abstract void onSizeChange(int oldWidth, int oldHeight);
}
