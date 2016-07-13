package physics.JBox2D;

import matrix.Mat4;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public class Convert {

	static Vec2 convert(matrix.Vec2 vec2) {

		return new Vec2(vec2.x, vec2.y);
	}

	static Vec2 convert(matrix.Vec3 vec3) {

		return new Vec2(vec3.x, vec3.y);
	}

	static Vec2[] convert(matrix.Vec2[] vec2s) {

		Vec2[] vecs = new Vec2[vec2s.length];

		for (int i = 0; i < vecs.length; i++)
			vecs[i] = convert(vec2s[i]);

		return vecs;
	}

	static Vec2[] convert(matrix.Vec3[] vec3s) {

		Vec2[] vecs = new Vec2[vec3s.length];

		for (int i = 0; i < vecs.length; i++)
			vecs[i] = convert(vec3s[i]);

		return vecs;
	}

	static Transform convert(Mat4 transform) {

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
}
