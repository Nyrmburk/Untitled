package gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import graphics.UIRenderEngine;

public class TextBox extends GUIElement {
	
	private Insets insets;
	private static FormattedFont font;
	private boolean renderAsTexture = false;
	
	private String text = "";
	private String[] textLines;
	
	private Texture fontTexture;
	
	public TextBox() {
		
//		this(new FormattedFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12), true));
		this(font != null ? font : new FormattedFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12), true));
	}
	
	public TextBox(FormattedFont font) {
		
		super();
		this.font = font;
		this.insets = new Insets(0, 0, 0, 0);
		formatLines();
		this.height = font.getLineHeight();
	}
	
	@Override
	public BufferedImage render(UIRenderEngine renderEngine) {
		
		return renderEngine.renderTextBox(this);
	}
	
	public void draw() {
		
		draw(0, Color.black);
	}
	
	public void draw(float startLine, Color color) {
		
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
		
		int totalHeight = ((height - insets.top - insets.bottom) / lineHeight);
		if (totalHeight == 0) totalHeight = 1;
		if (totalHeight > textLines.length) totalHeight = textLines.length;
		totalHeight *= lineHeight;
		
		float verticalOffset = 0;
		if (font.verticalAlignment == FormattedFont.VerticalAlignment.TOP)
			verticalOffset += insets.top;
		else if (font.verticalAlignment == FormattedFont.VerticalAlignment.BOTTOM)
			verticalOffset += height - totalHeight - insets.bottom;
		else if (font.verticalAlignment == FormattedFont.VerticalAlignment.CENTER)
			verticalOffset += (height - totalHeight - insets.bottom) / 2;
		
		float horizontalOffset = 0;
		if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.LEFT)
			horizontalOffset += insets.left;
		else if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.RIGHT)
			horizontalOffset += width - insets.right;
		else
			horizontalOffset += insets.left
					- (-width + insets.right + insets.left) / 2;
		
		for (int i = (int) startLine; i < textLines.length; i++) {
			
			if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.LEFT)
				font.drawString(x + horizontalOffset, y + verticalOffset,
						textLines[i], color);
			else if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.RIGHT)
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
		
		if (text == null) return;
		if (this.renderAsTexture) renderToTexture();
	}
	
	private void renderToTexture() {
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new java.awt.Color(255, 255, 255, 0));
		g.fillRect(0, 0, width, height);
		
		if (font.antiAlias) {
			// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		}
		
		g.setColor(java.awt.Color.WHITE);
		g.setFont(font.getFont());
		
		int lineHeight = font.getLineHeight();
		int ascent = g.getFontMetrics().getAscent();
		
		int totalHeight = ((height - insets.top - insets.bottom) / lineHeight);
		if (totalHeight == 0) totalHeight = 1;
		if (totalHeight > textLines.length) totalHeight = textLines.length;
		totalHeight *= lineHeight;
		
		float verticalOffset = ascent;
		if (font.verticalAlignment == FormattedFont.VerticalAlignment.TOP)
			verticalOffset += insets.top;
		else if (font.verticalAlignment == FormattedFont.VerticalAlignment.BOTTOM)
			verticalOffset += height - totalHeight - insets.bottom;
		else if (font.verticalAlignment == FormattedFont.VerticalAlignment.CENTER)
			verticalOffset += (height - totalHeight - insets.bottom) / 2;
		
		float horizontalOffset = 0;
		if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.LEFT)
			horizontalOffset += insets.left;
		else if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.RIGHT)
			horizontalOffset += width - insets.right;
		else
			horizontalOffset += insets.left
					- (-width + insets.right + insets.left) / 2;
		
		for (int i = 0; i < textLines.length; i++) {
			
			if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.LEFT)
				g.drawString(textLines[i], horizontalOffset, verticalOffset);
			else if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.RIGHT)
				g.drawString(textLines[i], horizontalOffset
						- font.getWidth(textLines[i].trim()), verticalOffset);
			else
				g.drawString(textLines[i], horizontalOffset
						- font.getWidth(textLines[i].trim()) / 2,
						verticalOffset);
			
			verticalOffset += lineHeight;
			if (verticalOffset > height - lineHeight + ascent - insets.bottom)
				break;
		}
		
		try {
			if (fontTexture != null) fontTexture.release();
			fontTexture = BufferedImageUtil.getTexture(font.toString(), image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
		image = null;
	}
	
	public String getText() {
		
		return text;
	}
	
	public void setText(String text) {
		
		if (this.text == null || !this.text.equals(text)) {
			
			if (this.autoWidth) {
				
				this.width = font.getWidth(text) + insets.left + insets.right;
			}
			
			this.text = text;
//			formatLines();
			
//			if (renderAsTexture) renderToTexture();
			invalidate();
		}
	}
	
	public FormattedFont getFont() {
		
		return font;
	}
	
	public Insets getInsets() {
		
		return insets;
	}
	
	public void setInsets(Insets insets) {
		
		this.insets = insets;
	}
	
	public void setInsets(int top, int left, int bottom, int right) {
		
		this.insets = new Insets(top, left, bottom, right);
	}
	
	public void formatLines() {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		int start = 0;
		int end = 0;
		
		int currrentLineWidth = 0;
		int spaceWidth = font.getWidth(" ");
		int wordWidth = 0;
		
		String[] words = text.split("[\\r\\n|\\r]|[\\s]");
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
			
			if (wordWidth > width - insets.left - insets.right) break;
			
			currrentLineWidth += wordWidth + spaceWidth;
			
			if (currrentLineWidth > width - insets.left - insets.right
					+ spaceWidth) {
				
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
	
	@Override
	public void revalidate() {
		
//		System.out.println("TextBox revalidated");
		formatLines();
		super.revalidate();
	}

	@Override
	protected void onPositionChange(int oldX, int oldY) {
	}

	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {
		
		if (renderAsTexture) renderToTexture();
	}
}
