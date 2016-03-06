package gui;

import graphics.GraphicsFont;

import java.awt.*;

/**
 * Created by Nyrmburk on 3/6/2016.
 */
public class GUIAppearance {

	//TODO extend appearance

	private static GUIAppearance currentAppearance = new DefaultAppearance();

	protected GraphicsFont font;
	protected Color fontColor;

	//lots of ninepatches?

	public static GUIAppearance getCurrentAppearance() {

		return currentAppearance;
	}

	public GraphicsFont getFont() {

		return font;
	}

	public Color getFontColor() {

		return fontColor;
	}

	public static class DefaultAppearance extends GUIAppearance {

		{
			font = new GraphicsFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
			fontColor = Color.BLACK;
		}
	}
}
