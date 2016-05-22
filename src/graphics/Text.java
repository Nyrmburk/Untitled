package graphics;

import java.awt.*;
import java.util.List;

/**
 * Created by Nyrmburk on 5/22/2016.
 */
public class Text {

	public GraphicsFont font;
	public List<TextInstance> instances;

	public static class TextInstance {

		public Point point;
		public String string;
		public Color color;

		public TextInstance (Point point, String string) {

			this(point, string, Color.BLACK);
		}

		public TextInstance (Point point, String string, Color color) {

			this.point = point;
			this.string = string;
			this.color = color;
		}
	}
}
