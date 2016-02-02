package gui;

import org.lwjgl.opengl.GL11;

public class Menu extends Panel {
	
	private TextBox title;
	
	public Menu() {
		
		super();
		super.setlayout(new GUIBoxLayout());
		title = new TextBox();
//		title.setSize(100, title.getFont().getLineHeight());
//		title.setText(this.getName());
		this.addChild(title, GUIBoxLayout.LEFT);
//		TextBox textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("LEFT");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("CENTER");
//		textBox.getFont().horizontalAlignment = FormattedFont.HorizontalAlignment.CENTER;
//		this.addChild(textBox, GUIBoxLayout.CENTER);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("RIGHT");
//		textBox.getFont().horizontalAlignment = FormattedFont.HorizontalAlignment.RIGHT;
//		this.addChild(textBox, GUIBoxLayout.RIGHT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("ordered");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("strings");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("based");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("upon");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("time");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("of");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
//		textBox = new TextBox();
//		textBox.setSize(100, title.getFont().getLineHeight());
//		textBox.setText("insertion");
//		this.addChild(textBox, GUIBoxLayout.LEFT);
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
//		GUI.drawBorder(title);
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