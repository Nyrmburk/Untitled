package gui;

import graphics.TextureInterface;

import java.awt.*;

public class Panel extends Container {
	
	private TextureInterface image;
	
	public Panel() {
		super();
	}
	
	public Panel(GUILayoutManager layout) {
		super(layout);
	}
	
	public void setImage(TextureInterface image) {

		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		
		this.image = image;
	}
	
	public TextureInterface getImage() {
		
		return image;
	}

	@Override
	protected ContextBox createBox() {

		ContextBox box = new ContextBox();
		box.setBounds(this.getBounds());
		box.color = getBackgroundColor();

		box.texture = image;

		return box;
	}
}
