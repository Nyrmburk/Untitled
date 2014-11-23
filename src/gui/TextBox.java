package gui;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextBox extends GUIElement {
	
	private static final long serialVersionUID = 3367916953001287485L;
	
	private Insets insets;
	private FormattedFont font;
	private boolean renderAsTexture = false;
	
	private String text;
	private String[] textLines;
	
	private Texture fontTexture;
	
	TextBox(FormattedFont font) {
		
		super(0, 0, 0, 0);
		this.font = font;
		this.insets = new Insets(0, 0, 0, 0);
	}
	
	TextBox(FormattedFont font, int x, int y, int width, int height) {
		
		super(x, y, width, height);
		this.font = font;
		this.insets = new Insets(0, 0, 0, 0);
	}
	
	TextBox(FormattedFont font, int x, int y, int width, int height, int top,
			int left, int bottom, int right) {
		
		super(x, y, width, height);
		this.font = font;
		this.insets = new Insets(top, left, bottom, right);
	}
	
	TextBox(FormattedFont font, Rectangle rect) {
		
		super(rect);
		this.font = font;
		this.insets = new Insets(0, 0, 0, 0);
	}
	
	TextBox(FormattedFont font, Rectangle rect, int top,
			int left, int bottom, int right) {
		
		super(rect);
		this.font = font;
		this.insets = new Insets(top, left, bottom, right);
	}
	
	TextBox(FormattedFont font, Rectangle rect, Insets insets) {
		
		super(rect);
		this.font = font;
		this.insets = insets;
	}
	
	public void render(float startLine, Color color) {
		
		if (renderAsTexture) {
			color.bind();
			fontTexture.bind();
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(0, fontTexture.getHeight());
			GL11.glVertex2f(x, y + height);
			GL11.glTexCoord2f(fontTexture.getWidth(), fontTexture.getHeight());
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(fontTexture.getWidth(), 0);
			GL11.glVertex2f(x + width, y);
			GL11.glEnd();
			return;
		}
		
		int lineHeight = font.getLineHeight();
		
		float verticalOffset = insets.top;
		float horizontalOffset = 0;
		if (font.currentAlignment == FormattedFont.alignment.LEFT)
			horizontalOffset += insets.left;
		else if (font.currentAlignment == FormattedFont.alignment.RIGHT)
			horizontalOffset += width - insets.right;
		else
			horizontalOffset += insets.left
					- (-width + insets.right + insets.left) / 2;
		
		for (int i = (int) startLine; i < textLines.length; i++) {
			
			if (font.currentAlignment == FormattedFont.alignment.LEFT)
				font.drawString(x + horizontalOffset, y + verticalOffset,
						textLines[i], color);
			else if (font.currentAlignment == FormattedFont.alignment.RIGHT)
				font.drawString(x + horizontalOffset
						- font.getWidth(textLines[i].trim()), y
						+ verticalOffset, textLines[i], color);
			else
				font.drawString(x + horizontalOffset
						- font.getWidth(textLines[i].trim()) / 2, y
						+ verticalOffset, textLines[i], color);
			
			verticalOffset += lineHeight;
			if (verticalOffset > height - lineHeight - insets.bottom) break;
		}
	}
	
	public void setRenderAsTexture(boolean renderAsTexture) {
		
		this.renderAsTexture = renderAsTexture;
		if (this.renderAsTexture) renderToTexture();
	}
	
	private void renderToTexture() {
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.createGraphics();
		g.setColor(new java.awt.Color(255,255,255,0));
		g.fillRect(0,0, width, height);
		
		if (font.antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setColor(java.awt.Color.BLACK);
		g.setFont(font.getFont());
		
		int lineHeight = font.getLineHeight();
		int ascent = g.getFontMetrics().getAscent();
		
		float verticalOffset = insets.top + ascent;
		float horizontalOffset = 0;
		if (font.currentAlignment == FormattedFont.alignment.LEFT)
			horizontalOffset += insets.left;
		else if (font.currentAlignment == FormattedFont.alignment.RIGHT)
			horizontalOffset += width - insets.right;
		else
			horizontalOffset += insets.left
					- (-width + insets.right + insets.left) / 2;
		
		for (int i = 0; i < textLines.length; i++) {
			
			if (font.currentAlignment == FormattedFont.alignment.LEFT)
				g.drawString(textLines[i], horizontalOffset, verticalOffset);
			else if (font.currentAlignment == FormattedFont.alignment.RIGHT)
				g.drawString(textLines[i], horizontalOffset
						- font.getWidth(textLines[i].trim()), verticalOffset);
			else
				g.drawString(textLines[i], horizontalOffset
						- font.getWidth(textLines[i].trim()) / 2, verticalOffset);
			
			verticalOffset += lineHeight;
			if (verticalOffset > height - lineHeight + ascent - insets.bottom) break;
		}
		
		try {
			fontTexture = BufferedImageUtil.getTexture(font.toString(), image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setText(String text) {
		
		this.text = text;
		formatLines();
	}
	
	public FormattedFont getFont() {
		
		return font;
	}
	
	public Insets getInsets() {
		
		return insets;
	}
	
	public void formatLines() {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		int start = 0;
		int end = 0;
		
		int currrentLineWidth = 0;
		int spaceWidth = font.getWidth(" ");
		int wordWidth = 0;
		
		String[] words = text.split("[\\r?\\n|\\r]|[\\s]");
		String word;
		
		boolean lastEmpty = false;
		
		for (int i = 0; i < words.length; i++) {
			
			word = words[i];
			
			if (word.isEmpty()) {
				if (lastEmpty) {
					end++;
					start++;
					continue;
				}
				lines.add(text.substring(start, end));
				start = ++end;
				currrentLineWidth = 0;
				lastEmpty = true;
				continue;
			}
			
			lastEmpty = false;
			
			wordWidth = font.getWidth(word);
			currrentLineWidth += wordWidth + spaceWidth;
			;
			
			if (currrentLineWidth > width - insets.left - insets.right) {
				
				lines.add(text.substring(start, end));
				start = end;
				currrentLineWidth = 0;
				i--;
			} else {
				
				end += word.length() + 1;
				if (end >= text.length()) {
					
					lines.add(text.substring(start, text.length()));
					break;
				}
			}
		}
		
		textLines = new String[lines.size()];
		lines.toArray(textLines);
	}
}
