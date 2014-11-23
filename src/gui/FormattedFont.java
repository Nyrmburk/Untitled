package gui;
import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class FormattedFont extends TrueTypeFont {

	enum alignment {
		LEFT,
		CENTER,
		RIGHT
	}
	
	private Font font;
	boolean antiAlias;
	
	alignment currentAlignment = alignment.LEFT;
	boolean justified = false;
	byte indent = 3;
	boolean underline = false;
	boolean strikethrough = false;
	
	public FormattedFont(Font font, boolean antiAlias) {
		super(font, antiAlias);
		this.font = font;
		this.antiAlias = antiAlias;
	}
	
	public FormattedFont(Font font, boolean antiAlias, char[] additionalChars) {
		super(font, antiAlias, additionalChars);
		// TODO Auto-generated constructor stub
	}
	
	public Font getFont() {
		
		return font;
	}
}
