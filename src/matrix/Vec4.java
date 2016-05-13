package matrix;

/**
 * Created by Chris on 5/10/2016.
 */
public class Vec4 {

	public static final int SIZE = 4;

	public float x;
	public float y;
	public float z;
	public float w;

	public Vec4() {
	}

	public Vec4(float x, float y, float z, float w) {

		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4(Vec4 copy) {

		this(copy.x, copy.y, copy.z, copy.w);
	}

	public Vec4 add(Vec4 toAdd) {

		return new Vec4(x + toAdd.x, y + toAdd.y, z + toAdd.z, w + toAdd.w);
	}

	public Vec4 subtract(Vec4 toSub) {

		return new Vec4(x - toSub.x, y - toSub.y, z - toSub.z, w - toSub.w);
	}

	public Vec4 multiply(Vec4 toMult) {

		return new Vec4(x * toMult.x, y * toMult.y, z * toMult.z, w * toMult.w);
	}

	public Vec4 multiply(float scalar) {

		return new Vec4(x * scalar, y * scalar, z * scalar, w * scalar);
	}

	public Vec4 divide(Vec4 toDiv) {

		return new Vec4(x / toDiv.x, y / toDiv.y, z / toDiv.z, w / toDiv.w);
	}

	public Vec4 divide(float scalar) {

		return new Vec4(x / scalar, y / scalar, z / scalar, w / scalar);
	}

	public float dot(Vec4 toDot) {

		return x * toDot.x + this.y * toDot.y + z * toDot.z + w * toDot.w;
	}

	public Vec4 reciprocal() {

		return new Vec4(1 / x, 1 / y, 1 / z, 1 / w);
	}

	public Vec4 negate() {

		return new Vec4(-x, -y, -z, -w);
	}

	public Vec4 normalized() {

		return new Vec4(this.divide(length()));
	}


	public float distance(Vec4 vec) {

		return vec.subtract(this).length();
	}

	public float length() {

		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public void setPosition(Vec4 vec) {

		x = vec.x;
		y = vec.y;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Vec4) {

			Vec4 vec = (Vec4) obj;
			if (x == vec.x && y == vec.y && z == vec.z && w == vec.w)
				return true;
		}

		return false;
	}

	@Override
	public String toString() {

		return "[" + x + ", " + y + ", " + z + ", " + w + "]";
	}
}
