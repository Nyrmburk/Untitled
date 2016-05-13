package main;

import matrix.Mat3;
import matrix.Mat4;
import matrix.Vec3;
import matrix.Vec4;

/**
 * Created by Nyrmburk on 5/9/2016.
 * http://math.stackexchange.com/questions/82602/how-to-find-camera-position-and-rotation-from-a-4x4-matrix
 */
public class Transform {

	//  0  4  8 12
	//  1  5  9 13
	//  2  6 10 14
	//  3  7 11 15

	//  r  r  r  x
	//  r  r  r  y
	//  r  r  r  z
	//  ?  ?  ?  w?
	public Mat4 transform;

	public Transform() {

		transform = Mat4.identity();
	}

	public Transform(Transform copy) {

		transform = new Mat4(copy.getMatrix());
	}

	public Mat4 getMatrix() {

		return transform;
	}

	public Vec3 getPosition() {

		float[] m = transform.m;
		return new Vec3(m[12], m[13], m[14]);
	}

	public void setPosition(Vec3 position) {

		float[] m = transform.m;
		m[12] = position.x;
		m[13] = position.y;
		m[14] = position.z;
	}

	public Mat3 getRotationMatrix() {

		float[] m = transform.m;
		return new Mat3(
				m[0], m[4], m[8],
				m[1], m[5], m[9],
				m[2], m[6], m[10]);
	}

	public void setRotationMatrix(Mat3 matrix) {

		float[] m = transform.m;
		m[0] = matrix.m[0];
		m[1] = matrix.m[1];
		m[2] = matrix.m[2];
		m[4] = matrix.m[3];
		m[5] = matrix.m[4];
		m[6] = matrix.m[5];
		m[8] = matrix.m[6];
		m[9] = matrix.m[7];
		m[10] = matrix.m[8];
	}

	public Vec4 getRotationQuaternion() {

		return null;
	}

	public void setRotationQuaternion(Vec4 quaternion) {

	}

	public Mat4 rotate(Vec3 axis, float angle) {

		Mat4 rotated = Mat4.identity();
		float[] r = rotated.m;

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

		return transform.multiply(rotated);
	}

	public Mat4 pointAt(Vec3 position, Vec3 target, Vec3 up) {

		Vec3 forward = target.subtract(position).normalized();
		Vec3 side = forward.cross(up);
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

		result = transform.multiply(result);

		return result;
	}

	public void project() {

	}
}
