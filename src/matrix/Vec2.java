package matrix;

public class Vec2 {

	public static final int SIZE = 2;

	public float x;
	public float y;

	public Vec2() {
	}

	public Vec2(float x, float y) {

		this.x = x;
		this.y = y;
	}

	public Vec2(Vec2 copy) {

		this(copy.x, copy.y);
	}

	public Vec2 add(Vec2 toAdd) {

		return new Vec2(x + toAdd.x, y + toAdd.y);
	}

	public Vec2 subtract(Vec2 toSub) {

		return new Vec2(x - toSub.x, y - toSub.y);
	}

	public Vec2 multiply(Vec2 toMult) {

		return new Vec2(x * toMult.x, y * toMult.y);
	}

	public Vec2 multiply(float scalar) {

		return new Vec2(x * scalar, y * scalar);
	}

	public Vec2 divide(Vec2 toDiv) {

		return new Vec2(x / toDiv.x, y / toDiv.y);
	}

	public Vec2 divide(float scalar) {

		return new Vec2(x / scalar, y / scalar);
	}

	public float dot(Vec2 toDot) {

		return x * toDot.x + this.y * toDot.y;
	}

	public Vec2 transpose() {

		return new Vec2(-y, x);
	}

	public Vec2 reciprocal() {

		return new Vec2(1 / x, 1 / y);
	}

	public Vec2 negate() {

		return new Vec2(-x, -y);
	}

	public Vec2 normalized() {

		return new Vec2(this.divide(length()));
	}

	public void translate(Vec2 delta) {

		set(this.add(delta));
	}

	public void rotate(Vec2 center, float radians) {

		double angle = Math.atan2(y - center.y, x - center.x);
		angle += radians;

		float distance = this.distance(center);

		float x = (float) Math.cos(angle) * distance + center.x;
		float y = (float) Math.sin(angle) * distance + center.x;

		set(new Vec2(x, y));
	}

	public void scale(Vec2 center, float amount) {

		Vec2 delta = this.subtract(center).normalized();
		delta.multiply(amount);

		set(delta.add(center));
	}

	public float distance(Vec2 vec) {

		return vec.subtract(this).length();
	}

	public float length() {

		return (float) Math.sqrt(x * x + y * y);
	}

	public void set(Vec2 vec) {

		x = vec.x;
		y = vec.y;
	}

	public Vec3 asVec3(float z) {

		return new Vec3(x, y, z);
	}

	public Vec3 asVec3() {

		return asVec3(0);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Vec2) {

			Vec2 vec = (Vec2) obj;
			if (x == vec.x && y == vec.y)
				return true;
		}

		return false;
	}

	@Override
	public String toString() {

		return "[" + x + ", " + y + "]";
	}
}