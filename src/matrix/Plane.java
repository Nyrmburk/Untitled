package matrix;

/**
 * Created by Nyrmburk on 6/3/2016.
 */
public class Plane {

	public Vec3 position;
	public Vec3 normal;

	public Plane(Vec3 position, Vec3 normal) {

		this.position = position;
		this.normal = normal;
	}

	public Plane(float... pn) {

		this(new Vec3(pn[0], pn[1], pn[2]), new Vec3(pn[3], pn[4], pn[5]));
	}

	public float intersect(Ray3 ray) {

		Vec3 relativePosition = position.subtract(ray.position);

		float denominator = ray.direction.dot(normal);

		if (denominator == 0)
			return Float.NaN;

		return (relativePosition.dot(normal)) / denominator;
	}
}
