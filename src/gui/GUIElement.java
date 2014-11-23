package gui;

import java.awt.Rectangle;

import graphics.Drawable;

public abstract class GUIElement extends Rectangle implements Drawable {

	private static final long serialVersionUID = -1244664770173876380L;

	public GUIElement(int x, int y, int width, int height) {
		
		super(x, y, width, height);
	}
	
	public GUIElement(Rectangle rect) {
		
		super(rect);
	}
	
	@Override
	public void draw() {
	}
}
