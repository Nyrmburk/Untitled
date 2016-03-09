package gui;

import graphics.GraphicsFont;
import graphics.TextureInterface;

import java.awt.*;

/**
 * Created by Nyrmburk on 3/6/2016.
 */
public class ContextBox extends Rectangle {

		public Color color;
		public TextureInterface texture;
//		public NinePatch ninepatch
		public Text[] texts;
		public ContextBox[] subBoxes;

	public static class Text extends Point {

		public Color color;
		public GraphicsFont font;
		public String text;
	}
}