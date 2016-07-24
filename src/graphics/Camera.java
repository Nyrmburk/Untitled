package graphics;

import matrix.Mat4;
import matrix.Vec3;

import java.awt.*;

public abstract class Camera {

	private Mat4 transform = Mat4.identity();
	private Vec3 up;

	private float near;
	private float far;

	public Mat4 getTransform() {

		return transform;
	}

	public void setTransform(Mat4 transform) {
		this.transform = transform;
	}

	public void setNear(float near) {

		this.near = near;
	}

	public float getNear(){

		return near;
	}

	public void setFar(float far) {

		this.far = far;
	}

	public float getFar() {

		return far;
	}

	public Vec3 getUp() {

		return up;
	}

	public void setUp(Vec3 up) {

		this.up = up;
	}

	public abstract Mat4 getProjection(Dimension viewport);
}
