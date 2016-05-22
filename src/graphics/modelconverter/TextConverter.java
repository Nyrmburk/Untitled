package graphics.modelconverter;

import graphics.GraphicsFont;
import graphics.ModelLoader;

import java.awt.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 5/14/2016.
 */
public class TextConverter implements ModelConverter<TextConverter.Text> {

	// index counter to keep track of vertex indices;
	int totalIndex;

	@Override
	public List<ModelLoader> convert(Text text) {

		totalIndex = 0;

		ModelLoader model = new ModelLoader();

		model.texture = text.graphicsFont.getAtlas();

		int index = 0;

		// List of bounds of chars in graphicsfont
		Rectangle[] allBounds = text.graphicsFont.getBounds();

		// How much to move the next character
		int charAdvance = text.position.x;

		// Iterate through all the characters in the string
		for (int i = 0; i < text.string.length(); i++) {

			char c = text.string.charAt(i);

			if (c > 255) c = ' '; //char is not ascii

			//bounds of the current char
			Rectangle bounds = allBounds[c];

			// copy the bounds (for width and height)
			Rectangle r = bounds.getBounds();

			// copy the bounds and scale to texture coordinates
			float width = text.graphicsFont.getAtlas().getWidth();
			float height = text.graphicsFont.getAtlas().getHeight();
			float top = ((float) bounds.x) / width;
			float bottom = ((float) bounds.x + bounds.width) / width;
			float left = ((float) bounds.y) / height;
			float right = ((float) bounds.y + bounds.height) / height;

			// change the position from texture pixel position to text position
			r.x = charAdvance;
			r.y = text.position.y;

			// put vertices
			//    0--------3
			//    |        |
			//    1--------2
			model.vertices.put((float) r.x, r.y, 0);
			model.vertices.put((float) r.x, r.y + r.height, 0);
			model.vertices.put((float) r.x + r.width, r.y + r.height, 0);
			model.vertices.put((float) r.x + r.width, r.y, 0);

			// put texture coordinates
			model.textureCoords.put(top, left);
			model.textureCoords.put(bottom, left);
			model.textureCoords.put(bottom, right);
			model.textureCoords.put(top, right);

			// put indices;
			model.addFace(totalIndex++, totalIndex++, totalIndex++, totalIndex++);
		}

		List<ModelLoader> modelList = new LinkedList<>();
		modelList.add(model);
		return modelList;
	}

	public static class Text {

		public String string;
		public GraphicsFont graphicsFont;
		public Point position;

		public Text(String text, GraphicsFont font, Point position) {

			this.string = text;
			this.graphicsFont = font;
			this.position = position;
		}
	}
}
