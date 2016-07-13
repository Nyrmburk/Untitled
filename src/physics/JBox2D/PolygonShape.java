package physics.JBox2D;

import matrix.Mat4;
import matrix.Vec2;
import matrix.Vec3;
import org.jbox2d.collision.shapes.Shape;
import physics.Shape2;

import static physics.JBox2D.Convert.convert;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public class PolygonShape implements JBox2DShape {

	private org.jbox2d.collision.shapes.PolygonShape shape = new org.jbox2d.collision.shapes.PolygonShape();

	@Override
	public Shape getShape() {

		return shape;
	}

	public void setVertices(Vec2[] vertices) {

		shape.set(convert(vertices), vertices.length);
	}

	@Override
	public ShapeType getType() {

		return ShapeType.POLYGON;
	}

	@Override
	public float getRadius() {

		return shape.getRadius();
	}

	@Override
	public void setRadius(float radius) {

		shape.setRadius(radius);
	}

	@Override
	public int getChildCount() {

		return shape.getChildCount();
	}

	@Override
	public boolean testPoint(Mat4 transform, Vec3 point) {

		return shape.testPoint(convert(transform), convert(point));
	}

	@Override
	public boolean raycast(Object output, Object input, Mat4 transform, int childIndex) {
		return false;
	}

	@Override
	public void computeBounds() {

	}

	@Override
	public void computeMass() {

	}

	@Override
	public float computeDistanceToOut(Mat4 transform, Vec3 point, int childIndex, Vec3 normalOut) {
		return 0;
	}
}
