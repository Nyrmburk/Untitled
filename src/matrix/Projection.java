package matrix;

/**
 * Created by Nyrmburk on 5/12/2016.
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

	public static Vec3 unproject() {

		return null;
	}
}
