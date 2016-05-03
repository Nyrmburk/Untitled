package gui;

import graphics.GraphicsFont;

import java.awt.*;
import java.util.LinkedList;

public class TextBox extends GUIElement {

	private GraphicsFont font;

	private String text;
	private String[] lines;

	private boolean wordwrap;

	public String[] formatLines(String text, FontMetrics metrics, int width) {

		LinkedList<String> lines = new LinkedList<>();

		int lineStart = 0;
		int lineEnd;

		int lineWidth = 0;

		int lastWhitespace = 0;

		for (int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);
			lineWidth += metrics.charWidth(c);

			if (lineWidth > width) {

				lineEnd = lastWhitespace;
				lines.add(text.substring(lineStart, lineEnd));
				lineStart = lineEnd;
			}

			if (Character.isWhitespace(c))
				lastWhitespace = i;
		}

		String[] textLines = new String[lines.size()];
		lines.toArray(textLines);

		return textLines;
	}

	@Override
	protected void layout() {
		if (doesWordwrap()) {
			lines = formatLines(getText(), getFont().getFontMetrics(), getWidth());
		} else {

			setSize(getFont().getFontMetrics().stringWidth(text), getFont().getFontMetrics().getHeight());
		}
	}

	public boolean doesWordwrap() {
		return wordwrap;
	}

	public void setWordwrap(boolean wordwrap) {
		this.wordwrap = wordwrap;
		invalidate();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		invalidate();
	}

	public GraphicsFont getFont() {

		GraphicsFont font = this.font;
		if (font == null)
			font = GUIAppearance.getCurrentAppearance().getFont();

		return font;
	}

	public void setFont(GraphicsFont font) {
		this.font = font;
		invalidate();
	}

	@Override
	public Color getForegroundColor() {

		Color color = super.getForegroundColor();
		if (color == null)
			color = GUIAppearance.getCurrentAppearance().fontColor;

		return color;
	}

	@Override
	protected ContextBox createBox() {

		ContextBox box = new ContextBox();
		box.setBounds(this.getBounds());
		box.color = getBackgroundColor();

		if (doesWordwrap()) {

			box.texts = new ContextBox.Text[lines.length];

			int y = getY();
			for (int i = 0; i < lines.length; i++) {

				box.texts[i].setLocation(getX(), y);
				box.texts[i] = new ContextBox.Text();
				box.texts[i].text = lines[i];
				box.texts[i].color = getForegroundColor();
				box.texts[i].font = font;
				y += font.getFontMetrics().getHeight();
			}
		} else {

			box.texts = new ContextBox.Text[1];

			box.texts[0] = new ContextBox.Text();
			box.texts[0].setLocation(getX(), getY());
			box.texts[0].text = text;
			box.texts[0].color = getForegroundColor();
			box.texts[0].font = getFont();
		}

		return box;
	}
}
