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

	public Vec4 multiply(Vec4 vec) {

		Vec4 multiplied = new Vec4();

		multiplied.x = vec.x * m[0] + vec.y * m[4] + vec.z * m[8] + vec.w * m[12];
		multiplied.y = vec.x * m[1] + vec.y * m[5] + vec.z * m[9] + vec.w * m[13];
		multiplied.z = vec.x * m[2] + vec.y * m[6] + vec.z * m[10] + vec.w * m[14];
		multiplied.w = vec.x * m[3] + vec.y * m[7] + vec.z * m[11] + vec.w * m[15];

		return multiplied;
	}

	public Vec3 multiply(Vec3 vec, float w) {

		Vec4 multiplied = multiply(new Vec4(vec.x, vec.y, vec.z, w));

		if (multiplied.w == 0)
			return null;

		return new Vec3(multiplied.x, multiplied.y, multiplied.z).divide(multiplied.w);
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

	//  0  4  8 12
	//  1  5  9 13
	//  2  6 10 14
	//  3  7 11 15
	// http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche23.html
	public float determinant() {

		float determinant = 0;

		determinant += m[0]*m[5]*m[10]*m[15] - m[0]*m[5]*m[14]*m[11];
		determinant += m[0]*m[9]*m[14]*m[7] - m[0]*m[9]*m[6]*m[15];
		determinant += m[0]*m[13]*m[6]*m[11] - m[0]*m[13]*m[10]*m[7];

		determinant += m[4]*m[1]*m[14]*m[11] - m[4]*m[1]*m[10]*m[15];
		determinant += m[4]*m[9]*m[2]*m[15] - m[4]*m[9]*m[14]*m[3];
		determinant += m[4]*m[13]*m[10]*m[3] - m[4]*m[13]*m[2]*m[11];

		determinant += m[8]*m[1]*m[6]*m[15] - m[8]*m[1]*m[14]*m[7];
		determinant += m[8]*m[5]*m[14]*m[3] - m[8]*m[5]*m[2]*m[15];
		determinant += m[8]*m[13]*m[2]*m[7] - m[8]*m[13]*m[6]*m[3];

		determinant += m[12]*m[1]*m[10]*m[7] - m[12]*m[1]*m[6]*m[11];
		determinant += m[12]*m[5]*m[2]*m[11] - m[12]*m[5]*m[10]*m[3];
		determinant += m[12]*m[9]*m[6]*m[3] - m[12]*m[9]*m[2]*m[7];

		return determinant;
	}

	//  0  4  8 12
	//  1  5  9 13
	//  2  6 10 14
	//  3  7 11 15
	// http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche23.html
	public Mat4 inverse() {

		float determinant = this.determinant();

		//inverse does not exist
		if (determinant == 0)
			return null;

		Mat4 inverse = new Mat4();

		inverse.m[0] = m[5]*m[10]*m[15] + m[9]*m[14]*m[7] + m[13]*m[6]*m[11];
		inverse.m[0] -= m[5]*m[14]*m[11] + m[9]*m[6]*m[15] + m[13]*m[10]*m[7];
		inverse.m[0] /= determinant;

		inverse.m[4] = m[4]*m[14]*m[11] + m[8]*m[6]*m[15] + m[12]*m[10]*m[7];
		inverse.m[4] -= m[4]*m[10]*m[15] + m[8]*m[14]*m[7] + m[12]*m[6]*m[11];
		inverse.m[4] /= determinant;

		inverse.m[8] = m[4]*m[9]*m[15] + m[8]*m[13]*m[7] + m[12]*m[5]*m[11];
		inverse.m[8] -= m[4]*m[13]*m[11] + m[8]*m[5]*m[15] + m[12]*m[9]*m[7];
		inverse.m[8] /= determinant;

		inverse.m[12] = m[4]*m[13]*m[10] + m[8]*m[5]*m[14] + m[12]*m[9]*m[6];
		inverse.m[12] -= m[4]*m[9]*m[14] + m[8]*m[13]*m[6] + m[12]*m[5]*m[10];
		inverse.m[12] /= determinant;

		inverse.m[1] = m[1]*m[14]*m[11] + m[9]*m[2]*m[15] + m[13]*m[10]*m[3];
		inverse.m[1] -= m[1]*m[10]*m[15] + m[9]*m[14]*m[3] + m[13]*m[2]*m[11];
		inverse.m[1] /= determinant;

		inverse.m[5] = m[0]*m[10]*m[15] + m[8]*m[14]*m[3] + m[12]*m[2]*m[11];
		inverse.m[5] -= m[0]*m[14]*m[11] + m[8]*m[2]*m[15] + m[12]*m[10]*m[3];
		inverse.m[5] /= determinant;

		inverse.m[9] = m[0]*m[13]*m[11] + m[8]*m[1]*m[15] + m[12]*m[9]*m[3];
		inverse.m[9] -= m[0]*m[9]*m[15] + m[8]*m[13]*m[3] + m[12]*m[1]*m[11];
		inverse.m[9] /= determinant;

		inverse.m[13] = m[0]*m[9]*m[14] + m[8]*m[13]*m[2] + m[12]*m[1]*m[10];
		inverse.m[13] -= m[0]*m[13]*m[10] + m[8]*m[1]*m[14] + m[12]*m[9]*m[2];
		inverse.m[13] /= determinant;

		inverse.m[2] = m[1]*m[6]*m[15] + m[5]*m[14]*m[3] + m[13]*m[2]*m[7];
		inverse.m[2] -= m[1]*m[14]*m[7] + m[5]*m[2]*m[15] + m[13]*m[6]*m[3];
		inverse.m[2] /= determinant;

		inverse.m[6] = m[0]*m[14]*m[7] + m[4]*m[2]*m[15] + m[12]*m[6]*m[3];
		inverse.m[6] -= m[0]*m[6]*m[15] + m[4]*m[14]*m[3] + m[12]*m[2]*m[7];
		inverse.m[6] /= determinant;

		inverse.m[10] = m[0]*m[5]*m[15] + m[4]*m[13]*m[3] + m[12]*m[1]*m[7];
		inverse.m[10] -= m[0]*m[13]*m[7] + m[4]*m[1]*m[15] + m[12]*m[5]*m[3];
		inverse.m[10] /= determinant;

		inverse.m[14] = m[0]*m[13]*m[6] + m[4]*m[1]*m[14] + m[12]*m[5]*m[2];
		inverse.m[14] -= m[0]*m[5]*m[14] + m[4]*m[13]*m[2] + m[12]*m[1]*m[6];
		inverse.m[14] /= determinant;

		inverse.m[3] = m[1]*m[10]*m[7] + m[5]*m[2]*m[11] + m[9]*m[6]*m[3];
		inverse.m[3] -= m[1]*m[6]*m[11] + m[5]*m[10]*m[3] + m[9]*m[2]*m[7];
		inverse.m[3] /= determinant;

		inverse.m[7] = m[0]*m[6]*m[11] + m[4]*m[10]*m[3] + m[8]*m[2]*m[7];
		inverse.m[7] -= m[0]*m[10]*m[7] + m[4]*m[2]*m[11] + m[8]*m[6]*m[3];
		inverse.m[7] /= determinant;

		inverse.m[11] = m[0]*m[9]*m[7] + m[4]*m[1]*m[11] + m[8]*m[5]*m[3];
		inverse.m[11] -= m[0]*m[5]*m[11] + m[4]*m[9]*m[3] + m[8]*m[1]*m[7];
		inverse.m[11] /= determinant;

		inverse.m[15] = m[0]*m[5]*m[10] + m[4]*m[9]*m[2] + m[8]*m[1]*m[6];
		inverse.m[15] -= m[0]*m[9]*m[6] + m[4]*m[1]*m[10] + m[8]*m[5]*m[2];
		inverse.m[15] /= determinant;

		return inverse;
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

				sb.append(String.format("%6.2f ", m[j * SIZE + i]));
			}
			sb.append('\n');
		}

		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}
}
