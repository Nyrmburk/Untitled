package main;

/**
 * A vector class to do conversions from Cartesian to Polar coordinates, and to
 * perform matrix math.
 * 
 * @version 1.2
 * @author Christopher Dombroski
 * 
 */
public class Vector {

	
	/**
	 * Converts polar coordinates to Cartesian.
	 * @param magnitude
	 * @param azimuthal
	 * @param polar
	 * @return
	 */
	public static float[] toCartesian(float magnitude, float azimuthal,
			float polar) {

		float[] coords;
		coords = new float[3];

		coords[0] = magnitude * (float) Math.sin(polar)
				* (float) Math.cos(azimuthal); // X
		coords[1] = magnitude * (float) Math.sin(polar)
				* (float) Math.sin(azimuthal); // Y
		coords[2] = magnitude * (float) Math.cos(polar); // Z

		return coords;
	}

	/**
	 * Converts Polar coordinates to Cartesian.
	 * @param magnitude
	 * @param azimuthal
	 * @return
	 */
	public static float[] toCartesian(float magnitude, float azimuthal) {

		float[] coords;
		coords = new float[3];

		coords[0] = magnitude * (float) Math.PI / 2
				* (float) Math.cos(azimuthal); // X
		coords[1] = magnitude * (float) Math.PI / 2
				* (float) Math.sin(azimuthal); // Y
		coords[2] = magnitude * (float) Math.PI / 2; // Z

		return coords;
	}

	/**
	 * Converts Polar coordinates to Cartesian.
	 * @param vector
	 * @return
	 */
	public static float[] toCartesian(float[] vector) {

		float[] coords;
		coords = new float[3];

		coords[0] = vector[0] * (float) Math.sin(vector[2])
				* (float) Math.cos(vector[1]); // X
		coords[1] = vector[0] * (float) Math.sin(vector[2])
				* (float) Math.sin(vector[1]); // Y
		coords[2] = vector[0] * (float) Math.cos(vector[2]); // Z

		return coords;
	}

	/**
	 * Converts Cartesian coordinates to Polar.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return
	 */
	public static float[] toSpherical(float x, float y, float z) {

		float[] vector;
		vector = new float[3];

		vector[0] = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
				+ Math.pow(z, 2)); // magnitude
		vector[1] = (float) Math.atan2(y, x); // azimuthal

		if (vector[0] != 0) {
			vector[2] = (float) Math.acos(z / vector[0]); // polar
		} else {
			vector[2] = 0;
		}

