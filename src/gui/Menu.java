package gui;

import org.lwjgl.opengl.GL11;

public class Menu extends Panel {
	
	private TextBox title;
	
	public Menu() {
		
		super();
		super.setlayout(new GUIBoxLayout());
		title = new TextBox();
		title.setSize(100, title.getFont().getLineHeight());
		this.addChild("txt_title", title, GUIBoxLayout.LEFT);
	}
	
	public Menu(String title) {
		
		this();
		setTitle(title);
	}
	
	@Override
	public void draw() {
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GUI.awtToGL(GUI.focusColor);
		GUI.drawQuad(this);
		GUI.awtToGL(GUI.systemColor);
		GUI.drawBorder(this);
		GUI.drawBorder(title);
		GL11.glEnable(GL11.GL_BLEND);
		super.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void setVisible(boolean visible) {
		
		super.setVisible(visible);
		
		if (visible)
			GUI.setCurrentMenu(this);
	}
	
	public String getTitle() {
		
		return title.getText();
	}
	
	public void setTitle(String title) {
		
		this.title.setText(title);
	}
	
	public void AddSeparator() {
		
		// TODO Implement some way of adding vertical space or something
	}
}