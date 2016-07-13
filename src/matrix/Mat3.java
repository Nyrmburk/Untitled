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

	public Mat3 multiply(Mat3 matrix) {

		Mat3 multiplied = new Mat3();
		for (int i = 0; i < TOTAL_SIZE; i++) {

			int row = i % SIZE;
			int col = i / SIZE * SIZE;

			float dot = 0;
			for (int j = 0; j < SIZE; j++) {
				dot += m[row] * matrix.m[col++];
				row += SIZE;
			}

			multiplied.m[i] = dot;
		}

		return multiplied;
	}

	public Vec3 multiply(Vec3 vector) {

		//TODO
		return null;
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

		return new Mat3(
				m[0], m[3], m[6],
				m[1], m[4], m[7],
				m[2], m[5], m[8]);
	}

	public float trace() {

		return m[0] + m[4] + m[8];
	}

	public Mat3 swapRows(int row1, int row2) {

		Mat3 swapped = new Mat3(this);

		float[] tempRow = new float[SIZE];

		// copy first row to temp
		for (int i = 0; i < SIZE; i++)
			tempRow[i] = swapped.m[i * SIZE + row1];

		// copy second row to first
		for (int i = 0; i < SIZE; i++)
			swapped.m[i * SIZE + row1] = swapped.m[i * SIZE + row2];

		// copy temp to second
		for (int i = 0; i < SIZE; i++)
			swapped.m[i * SIZE + row2] = tempRow[i];

		return swapped;
	}

	public Mat3 swapCols(int col1, int col2) {

		col1 *= SIZE;
		col2 *= SIZE;

		Mat3 swapped = new Mat3(this);

		float[] tempCol = new float[SIZE];

		System.arraycopy(swapped.m, col1, tempCol, 0, SIZE); // copy first col to temp
		System.arraycopy(swapped.m, col2, swapped.m, col1, SIZE); // copy second col to first
		System.arraycopy(tempCol, 0, swapped.m, col2, SIZE); // copy temp to second col

		return swapped;
	}

	public static Mat3 identity() {

		return new Mat3(
				1, 0, 0,
				0, 1, 0,
				0, 0, 1);
	}
}