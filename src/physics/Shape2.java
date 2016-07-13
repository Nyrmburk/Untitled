package physics;

import matrix.Mat4;
import matrix.Vec3;

/**
 * Created by Nyrmburk on 7/12/2016.
 */
public interface Shape2 {

	enum ShapeType {

		CIRCLE,
		EDGE,
		POLYGON,
		COMPLEX_POLYGON,
		CHAIN,
	}

	ShapeType getType();

	float getRadius();

	void setRadius(float radius);

	int getChildCount();

	boolean testPoint(Mat4 transform, Vec3 point);

	boolean raycast(Object output, Object input, Mat4 transform, int childIndex);

	void computeBounds();

	void computeMass();

	float computeDistanceToOut(Mat4 transform, Vec3 point, int childIndex, Vec3 normalOut);
}
