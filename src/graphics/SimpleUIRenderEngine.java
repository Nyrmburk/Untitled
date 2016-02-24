package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gui.Button;
import gui.FormattedFont;
import gui.Panel;
import gui.ProgressBar;
import gui.TextBox;

public class SimpleUIRenderEngine extends UIRenderEngine {

	@Override
	public BufferedImage renderPanel(Panel panel) {

		if (panel.getWidth() == 0 && panel.getHeight() == 0)
			return null;

		BufferedImage image = new BufferedImage(
				panel.getWidth(), panel.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.setColor(panel.getBackgroundColor());
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		
		if (panel.getImage() != null) {
			
			int x = (panel.getWidth() - panel.getImage().getWidth()) / 2;
			int y = (panel.getHeight() - panel.getImage().getHeight()) / 2;
			g.drawImage(panel.getImage(), x, y, null);
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, panel.getWidth()-1, panel.getHeight()-1);
		
		g.dispose();
		return image;
	}

	@Override
	public BufferedImage renderTextBox(TextBox textBox) {

		if (textBox.getWidth() == 0 && textBox.getHeight() == 0)
			return null;

		int width = textBox.getWidth();
		int height = textBox.getHeight();
		Insets insets = textBox.getInsets();
		FormattedFont font = textBox.getFont();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) image.getGraphics();

		if (font.antiAlias) {
			// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		}

		g.setColor(java.awt.Color.WHITE);
		g.setFont(font.getFont());

		int lineHeight = font.getLineHeight();
		int ascent = g.getFontMetrics().getAscent();

		ArrayList<String> textLines = formatLines(textBox);

		int totalHeight = ((height - insets.top - insets.bottom) / lineHeight);
		if (totalHeight == 0)
			totalHeight = 1;
		if (totalHeight > textLines.size())
			totalHeight = textLines.size();
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
			horizontalOffset += insets.left - (-width + insets.right + insets.left) / 2;

		for (int i = 0; i < textLines.size(); i++) {

			if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.LEFT)
				g.drawString(textLines.get(i), horizontalOffset, verticalOffset);
			else if (font.horizontalAlignment == FormattedFont.HorizontalAlignment.RIGHT)
				g.drawString(textLines.get(i), horizontalOffset - font.getWidth(textLines.get(i).trim()),
						verticalOffset);
			else
				g.drawString(textLines.get(i), horizontalOffset - font.getWidth(textLines.get(i).trim()) / 2,
						verticalOffset);

			verticalOffset += lineHeight;
			if (verticalOffset > height - lineHeight + ascent - insets.bottom)
				break;
		}
		
		g.dispose();

		return image;
	}

	private static ArrayList<String> formatLines(TextBox textBox) {

		String text = textBox.getText();

		ArrayList<String> lines = new ArrayList<String>();

		int start = 0;
		int end = 0;

		int currrentLineWidth = 0;
		int spaceWidth = textBox.getFont().getWidth(" ");
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

			wordWidth = textBox.getFont().getWidth(word);

			if (wordWidth > textBox.getWidth() - textBox.getInsets().left - textBox.getInsets().right)
				break;

			currrentLineWidth += wordWidth + spaceWidth;

			if (currrentLineWidth > textBox.getWidth() - textBox.getInsets().left - textBox.getInsets().right
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

		return lines;
	}

	@Override
	public BufferedImage renderProgressBar(ProgressBar progressBar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage renderButton(Button button) {

		if (button.getWidth() == 0 && button.getHeight() == 0)
			return null;

		BufferedImage image = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();
		g.setColor(button.getColor());
		g.fillRect(0, 0, button.getWidth(), button.getHeight());

		// add quad to UIRenderContext
		// actually don't, we just want the image
		// do that step at a later time

		// if button has an image or text
		if (button.hasText()) {

			// render text
			TextBox textBox = new TextBox();
			textBox.setText(button.getText());
			// look into the quads to see if adjustment needed
			g.drawImage(renderTextBox(textBox), 0, 0, null);
		} else if (button.hasImage()) {

			// render image

			int x = (button.getWidth() - button.getImage().getWidth()) / 2;
			int y = (button.getHeight() - button.getImage().getHeight()) / 2;

			g.drawImage(button.getImage(), x, y, null);
		}

		g.dispose();

		return image;
	}
}
