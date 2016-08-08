package graphics;

import matrix.Mat4;
import matrix.Projection;

import java.awt.*;

/**
 * Created by Nyrmburk on 7/24/2016.
 */
public class PerspectiveCamera extends Camera {

	private float fov;

	public PerspectiveCamera(float fov, float near, float far) {

		setFov(fov);
		setNear(near);
		setFar(far);
	}

	public PerspectiveCamera() {

		this(90, 0.1f, 1000f);
	}

	public float viewHeight(float distance, Dimension viewport) {

		return distance * (float) Math.tan(fov / 2);
	}

	public float viewWidth(float distance, Dimension viewport) {

		return viewHeight(distance, viewport) * aspectRatio(viewport);
	}

	public static float fovFromDistance(float height, float distance) {

		return (float) Math.toDegrees(2 * Math.atan(height / (2 * distance)));
	}

	public static float distanceFromFov(float height, float fov) {

		return height / 2 * (float) Math.tan(Math.toRadians(fov / 2));
	}

	public void setFov(float fov) {

		this.fov = (float) Math.toRadians(fov);
	}

	public float getFov() {

		return (float) Math.toDegrees(fov);
	}

	@Override
	public Mat4 getProjection(Dimension viewport) {

		return new Mat4(Projection.perspective(fov, aspectRatio(viewport), getNear(), getFar()));
	}

	private float aspectRatio(Dimension viewport) {

		return (float) viewport.width / viewport.height;
	}
}
