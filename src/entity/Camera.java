package entity;

import main.Transform;

public class Camera {

	private float aspectRatio;
	private float fieldOfView = 90;
	private Transform transform = new Transform();

	public Camera(float aspectRatio, float fieldOfView) {

		setAspectRatio(aspectRatio);
		setFieldOfView(fieldOfView);
	}

	public Camera(int width, int height, float fieldOfView) {

		 this(aspectRatioFromResolution(width, height), fieldOfView);
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public float getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public static float aspectRatioFromResolution(int width, int height) {

		return ((float) width) / height;
	}
}
