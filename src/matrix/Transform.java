package matrix;

/**
 * Created by Nyrmburk on 5/9/2016.
 * http://math.stackexchange.com/questions/82602/how-to-find-camera-position-and-rotation-from-a-4x4-matrix
 * https://www.opengl.org/wiki/GluLookAt_code
 */
public class Transform extends Mat4 {

	//  0  4  8 12
	//  1  5  9 13
	//  2  6 10 14
	//  3  7 11 15

	//  r  r  r  x
	//  r  r  r  y
	//  r  r  r  z
	//  ?  ?  ?  w?

	public static Vec3 getPosition(Mat4 matrix) {

		float[] m = matrix.m;
		return new Vec3(m[12], m[13], m[14]);
	}

	public static void setPosition(Mat4 matrix, Vec3 position) {

		float[] m = matrix.m;
		m[12] = position.x;
		m[13] = position.y;
		m[14] = position.z;
	}

	public static void translate(Mat4 matrix, Vec3 translate) {

		Mat4 translation = Mat4.identity();
		setPosition(translation, translate);

		matrix.m = matrix.multiply(translation).m;
	}

	public static Mat3 getRotationMatrix(Mat4 matrix) {

		float[] m = matrix.m;
		return new Mat3(
				m[0], m[4], m[8],
				m[1], m[5], m[9],
				m[2], m[6], m[10]);
	}

	public static Mat4 setRotationMatrix(Mat4 matrix, Mat3 rotation) {

		matrix.m[0] = rotation.m[0];
		matrix.m[1] = rotation.m[1];
		matrix.m[2] = rotation.m[2];
		matrix.m[4] = rotation.m[3];
		matrix.m[5] = rotation.m[4];
		matrix.m[6] = rotation.m[5];
		matrix.m[8] = rotation.m[6];
		matrix.m[9] = rotation.m[7];
		matrix.m[10]= rotation.m[8];

		return matrix;
	}

	public static Vec4 getRotationQuaternion(Mat4 matrix) {

		return null;
	}

	public static Mat4 setRotationQuaternion(Mat4 matrix, Vec4 quaternion) {

		return matrix;
	}

	public static void rotate(Mat4 matrix, Vec3 axis, float angle) {

		float[] r = matrix.m;

		float xx = axis.x * axis.x;
		float xy = axis.x * axis.y;
		float xz = axis.x * axis.z;
		float yy = axis.y * axis.y;
		float yz = axis.y * axis.z;
		float zz = axis.z * axis.z;

		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		float oneMinusCos = 1 - cos;

		r[0] = xx * oneMinusCos + cos;
		r[1] = xy * oneMinusCos + axis.z * sin;
		r[2] = xz * oneMinusCos - axis.y * sin;
		r[4] = xy * oneMinusCos - axis.z * sin;
		r[5] = yy * oneMinusCos + cos;
		r[6] = yz * oneMinusCos + axis.x * sin;
		r[8] = xz * oneMinusCos + axis.y * sin;
		r[9] = yz * oneMinusCos - axis.x * sin;
		r[10]= zz * oneMinusCos + cos;
	}

	public static void pointAt(Mat4 matrix, Vec3 position, Vec3 target, Vec3 up) {

		Vec3 forward = target.subtract(position).normalized();
		Vec3 side = forward.cross(up).normalized();
		up = side.cross(forward);

		Mat4 result = Mat4.identity();
		float[] m = result.m;

		m[0] = side.x;
		m[4] = side.y;
		m[8] = side.z;
		m[1] = up.x;
		m[5] = up.y;
		m[9] = up.z;
		m[2] = -forward.x;
		m[6] = -forward.y;
		m[10]= -forward.z;

		matrix.m = matrix.multiply(result).m;
		translate(matrix, position.negate());
	}
}
