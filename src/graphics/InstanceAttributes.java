package graphics;

import matrix.Mat4;

//Beef this up.
public class InstanceAttributes {

	private Mat4 transform = Mat4.identity();

	public Mat4 getTransform() {
		return transform;
	}

	public void setTransform(Mat4 transform) {

		this.transform = transform;
	}
}
