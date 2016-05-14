package entity;

import matrix.Mat4;
import matrix.Vec3;

public class Camera {

	private Mat4 projection;

	private Mat4 transform = Mat4.identity();
	private Vec3 up;

	public Camera(Mat4 projection) {

		setProjection(projection);
	}

	public Mat4 getTransform() {
		return transform;
	}

	public void setTransform(Mat4 transform) {
		this.transform = transform;
	}

	public static float aspectRatioFromResolution(float width, float height) {

		return width / height;
	}

	public Vec3 getUp() {
		return up;
	}

	public void setUp(Vec3 up) {
		this.up = up;
	}

	public Mat4 getProjection() {

		return projection;
	}

	public void setProjection(Mat4 projection) {

		this.projection = projection;
	}
}
