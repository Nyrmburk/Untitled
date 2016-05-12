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
//		System.out.println(Arrays.toString(m));
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

	public void rotate() {

	}

	public void pointAt() {

	}

	public void project() {

	}
}
