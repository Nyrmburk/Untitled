package gui.css;

import org.fit.cssbox.layout.TextBox;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSTextBox extends CSSBox {

	private String text;

	//TODO make a font object that has a font atlas
//	private Font font;

	{
		transparent = true;
	}

	public CSSTextBox(TextBox textBox) {

		this.text = textBox.getText();
	}

	public String getText() {

		return text;
	}

//	public Font getFont() {
//
//		return font;
//	}
}
