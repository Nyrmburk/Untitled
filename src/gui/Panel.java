package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import graphics.UIRenderEngine;

public class Panel extends Container {
	
	private BufferedImage image;
	
	private Color backgroundColor = new Color(0x00000000, true);
	private Color foregroundColor = new Color(0x00000000, true);;
	
	public Panel() {
		super();
	}
	
	public Panel(GUILayoutManager layout) {
		super(layout);
	}
	
	@Override
	public BufferedImage render(UIRenderEngine renderEngine) {
		
		return renderEngine.renderPanel(this);
	}
	
	@Override
	public void draw() {
		
		renderChildren();
	}
	
	public void setImage(BufferedImage image) {
		
		if (autoHeight && autoWidth)
			this.setBounds(this.x, this.y, image.getWidth(), image.getHeight());
		
		this.image = image;
	}
	
	public BufferedImage getImage() {
		
		return image;
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
	
	@Override
	protected void onPositionChange(int oldX, int oldY) {
		
		for (GUIElement child : children) {
			
			child.translate(x - oldX, y - oldY);
		}
	}
	
	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {
		
		invalidate();
		
//		int deltaWidth = width - oldWidth;
//		int deltaHeight = height - oldHeight;
//		
//		for (GUIElement child : children) {
//			
//			child.setSize(child.width + deltaWidth, child.height + deltaHeight);
//		}
	}
}
