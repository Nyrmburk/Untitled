package graphics;

import matrix.Mat4;
import matrix.Projection;

import java.awt.*;

/**
 * Created by Nyrmburk on 7/24/2016.
 */
public class GUICamera extends Camera {

	@Override
	public Mat4 getProjection(Dimension viewport) {

		return Projection.ortho(0, viewport.width, viewport.height, 0, -1, 1);
	}
}
