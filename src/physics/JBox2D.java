package physics;

import matrix.Mat4;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import java.util.List;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public class JBox2D {

	public static Shape[] shapeFromPolygon(matrix.Vec2[] polygon) {

		List<matrix.Vec2[]> polys = Polygon.approximateDecomposition(polygon);

		Shape[] shapes = new Shape[polys.size()];
		for (int i = 0; i < polys.size(); i++) {

			PolygonShape shape = new PolygonShape();
			shape.set(convert(polys.get(i)), polys.get(i).length);
			shapes[i] = shape;
		}
		return shapes;
	}

	public static Vec2 convert(matrix.Vec2 vec2) {

		return new Vec2(vec2.x, vec2.y);
	}

	public static Vec2 convert(matrix.Vec3 vec3) {

		return new Vec2(vec3.x, vec3.y);
	}

	public static Vec2[] convert(matrix.Vec2[] vec2s) {

		Vec2[] vecs = new Vec2[vec2s.length];

		for (int i = 0; i < vecs.length; i++)
			vecs[i] = convert(vec2s[i]);

		return vecs;
	}

	public static Vec2[] convert(matrix.Vec3[] vec3s) {

		Vec2[] vecs = new Vec2[vec3s.length];

		for (int i = 0; i < vecs.length; i++)
			vecs[i] = convert(vec3s[i]);

		return vecs;
	}

	public static Transform convert(Mat4 transform) {

		Vec2 position = convert(matrix.Transform.getPosition(transform));
		Rot rotation = new Rot(getRotation(transform));
		return new Transform(position, rotation);
	}

	//http://stackoverflow.com/questions/10629737/convert-3d-4x4-rotation-matrix-into-2d
	private static float getRotation(Mat4 transform) {

		float[] m = transform.m;

		float scaleX = (float) Math.sqrt(m[0] * m[0] + m[4] * m[4]);
		float scaleY = (float) Math.sqrt(m[1] * m[1] + m[5] * m[5]);

		// I am not entirely sure, but I think scaleX and scaleY are mixed up
		return (float) Math.atan2(scaleX * m[5], scaleY * m[1]);
	}

	////////////////////  other way around  ////////////////////

	public static matrix.Vec2 convert(Vec2 vec2) {

		return new matrix.Vec2(vec2.x, vec2.y);
	}

	public static matrix.Vec2[] convert(Vec2[] vec2s) {

		matrix.Vec2[] vecs = new matrix.Vec2[vec2s.length];

		for (int i = 0; i < vecs.length; i++)
			vecs[i] = convert(vec2s[i]);

		return vecs;
	}
}
