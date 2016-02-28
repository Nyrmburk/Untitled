package gui.css;

import graphics.GraphicsFont;
import org.fit.cssbox.layout.TextBox;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSTextBox extends CSSBox {

	private String text;

	//TODO make a font object that has a font atlas
	private GraphicsFont font;

	{
		transparent = true;
	}

	public CSSTextBox(TextBox textBox, GraphicsFont font) {

		super(textBox);
		this.text = textBox.getText();
		this.font = font;
//		textBox.
	}

	public String getText() {

		return text;
	}

	public GraphicsFont getFont() {

		return font;
	}

//	public Font getFont() {
//
//		return font;
//	}
}
