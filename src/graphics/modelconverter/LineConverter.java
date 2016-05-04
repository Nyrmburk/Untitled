package graphics.modelconverter;

import graphics.ModelLoader;
import main.Line;
import main.Vec2;

/**
 * Created by Nyrmburk on 5/3/2016.
 * <p>
 * Requirements for line rendering:
 * 1. Varying line thickness
 * 2. Varying color
 * 3. Miter and bevel edges
 * 4. Looping (add edges to line if end == start)
 * 5. antialiased?
 * <p>
 * Implementation.
 * <p>
 * The implementation I am taking is getting two line segments, calculating the normal,normalizing them, and multiplying the length (1) by the
 * thickness. Next, add the vectors together to get the first vertex. It will be relative so we also need to add the
 * vertex position. To get the other side of the line, simply reverse the previous vector and again add the vertex.
 * I might do a test on the two lines to determine if they are an obtuse or acute angle. Then take the obtuse side and
 * bevel instead of miter if the length is too great relative to the thickness.
 * <p>
 * https://forum.libcinder.org/topic/smooth-thick-lines-using-geometry-shader#23286000001269127
 */
public class LineConverter implements ModelConverter<Line> {

	public ModelLoader convert(Line line) {

		ModelLoader model = new ModelLoader();

		Vec2[] verts = line.getVertices();

		Vec2 inLine = verts[1].subtract(verts[0]).normalized();

		for (int i = 1; i < line.getLength(); i++) {

			float width = line.getWidth()[i] / 2;

			Vec2 outLine = verts[i + 1].subtract(verts[i]).normalized();
			Vec2 normal = outLine.transpose();
			Vec2 tangent = inLine.add(outLine).normalized();

			Vec2 miter = new Vec2(-tangent.getY(), tangent.getX());
			float length = width / miter.dot(normal);
			miter = miter.normalized().multiply(length);

			Vec2 leftVert = verts[i].add(miter);
			Vec2 rightVert = verts[i].subtract(miter);

			inLine = outLine;
		}

		return model;
	}
}