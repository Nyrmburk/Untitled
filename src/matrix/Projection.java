package matrix;

import java.awt.*;

/**
 * Created by Nyrmburk on 5/12/2016.
 * http://www.songho.ca/opengl/gl_projectionmatrix.html
 * https://www.opengl.org/wiki/GluProject_and_gluUnProject_code
 */
public class Projection {

	public static Mat4 perspective(float fieldOfViewY, float aspect, float near, float far) {

		Mat4 projection = new Mat4();
		float[] m = projection.m;

		//change to radians and divide by 2
		// fieldOfView / 2 * PI / 180
		// fieldOfView *= 1 / 2 * PI / 180
		fieldOfViewY *= 0.0087266462599716f;
		float f = (1 / (float) Math.tan(fieldOfViewY) * fieldOfViewY);
		float deltaZ = far - near;

		m[0] = f / aspect;
		m[5] = f;
		m[10]= -(far + near) / deltaZ;
		m[11]= -1;
		m[14]= -2 * near * far / deltaZ;

		return projection;
	}

	public static Mat4 ortho(float left, float right, float bottom, float top, float near, float far) {

		Mat4 projection = Mat4.identity();
		float[] m = projection.m;

		m[0] = 2 / (right - left);
		m[5] = 2 / (top - bottom);
		m[10] = -2 / (far - near);
		m[12] = -(right + left) / (right - left);
		m[13] = -(top + bottom) / (top - bottom);
		m[14] = -(far + near) / (far - near);

		return projection;
	}

	public static Vec2 project() {

		return null;
	}

	// This is not the traditional unproject from glu. It is similar in the fact that it serves the same purpose but in
	// a better way. Instead of providing the point, it provides a ray from the front to the back of the
	// projection matrix. Usually gluunproject is called twice to get this ray. By simplifying the method I can limit
	// the number of matrix inversions.
	public static Ray3[] unproject(Vec2[] points, Mat4 transform, Mat4 projection, Rectangle viewport) {

		// calculate inverse matrix
		Mat4 A = projection.multiply(transform).inverse();
		if (A == null)
			return null;

		Ray3[] rays = new Ray3[points.length];

		// normalize points
		for (int i = 0; i < points.length; i++) {
			points[i].x = (points[i].x - viewport.x) / viewport.width * 2 - 1;
			points[i].y = (points[i].y - viewport.y) / viewport.height * 2 - 1;

			// calculate beginning and end of ray
			Vec3 start = A.multiply(new Vec3(points[i].x, points[i].y, -1), 1);
			Vec3 end = A.multiply(new Vec3(points[i].x, points[i].y, 1), 1);

			// check if above step failed
			if (start == null || end == null)
				continue;

			// transform start and ind into start and direction
			rays[i] = new Ray3(start, end.subtract(start));
		}

		return rays;
	}
}
