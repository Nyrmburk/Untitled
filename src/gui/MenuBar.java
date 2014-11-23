package gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import main.Engine;
import main.Settings;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

public class MenuBar extends GUIElement {

	private static final long serialVersionUID = -6931203420417782709L;
	
	InputStream inputStream;
	Font awtFont;
	FormattedFont font;
	TextBox time;

	public MenuBar() {
		
		super(new Rectangle(0, Settings.windowHeight - 36 + 2 * GUI.padding, Settings.windowWidth, 36 + 2 * GUI.padding));
		inputStream = ResourceLoader.getResourceAsStream("\\res\\fonts\\roboto\\Roboto-Regular.ttf");
		
		try {
			
			awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(20f);
			inputStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		
		font = new FormattedFont(awtFont, true);
		font.currentAlignment = FormattedFont.alignment.RIGHT;
		time = new TextBox(font, (Rectangle)this, GUI.padding, GUI.padding, GUI.padding, GUI.padding);
	}
	
	@Override
	public void draw() {
		
		GUI.awtToGL(GUI.systemColor);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x, y + height);
		GL11.glVertex2i(x + width, y + height);
		GL11.glVertex2i(x + width, y);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_BLEND);
		font.drawString(0, 0, String.valueOf(Engine.currentFPS));
		time.setText(new Date().toString());
		time.render(0, Color.white);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
