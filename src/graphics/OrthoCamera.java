package graphics;

import matrix.Mat4;
import matrix.Projection;

import java.awt.*;

/**
 * Created by Nyrmburk on 7/24/2016.
 */
public class OrthoCamera extends Camera {

	public OrthoCamera(float near, float far) {

		setNear(near);
		setFar(far);
	}

	public OrthoCamera() {

		this(-1, 1);
	}

	@Override
	public Mat4 getProjection(Dimension viewport) {

		float width = (float) viewport.width / 2;
		float height = (float) viewport.height / 2;
		return Projection.ortho(-width, width, -height, height, getNear(), getFar());
	}
}
