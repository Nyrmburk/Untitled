package graphics.modelconverter;

import graphics.ModelLoader;
import main.Line;
import main.Vec2;

/**
 * Created by Nyrmburk on 5/3/2016.
 *
 * Requirements for line rendering:
 * 1. Varying line thickness
 * 2. Varying color
 * 3. Miter and bevel edges
 * 4. Looping (add edges to line if end == start)
 * 5. antialiased?
 *
 * Implementation.
 *
 * The implementation I am taking is getting two line segments, normalizing them, and multiplying the length (1) by the
 * thickness. Next, add the vectors together to get the first vertex. It will be relative so we also need to add the
 * vertex position. To get the other side of the line, simply reverse the previous vector and again add the vertex.
 * I might do a test on the two lines to determine if they are an obtuse or acute angle. Then take the obtuse side and
 * bevel instead of miter if the length is too great relative to the thickness.
 */
public class LineConverter implements ModelConverter<Line> {

	public ModelLoader convert(Line line) {

		ModelLoader model = new ModelLoader();

		for (int i = 0; i < line.getLength(); i++) {


		}

		return model;
	}
}