package gui;

import java.awt.Color;

import graphics.GraphicsFont;
import graphics.Texture;

public class Button extends GUIElement {

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

	private TextBox textBox;
	private Texture texture;

	{
		addActionListener(new PointerListener() {

			@Override
			public void actionPerformed() {
				switch (getCurrentState()) {

					case ENTER:
						setBackgroundColor(focusColor);
						currentColorIdentifier = FOCUS_COLOR;
						break;
					case EXIT:
						setBackgroundColor(unfocusColor);
						currentColorIdentifier = UNFOCUS_COLOR;
						break;
					case PRESS:
						setBackgroundColor(focusColor);
						currentColorIdentifier = FOCUS_COLOR;
						break;
					case RELEASE:
						setBackgroundColor(focusColor);
						currentColorIdentifier = FOCUS_COLOR;
						break;
				}
			}
		});
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

	public void setFont(GraphicsFont font) {
		textBox.setFont(font);
	}

	public GraphicsFont getFont() {

		return textBox.getFont();
	}

	public void setImage(Texture texture) {

		if (this.texture != null)
			this.texture.release();
		this.texture = texture;
		this.textBox = null;
		invalidate();
	}
	
	public boolean hasImage() {
		
		return texture != null;
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