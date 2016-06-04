package matrix;

/**
 * Created by Nyrmburk on 6/2/2016.
 */
public class Ray3 {

	public Vec3 position;
	public Vec3 direction;

	public Ray3 (Vec3 position, Vec3 direction) {

		this.position = position;
		this.direction = direction;
	}

	public Vec3 point(float t) {

		return position.add(direction.multiply(t));
	}
}
