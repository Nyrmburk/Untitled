package gui;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.Date;

import main.AssetManager;
import main.Engine;
import main.Settings;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class MenuBar extends Panel {
	
	InputStream inputStream;
	FormattedFont font;
	TextBox time;
	
	public MenuBar() {
		
		super();
		
		this.setBounds(0,
				Settings.windowHeight - GUI.content - 2 * GUI.padding,
				Settings.windowWidth, GUI.content + 2 * GUI.padding);
		
		font = GUI.mainFont;
		font.horizontalAlignment = FormattedFont.HorizontalAlignment.RIGHT;
		font.verticalAlignment = FormattedFont.VerticalAlignment.CENTER;
		time = new TextBox(font);
		time.setBounds(this);
		time.setInsets(0, 0, 0, 5);
		Button btn = new Button(AssetManager.getTexture("menu.gif"));
		btn.setColor(Button.UNFOCUS_COLOR, new java.awt.Color(0, 0, 0, 0));
		btn.addActionListener(new java.awt.event.ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				printEvent(e);
			}
		});
		btn.setBounds(GUI.padding, Settings.windowHeight - GUI.padding
				- GUI.content, GUI.content, GUI.content);
		this.addChild("MyButton", btn);
		
		// time.setText("hello, world!");
		// time.setRenderAsTexture(true);
	}
	
	@Override
	public void draw() {
		
		GUI.awtToGL(GUI.systemColor);
		GUI.drawQuad(this);
		
		renderChildren();
		
		GL11.glEnable(GL11.GL_BLEND);
		
		time.setText(new Date().toString());
		// time.setRenderAsTexture(true);
		time.draw(0, Color.white);
		font.drawString(0, 0, String.valueOf(Engine.currentFPS + ", "
				+ Engine.frameQuality));
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void printEvent(java.awt.event.ActionEvent evt) {
		
//		System.out.println("menu button " + evt.getActionCommand());
		if (evt.getActionCommand() == Button.CLICKED) {
			AssetManager.getScript("menu_click.js").eval();
		}
	}
}
