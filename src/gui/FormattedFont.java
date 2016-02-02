package gui;
import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class FormattedFont extends TrueTypeFont {
	
	public enum HorizontalAlignment {
		LEFT, CENTER, RIGHT
	}
	
	public enum VerticalAlignment {
		TOP, CENTER, BOTTOM
	}
	
	private Font font;
	public boolean antiAlias;
	
	public HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	public VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
	// boolean justified = false;
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
	
	@Override
	public FormattedFont clone() {
		
		FormattedFont newFont = new FormattedFont(font, antiAlias);
		newFont.horizontalAlignment = horizontalAlignment;
		newFont.verticalAlignment = verticalAlignment;
		newFont.indent = indent;
		newFont.underline = underline;
		newFont.strikethrough = strikethrough;
		
		return newFont;
	}
}
