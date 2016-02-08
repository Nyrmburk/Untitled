package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import main.Engine;
import graphics.TextureInterface;
import graphics.UIRenderEngine;
import input.InputContext;

public class Button extends GUIElement implements PointerListener {

	public static final String PRESSED = "pressed";
	public static final String RELEASED = "released";
	public static final String CLICKED = "clicked";
	public static final String ENTERED = "entered";
	public static final String EXITED = "exited";

	public static final int FOCUS_COLOR = 0;
	public static final int UNFOCUS_COLOR = 1;
	public static final int SELECTED_COLOR = 2;

	public static final int IMAGE_FOCUS_COLOR = 3;
	public static final int IMAGE_UNFOCUS_COLOR = 4;

	private boolean lastPressed = false;

	private Color focusColor = GUI.focusColor;
	private Color unfocusColor = GUI.unfocusColor;
	private Color selectedColor = GUI.iconColor;

	private Color currentColor = unfocusColor;
	private int currentColorIdentifier = UNFOCUS_COLOR;

	// transient ActionListener actionListener;

	TextBox textBox;
	TextureInterface texture;
	
	public Button() {

		super();
//		new java.awt.Button();
	}

	Button(String label) {

		this();

		textBox = new TextBox(GUI.mainFont.clone());
		// setBounds(0, 0, textBox.getFont().getWidth(label), textBox.getFont()
		// .getHeight());
		// textBox.setBounds(this);
		textBox.getFont().horizontalAlignment = FormattedFont.HorizontalAlignment.CENTER;
		textBox.getFont().verticalAlignment = FormattedFont.VerticalAlignment.CENTER;
		textBox.setText(label);
		// textBox.setRenderAsTexture(true);
	}

	Button(BufferedImage image) {

		super();

		texture = Engine.renderEngine.getTextureFromImage(image);
	}

	Button(TextureInterface texture) {

		super();

		this.texture = texture;
	}

	public void update(boolean mouseOver) {

//		if (mouseOver) {
//
//			if (!entered) {
//
//				entered = true;
//				onEnter();
//
//				if (context.pointerDown() && !lastPressed)
//					lastPressed = true;
//			}
//
//			if (context.pointerDown() && !lastPressed) {
//
//				if (!pressed) {
//
//					pressed = true;
//					onPress();
//				}
//			} else if (!context.pointerDown()) {
//
//				if (pressed || lastPressed) {
//
//					if (!pressed)
//						lastPressed = false;
//					pressed = false;
//					onRelease();
//				}
//			}
//		} else {
//
//			if (!context.pointerDown()) {
//
//				pressed = false;
//
//				if (entered) {
//
//					entered = false;
//					onExit();
//				}
//			}
//		}
	}
	
	@Override
	public BufferedImage render(UIRenderEngine renderEngine) {
		
		return renderEngine.renderButton(this);
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
		}

		if (currentColorIdentifier == identifier)
			currentColor = color;
	}
	
	public Color getColor() {
		
		return currentColor;
	}

	public void setText(String text) {

		if (this.texture != null)
			this.texture.release();
		this.texture = null;

		if (textBox == null)
			textBox = new TextBox();

		this.textBox.setText(text);

		if (this.autoWidth)
			this.width = textBox.width + textBox.getInsets().left
					+ textBox.getInsets().right;
		if (this.autoHeight)
			this.height = textBox.height + textBox.getInsets().top
					+ textBox.getInsets().bottom;
	}
	
	public String getText() {
		
		return textBox.getText();
	}
	
	public boolean hasText() {
		
		//lazy. fix later
		return textBox != null && textBox.getText() != null && !textBox.getText().isEmpty();
	}

	public void setImage(TextureInterface object) {

		if (this.texture != null)
			this.texture.release();
		this.texture = object;
		this.textBox = null;
	}

	public void setImage(BufferedImage image) {

		setImage(Engine.renderEngine.getTextureFromImage(image));
	}
	
	public boolean hasImage() {
		
		return texture != null;
	}
	
	public BufferedImage getImage() {
		
		return null;
	}

	public void onPress() {

		currentColor = selectedColor;
		currentColorIdentifier = SELECTED_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, PRESSED));
		lastPressed = true;
	}

	public void onRelease() {

		currentColor = focusColor;
		currentColorIdentifier = FOCUS_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, RELEASED));

		if (lastPressed)
			onClick();
		lastPressed = false;
	}

	public void onClick() {

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, CLICKED));
	}

	public void onEnter() {

		currentColor = focusColor;
		currentColorIdentifier = FOCUS_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, ENTERED));
	}

	public void onExit() {

		currentColor = unfocusColor;
		currentColorIdentifier = UNFOCUS_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, EXITED));
		lastPressed = false;
	}

	@Override
	protected void onPositionChange(int oldX, int oldY) {

		if (textBox != null) {
			textBox.x = this.x;// (x - oldX, y - oldY);
			textBox.y = this.y;
		}
	}

	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {

		if (textBox != null)
			textBox.setSize(width, height);
	}

	@Override
	public void revalidate() {

		if (textBox != null)
			textBox.revalidate();
		super.revalidate();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
}