		return vector;
	}

	/**
	 * Converts Cartesian coordinates to Polar.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 *
	 * @return
	 */
	public static float[] toSpherical(float x, float y) {

		float[] vector;
		vector = new float[3];

		vector[0] = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // magnitude
		vector[1] = (float) Math.atan2(y, x); // azimuthal
		vector[2] = (float) Math.PI / 2; // polar

		return vector;
	}

	/**
	 * 
	 * @param coords
	 * @return
	 */
	public static float[] toSpherical(float[] coords) {

		float[] vector;
		vector = new float[3];

		vector[0] = (float) Math.sqrt(Math.pow(coords[0], 2)
				+ Math.pow(coords[1], 2) + Math.pow(coords[2], 2)); // magnitude
		vector[1] = (float) Math.atan2(coords[1], coords[0]); // azimuthal
		if (vector[0] != 0) {
			vector[2] = (float) Math.acos(coords[2] / vector[0]); // polar
		} else {
			vector[2] = 0;
		}

		return vector;
	}

	/**
	 * Perform matrix addition on spherical coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] sAddVector(float[] vector1, float[] vector2) {

		float[] newVector;
		float[][] coords;
		newVector = new float[3];
		coords = new float[2][3];

		float x, y, z;

		coords[0] = toCartesian(vector1);
		coords[1] = toCartesian(vector2);

		x = coords[0][0] + coords[1][0];
		y = coords[0][1] + coords[1][1];
		z = coords[0][2] + coords[1][2];

		newVector = toSpherical(x, y, z);

		return newVector;
	}

	/**
	 * Perform matrix addition on cartesian coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] cAddVector(float[] vector1, float[] vector2) {

		float[] newVector = new float[3];
		int vectorLength = 0;

		if (vector1.length == vector2.length) {

			vectorLength = vector1.length;

		} else {

			System.out.println("Vector type mismatch");
			System.out.println("vector1 length: " + vector1.length
					+ " vector2 length: " + vector2.length);

			return null;

		}

		for (int i = 0; i < vectorLength; i++) {

			newVector[i] = vector1[i] + vector2[i];

		}

		return newVector;
	}

	/**
	 * perform matrix subtraction on spherical coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] sSubVector(float[] vector1, float[] vector2) {

		float[] newVector;
		float[][] coords;
		newVector = new float[3];
		coords = new float[2][3];

		float x, y, z;

		coords[0] = toCartesian(vector1);
		coords[1] = toCartesian(vector2);

		x = coords[0][0] - coords[1][0];
		y = coords[0][1] - coords[1][1];
		z = coords[0][2] - coords[1][2];

		newVector = toSpherical(x, y, z);

		return newVector;
	}

	/**
	 * perform matrix subtraction on Cartesian coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] cSubVector(float[] vector1, float[] vector2) {

		float[] newVector = new float[3];
		int vectorLength = 0;

		if (vector1.length == vector2.length) {

			vectorLength = vector1.length;

		} else {

			System.out.println("Vector type mismatch");
			System.out.println("vector1 length: " + vector1.length
					+ " vector2 length: " + vector2.length);

			return null;

		}

		for (int i = 0; i < vectorLength; i++) {

			newVector[i] = vector1[i] - vector2[i];

		}

		return newVector;
	}

	/**
	 * Perform the dot product operation on spherical coordinates.
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float sDotVector(float[] vector1, float[] vector2) {

		float scalar;
		float angle;

		angle = (float) Math.acos(Math.sin(vector1[1]) * Math.sin(vector1[1])
				* Math.cos(vector1[2] - vector2[2]) + Math.cos(vector1[1])
				* Math.cos(vector2[1]));

		scalar = (float) (vector1[0] * vector2[0] * Math.cos(angle));

		return scalar;
	}

	/**
	 * perform the dot product operation on Cartesian coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	@SuppressWarnings("null")
	public static float cDotVector(float[] vector1, float[] vector2) {

		float scalar = 0;
		int vectorLength = 0;

		if (vector1.length == vector2.length) {

			vectorLength = vector1.length;

		} else {

			System.out.println("Vector type mismatch");
			System.out.println("vector1 length: " + vector1.length
					+ " vector2 length: " + vector2.length);

			return (Float) null;

		}

		for (int i = 0; i < vectorLength; i++) {

			scalar += vector1[i] * vector2[i];

		}

		return scalar;
	}

	/**
	 * Perform the cross product on speherical coordinates
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] sCrossVector(float[] vector1, float[] vector2) {

		float[] newVector;
		float[][] coords;
		newVector = new float[3];
		coords = new float[3][3];

		float x, y, z;

		coords[0] = toCartesian(vector1);
		coords[1] = toCartesian(vector2);

		x = coords[0][1] * coords[1][2] - coords[0][2] * coords[1][1]; // c_x =
																		// a_y *
																		// b_z -
																		// a_z *
																		// b_y
		y = coords[0][2] * coords[1][0] - coords[0][0] * coords[1][2]; // c_y =
																		// a_z *
																		// b_x -
																		// a_x *
																		// b_z
		z = coords[0][0] * coords[1][1] - coords[0][1] * coords[1][0]; // c_z =
																		// a_x *
																		// b_y -
																		// a_y *
																		// b_x

		newVector = toSpherical(x, y, z);

		return newVector;
	}

	/**
	 * perform the cross product operation on Cartesian coordinates
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static float[] cCrossVector(float[] vector1, float[] vector2) {

		float[] newVector = new float[3];

		// c_x = a_y * b_z - a_z * b_y
		newVector[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1]; 
		// c_y = a_z * b_x - a_x * b_z
		newVector[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2]; 
		// c_z = a_x * b_y - a_y * b_x
		newVector[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0]; 
		
		return newVector;
	}

	public static float[] cTripleProduct(float[] vector1, float[] vector2,
			float[] vector3) {

		float[] newVector = new float[3];

		newVector = cSubVector(
				cCrossVector(vector2, cCrossVector(vector1, vector3)),
				cCrossVector(vector3, cCrossVector(vector1, vector2)));

		return newVector;
	}

	public static float[] sScaleVector(float[] vector, float scalar, int i) {

		vector[i] *= scalar;

		return vector;
	}

	public static float[] cScaleVector(float[] vector, float scalar) {

		for (int i = 0; i < vector.length; i++) {

			vector[i] *= scalar;

		}

		return vector;
	}

	public static float[] cDivVector(float scalar, float[] vector) {

		for (int i = 0; i < vector.length; i++) {

			if (vector[i] == 0) {
				vector[i] = 0;
			} else {
				vector[i] = scalar / vector[i];
			}

		}

		return vector;
	}

	public static float cLength(float[] vector) {
		return (float) Math.sqrt(Math.pow(vector[0], 2)
				+ Math.pow(vector[1], 2) + Math.pow(vector[2], 2));
	}
	
	public static float cAngle(float[] vector1, float[] vector2) {
		
		return (float)Math.acos(cDotVector(vector1, vector2) / 
				(cLength(vector1) + cLength(vector2)));
	}

	public static float[] normalize(float[] vector) {

		float length = cLength(vector);
		return cScaleVector(vector, 1 / length);

	}

}
