package matrix;

/**
 * Created by Chris on 5/10/2016.
 */
public class Mat3 {

	public static final int SIZE = 3;
	public static final int TOTAL_SIZE = SIZE * SIZE; //9

	//  0  3  6
	//  1  4  7
	//  2  5  8
	public float[] m = new float[16];

	public Mat3() {
	}

	public Mat3(float... matrixArray) {

		System.arraycopy(matrixArray, 0, m, 0, TOTAL_SIZE);
	}

	public Mat3(Mat3 copy) {

		this(copy.m);
	}

	public Mat3 add(Mat3 toAdd) {

		Mat3 added = new Mat3();

		for (int i = 0; i < TOTAL_SIZE; i++)
			added.m[i] = m[i] + toAdd.m[i];

		return added;
	}

	public Mat3 subtract(Mat3 toSubtract) {

		Mat3 added = new Mat3();

		for (int i = 0; i < added.m.length; i++)
			added.m[i] = m[i] - toSubtract.m[i];

		return added;
	}

	public Mat3 multiply(float scalar) {

		Mat3 multiplied = new Mat3();
		for (int i = 0; i < TOTAL_SIZE; i++)
			multiplied.m[i] = m[i] * scalar;

		return multiplied;
	}

	public Mat3 divide(float scalar) {

		Mat3 divided = new Mat3();
		for (int i = 0; i < TOTAL_SIZE; i++)
			divided.m[i] = m[i] / scalar;

		return divided;
	}

	public float dot(Mat3 toDot) {

		float dot = 0;
		for (int i = 0; i < TOTAL_SIZE; i++)
			dot += m[i] * toDot.m[i];

		return dot;
	}

	public Mat3 transpose() {

		//  0  1  2  3  4  5  6  7  8
		//  0  3  6  1  4  7  2  5  8

		Mat3 transposed = new Mat3();

		for (int i = 0; i < TOTAL_SIZE; i++)
			transposed.m[i] = m[(i * SIZE) % TOTAL_SIZE + i / SIZE];

		return transposed;
	}

	public float trace() {

		return m[0] + m[4] + m[8];
	}

	public static Mat3 identity() {

		return new Mat3(
				1, 0, 0,
				0, 1, 0,
				0, 0, 1);
	}
}