package matrix;

/**
 * Created by Nyrmburk on 6/2/2016.
 */
public class Ray3 {

	public Vec3 position;
	public Vec3 direction;

	public Ray3(Vec3 position, Vec3 direction) {

		this.position = position;
		this.direction = direction;
	}

	public Ray3(float... pn) {

		this(new Vec3(pn[0], pn[1], pn[2]), new Vec3(pn[3], pn[4], pn[5]));
	}

	public Vec3 point(float t) {

		return position.add(direction.multiply(t));
	}
}
