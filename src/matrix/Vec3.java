package matrix;

/**
 * Created by Chris on 5/10/2016.
 */
public class Vec3 {

	public static final int SIZE = 3;

	public float x;
	public float y;
	public float z;

	public Vec3() {
	}

	public Vec3(float x, float y, float z) {

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(Vec3 copy) {

		this(copy.x, copy.y, copy.z);
	}

	public Vec3 add(Vec3 toAdd) {

		return new Vec3(x + toAdd.x, y + toAdd.y, z + toAdd.z);
	}

	public Vec3 subtract(Vec3 toSub) {

		return new Vec3(x - toSub.x, y - toSub.y, z - toSub.z);
	}

	public Vec3 multiply(Vec3 toMult) {

		return new Vec3(x * toMult.x, y * toMult.y, z * toMult.z);
	}

	public Vec3 multiply(float scalar) {

		return new Vec3(x * scalar, y * scalar, z * scalar);
	}

	public Vec3 divide(Vec3 toDiv) {

		return new Vec3(x / toDiv.x, y / toDiv.y, z / toDiv.z);
	}

	public Vec3 divide(float scalar) {

		return new Vec3(x / scalar, y / scalar, z / scalar);
	}

	public float dot(Vec3 toDot) {

		return x * toDot.x + this.y * toDot.y + z * toDot.z;
	}

	public Vec3 cross(Vec3 toCross) {

		Vec3 crossed = new Vec3();

		crossed.x = y * toCross.z - z * toCross.y;
		crossed.y = z * toCross.x - x * toCross.z;
		crossed.z = x * toCross.y - y * toCross.x;

		return crossed;
	}

	public float angle(Vec3 toCompare) {

		return (float) Math.acos(this.dot(toCompare) / (this.length() * toCompare.length()));
	}

	public Vec3 reciprocal() {

		return new Vec3(1 / x, 1 / y, 1 / z);
	}

	public Vec3 negate() {

		return new Vec3(-x, -y, -z);
	}

	public Vec3 normalized() {

		return new Vec3(this.divide(length()));
	}


	public void translate(Vec3 delta) {

		setPosition(this.add(delta));
	}

	//TODO needs axis
//	public void rotate(Vec3 center, float radians) {
//
//		double angle = Math.atan2(y - center.getY(), x - center.getX());
//		angle += radians;
//
//		float length = this.length(center);
//
//		float x = (float) Math.cos(angle) * length + center.getX();
//		float y = (float) Math.sin(angle) * length + center.getY();
//
//		setPosition(new Vec3(x, y, z));
//	}

	public void scale(Vec3 center, float amount) {

		Vec3 delta = this.subtract(center).normalized();
		delta.multiply(amount);

		setPosition(delta.add(center));
	}

	public float distance(Vec3 vec) {

		return vec.subtract(this).length();
	}

	public float length() {

		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public void setPosition(Vec3 vec) {

		x = vec.x;
		y = vec.y;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Vec3) {

			Vec3 vec = (Vec3) obj;
			if (x == vec.x && y == vec.y && z == vec.z)
				return true;
		}

		return false;
	}

	@Override
	public String toString() {

		return "[" + x + ", " + y + ", " + z + "]";
	}
}
