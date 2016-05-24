package graphics.modelconverter;

import graphics.ModelLoader;
import main.Line;
import matrix.Vec2;

import java.util.LinkedList;
import java.util.List;

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

	public List<ModelLoader> convert(Line line) {

		ModelLoader model = new ModelLoader();

		Vec2[] verts = line.getVertices();

		Vec2 inLine = null;
		Vec2 outLine = null;

		boolean endsEqual = verts[0].equals(verts[line.getLength() - 1]);

		if (line.isLoop()) {

			inLine = verts[0].subtract(verts[line.getLength() - 1]);
		} else if (endsEqual){

			inLine = verts[0].subtract(verts[line.getLength() - 2]);
		} else {

			inLine = verts[1].subtract(verts[0]);
		}

		for (int i = 0; i < line.getLength() - 1; i++) {

			float width = line.getWidth()[i] / 2;

			outLine = verts[i + 1].subtract(verts[i]);

			Vec2 miter = getCorner(inLine, outLine).multiply(width);

			Vec2 leftVert = verts[i].add(miter);
			Vec2 rightVert = verts[i].subtract(miter);

			model.vertices.put(rightVert.x, rightVert.y, 0);
			model.vertices.put(leftVert.x, leftVert.y, 0);
			int color = line.getColor()[i].getRGB();
			color = color << 8 | color >>> 24;
			model.color.add(color);
			model.color.add(color);

			inLine = outLine;
		}

		if (line.isLoop()) {

			outLine = verts[0].subtract(verts[line.getLength() - 1]);
		} else if (!endsEqual){

			outLine = verts[line.getLength() - 1].subtract(verts[line.getLength() - 2]);
		}

		if (line.isLoop() || !endsEqual) {

			Vec2 miter = getCorner(inLine, outLine).multiply(line.getWidth()[0] / 2);
			Vec2 leftVert = verts[line.getLength() - 1].add(miter);
			Vec2 rightVert = verts[line.getLength() - 1].subtract(miter);

			model.vertices.put(rightVert.x, rightVert.y, 0);
			model.vertices.put(leftVert.x, leftVert.y, 0);
			int color = line.getColor()[0].getRGB();
			color = color << 8 | color >>> 24;
			model.color.add(color);
			model.color.add(color);
		}

		int length = line.getLength() - 1;
		if (endsEqual)
			length--;
		length *= 2;
		for (int i = 0; i < length; i += 2) {

			model.addFace(i, i + 1, i + 2);
			model.addFace(i + 1, i + 3, i + 2);
		}

		if (line.isLoop() || endsEqual) {

			model.addFace(length, length + 1, 0);
			model.addFace(length + 1, 1, 0);
		}

		List<ModelLoader> modelList = new LinkedList<>();
		modelList.add(model);
		return modelList;
	}

	private Vec2 getCorner(Vec2 inLine, Vec2 outLine) {

		inLine = inLine.normalized();
		outLine = outLine.normalized();

		Vec2 normal = outLine.transpose();
		Vec2 tangent = inLine.add(outLine).normalized();

		Vec2 miter = new Vec2(-tangent.y, tangent.x);
		float length = 1 / miter.dot(normal);
		miter = miter.normalized().multiply(length);

		return miter;
	}
}