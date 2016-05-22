package graphics.modelconverter;

import graphics.ModelLoader;
import graphics.Text;
import graphics.Texture;

import java.awt.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 5/14/2016.
 */
public class TextConverter implements ModelConverter<Text[]> {

	// index counter to keep track of vertex indices;
	int totalIndex;

	@Override
	public List<ModelLoader> convert(Text[] texts) {

		List<ModelLoader> modelList = new LinkedList<>();

		for (Text text : texts) {

			totalIndex = 0;

			ModelLoader model = new ModelLoader();

			model.texture = text.font.getAtlas();

			// List of bounds of chars in graphicsfont
			Rectangle[] allBounds = text.font.getBounds();

			for (Text.TextInstance instance : text.instances) {
				// How much to move the next character
				int charAdvance = instance.point.x;

				// Iterate through all the characters in the string
				for (int i = 0; i < instance.string.length(); i++) {

					char c = instance.string.charAt(i);

					if (c > 255) c = ' '; //char is not ascii

					//bounds of the current char
					Rectangle bounds = allBounds[c];

					if (bounds == null)
						// char is not displayable or is whitespace
						continue;

					// copy the bounds (for width and height)
					Rectangle r = bounds.getBounds();

					// copy the bounds and scale to texture coordinates
					Texture atlas = text.font.getAtlas();
					float top = atlas.yRatio(atlas.getHeight() - bounds.y);
					float bottom = atlas.yRatio(atlas.getHeight() - (bounds.y + bounds.height));
					float left = atlas.xRatio(bounds.x);
					float right = atlas.xRatio(bounds.x + bounds.width);

					// change the position from texture pixel position to text position
					r.x = charAdvance;
					charAdvance += text.font.getFontMetrics().charWidth(c);
					r.y = instance.point.y;

					// put vertices
					//    0--------3
					//    |  \     |
					//    |     \  |
					//    1--------2
					model.vertices.put((float) r.x, r.y, 0);
					model.vertices.put((float) r.x, r.y + r.height, 0);
					model.vertices.put((float) r.x + r.width, r.y + r.height, 0);
					model.vertices.put((float) r.x + r.width, r.y, 0);

					// put texture coordinates
					// 0,1-----1,1
					//  |       |
					//  |       |
					// 0,0-----1,0
					model.textureCoords.put(left, top);
					model.textureCoords.put(left, bottom);
					model.textureCoords.put(right, bottom);
					model.textureCoords.put(right, top);

//					int color = instance.color.getRGB();
//					color = color >>> 8 | color << 24;
//					model.color.add(color);
					model.color.add(0xFF);

					// put indices;
					model.addFace(totalIndex++, totalIndex++, totalIndex++, totalIndex++);
				}
			}
			modelList.add(model);
		}

		return modelList;
	}
}
