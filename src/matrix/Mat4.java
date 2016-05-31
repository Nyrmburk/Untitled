package matrix;

/**
 * Created by Chris on 5/10/2016.
 */
public class Mat4 {

	public static final int SIZE = 4;
	public static final int TOTAL_SIZE = SIZE * SIZE; //16

	//  0  4  8 12
	//  1  5  9 13
	//  2  6 10 14
	//  3  7 11 15
	public float[] m = new float[16];

	public Mat4() {
	}

	public Mat4(float... matrixArray) {

		System.arraycopy(matrixArray, 0, m, 0, TOTAL_SIZE);
	}

	public Mat4(Mat4 copy) {

		this(copy.m);
	}

	public Mat4 add(Mat4 toAdd) {

		Mat4 added = new Mat4();

		for (int i = 0; i < TOTAL_SIZE; i++)
			added.m[i] = m[i] + toAdd.m[i];

		return added;
	}

	public Mat4 subtract(Mat4 toSubtract) {

		Mat4 added = new Mat4();

		for (int i = 0; i < added.m.length; i++)
			added.m[i] = m[i] - toSubtract.m[i];

		return added;
	}

	public Mat4 multiply(float scalar) {

		Mat4 multiplied = new Mat4();
		for (int i = 0; i < TOTAL_SIZE; i++)
			multiplied.m[i] = m[i] * scalar;

		return multiplied;
	}

	public Mat4 multiply(Mat4 matrix) {

		Mat4 multiplied = new Mat4();
		for (int i = 0; i < TOTAL_SIZE; i++) {
			int j = i % SIZE;
			int k = j;
			// dot product
			multiplied.m[i] =
			//		columns  rows
					m[j++] * matrix.m[k += SIZE] +
					m[j++] * matrix.m[k += SIZE] +
					m[j++] * matrix.m[k += SIZE] +
					m[j  ] * matrix.m[k        ];
		}

		return multiplied;
	}

	public Mat4 divide(float scalar) {

		Mat4 divided = new Mat4();
		for (int i = 0; i < TOTAL_SIZE; i++)
			divided.m[i] = m[i] / scalar;

		return divided;
	}

	public float dot(Mat4 toDot) {

		float dot = 0;
		for (int i = 0; i < TOTAL_SIZE; i++)
			dot += m[i] * toDot.m[i];

		return dot;
	}

	public Mat4 transpose() {

		return new Mat4(
				m[0], m[4], m[8], m[12],
				m[1], m[5], m[9], m[13],
				m[2], m[6], m[10], m[14],
				m[3], m[7], m[11], m[15]);
	}

	public float trace() {

		return m[0] + m[5] + m [10] + m[15];
	}

	public Mat4 swapRows(int row1, int row2) {

		Mat4 swapped = new Mat4(this);

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

	public Mat4 swapCols(int col1, int col2) {

		col1 *= SIZE;
		col2 *= SIZE;

		Mat4 swapped = new Mat4(this);

		float[] tempCol = new float[SIZE];

		System.arraycopy(swapped.m, col1, tempCol, 0, SIZE); // copy first col to temp
		System.arraycopy(swapped.m, col2, swapped.m, col1, SIZE); // copy second col to first
		System.arraycopy(tempCol, 0, swapped.m, col2, SIZE); // copy temp to second col

		return swapped;
	}

	public static Mat4 identity() {

		return new Mat4(
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				sb.append(String.format("%6.2f ", m[i * SIZE + j]));
			}
			sb.append('\n');
		}

		return sb.toString();
	}
}
