package gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Input;
import main.Settings;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class Button extends GUIElement {
	
	public static final String PRESSED = "pressed";
	public static final String RELEASED = "released";
	public static final String CLICKED = "clicked";
	public static final String ENTERED = "entered";
	public static final String EXITED = "exited";
	
	public static final int FOCUS_COLOR = 0;
	public static final int UNFOCUS_COLOR = FOCUS_COLOR + 1;
	public static final int SELECTED_COLOR = UNFOCUS_COLOR + 1;
	
	public static final int IMAGE_FOCUS_COLOR = SELECTED_COLOR + 1;
	public static final int IMAGE_UNFOCUS_COLOR = IMAGE_FOCUS_COLOR + 1;
	
	private boolean entered = false;
	private boolean pressed = false;
	
	private boolean lastPressed = false;
	
	private Color focusColor = GUI.focusColor;
	private Color unfocusColor = GUI.unfocusColor;
	private Color selectedColor = GUI.iconColor;
	
	private Color imageFocusColor = GUI.systemColor;
	private Color imageUnfocusColor = GUI.iconColor;
	
	private Color currentColor = unfocusColor;
	private int currentColorIdentifier = UNFOCUS_COLOR;
	
	transient ActionListener actionListener;
	
	TextBox textBox;
	Texture texture;
	
	Button() {
		
		super();
	}
	
	Button(String label) {
		
		this();
		
		textBox = new TextBox(GUI.mainFont.clone());
		setBounds(0, 0, textBox.getFont().getWidth(label), textBox.getFont()
				.getHeight());
		textBox.setBounds(this);
		textBox.getFont().horizontalAlignment = FormattedFont.HorizontalAlignment.CENTER;
		textBox.getFont().verticalAlignment = FormattedFont.VerticalAlignment.CENTER;
		textBox.setText(label);
		textBox.setRenderAsTexture(true);
	}
	
	Button(BufferedImage image) {
		
		super();
		
		try {
			texture = org.newdawn.slick.util.BufferedImageUtil.getTexture(this
					.toString(), image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	Button(Texture texture) {
		
		super();
		
		this.texture = texture;
	}
	
	public void update(boolean mouseOver) {
		
		if (mouseOver) {
			
			if (!entered) {
				
				entered = true;
				onEnter();
				
				if (Input.mouseDown[0] && !lastPressed) lastPressed = true;
			}
			
			if (Input.mouseDown[0] && !lastPressed) {
				
				if (!pressed) {
					
					pressed = true;
					onPress();
				}
			} else if (!Input.mouseDown[0]) {
				
				if (pressed || lastPressed) {
					
					if (!pressed) lastPressed = false;
					pressed = false;
					onRelease();
				}
			}
		} else {
			
			if (!Input.mouseDown[0]) {
				
				pressed = false;
				
				if (entered) {
					
					entered = false;
					onExit();
				}
			}
		}
	}
	
	public void draw() {
		
		update(this.containsPoint(new Point(Input.mouseX, Settings.windowHeight
				- Input.mouseY)));
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GUI.awtToGL(currentColor);
		GUI.drawQuad(this);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 1);
		
		GL11.glEnable(GL11.GL_BLEND);
		if (textBox != null)
			textBox.draw(0f, org.newdawn.slick.Color.black);
		else if (texture != null) {
			
			if (!entered)
				GUI.awtToGL(imageUnfocusColor);
			else
				GUI.awtToGL(imageFocusColor);
			texture.bind();
			
			int hOffset = (width - texture.getImageWidth()) / 2;
			int vOffset = (height - texture.getImageHeight()) / 2;
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2i(x + hOffset, y + vOffset);
			GL11.glTexCoord2f(0, texture.getHeight());
			GL11.glVertex2i(x + hOffset, y + vOffset + texture.getImageHeight());
			GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
			GL11.glVertex2i(x + hOffset + texture.getImageWidth(), y + vOffset
					+ texture.getImageHeight());
			GL11.glTexCoord2f(texture.getWidth(), 0);
			GL11.glVertex2i(x + hOffset + texture.getImageWidth(), y + vOffset);
			GL11.glEnd();
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void setColor(int identifier, Color color) {
		
		switch (identifier) {
		
		case FOCUS_COLOR:
			focusColor = color;
			break;
		case UNFOCUS_COLOR:
			unfocusColor = color;
			break;
		case SELECTED_COLOR:
			selectedColor = color;
			break;
		case IMAGE_FOCUS_COLOR:
			imageFocusColor = color;
			break;
		case IMAGE_UNFOCUS_COLOR:
			imageUnfocusColor = color;
			break;
		}
		
		if (currentColorIdentifier == identifier) currentColor = color;
	}
	
	public void setText(String text) {
		
		this.texture.release();
		this.texture = null;
		
		if (textBox == null)
			textBox = new TextBox();
		
		this.textBox.setText(text);
			
	}
	
	public void setImage(Texture texture) {
		
		if (this.texture != null)
			this.texture.release();
		this.texture = texture;
		this.textBox = null;
	}
	
	public void setImage(BufferedImage image) {
		
		try {
			setImage(org.newdawn.slick.util.BufferedImageUtil.getTexture(this
					.toString(), image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void onPress() {
		
		currentColor = selectedColor;
		currentColorIdentifier = SELECTED_COLOR;
		if (actionListener != null)
			actionListener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, PRESSED));
		lastPressed = true;
	}
	
	private void onRelease() {
		
		currentColor = focusColor;
		currentColorIdentifier = FOCUS_COLOR;
		if (actionListener != null)
			actionListener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, RELEASED));
		
		if (lastPressed) onClick();
		lastPressed = false;
	}
	
	private void onClick() {
		
		if (actionListener != null)
			actionListener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, CLICKED));
	}
	
	private void onEnter() {
		
		currentColor = focusColor;
		currentColorIdentifier = FOCUS_COLOR;
		if (actionListener != null)
			actionListener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, ENTERED));
	}
	
	private void onExit() {
		
		currentColor = unfocusColor;
		currentColorIdentifier = UNFOCUS_COLOR;
		if (actionListener != null)
			actionListener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, EXITED));
		lastPressed = false;
	}
	
	public void addActionListener(ActionListener actionListener) {
		
		this.actionListener = actionListener;
	}
	
	@Override
	protected void onPositionChange(int oldX, int oldY) {
		
		if (textBox != null) textBox.translate(x - oldX, y - oldY);
	}
	
	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {
		
		if (textBox != null) textBox.setSize(width, height);
	}
}