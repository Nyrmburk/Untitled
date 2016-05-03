package graphics.modelconverter;

import graphics.ModelLoader;
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
 */
public class Line implements ModelConverter<Vec2[]> {

	public ModelLoader convert(Vec2[] vertices) {

		return null;
	}
}
