package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import graphics.TextureInterface;

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

	private Color focusColor;
	private Color unfocusColor;
	private Color selectedColor;

	private int currentColorIdentifier = UNFOCUS_COLOR;

	TextBox textBox;
	TextureInterface texture;

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
			setBackgroundColor(color);
	}

	public void setText(String text) {

		if (textBox == null)
			textBox = new TextBox();
		textBox.setText(text);
		if (this.texture != null)
			this.texture.release();
		invalidate();
	}
	
	public String getText() {
		
		return textBox.getText();
	}
	
	public boolean hasText() {

		return textBox != null && textBox.getText() != null && !textBox.getText().isEmpty();
	}

	public void setImage(TextureInterface texture) {

		if (this.texture != null)
			this.texture.release();
		this.texture = texture;
		this.textBox = null;
		invalidate();
	}
	
	public boolean hasImage() {
		
		return texture != null;
	}

	public void onPress() {

		setBackgroundColor(selectedColor);
		currentColorIdentifier = SELECTED_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, PRESSED));
		lastPressed = true;
	}

	public void onRelease() {

		setBackgroundColor(focusColor);
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

		setBackgroundColor(focusColor);
		currentColorIdentifier = FOCUS_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, ENTERED));
	}

	public void onExit() {

		setBackgroundColor(unfocusColor);
		currentColorIdentifier = UNFOCUS_COLOR;

		for (ActionListener listener : listeners)
			if (listener != null)
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, EXITED));
		lastPressed = false;
	}

	@Override
	protected void layout() {
		textBox.setPosition(getPosition());
		textBox.layout();
		setSize(textBox.getSize());
		textBox.revalidate();
	}

	@Override
	protected ContextBox createBox() {

		ContextBox box = new ContextBox();
		box.setBounds(this.getBounds());
		box.color = getBackgroundColor();

		if (hasImage()) {

			box.texture = texture;
		} else if (hasText()) {

			box.subBoxes = new ContextBox[]{textBox.getBox()};
		}

		return box;
	}
}