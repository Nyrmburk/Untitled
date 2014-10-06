package main;

public class GeometryHelper {
	
	public static float isLeft(float[] point1, float[] point2, float[] point3) {
		
		return (point1[0] - point3[0]) * 
				(point2[1] - point3[1]) - 
				(point2[0] - point3[0]) * 
				(point1[1] - point3[1]);
	}
	
	public static float triangleHeight(float[] location, float[] point1, float[] point2, float[] point3) {
		
		float determinant = (point2[1] - point3[1]) * (point1[0] - point3[0]) + 
				(point3[0] - point2[0]) * (point1[1] - point3[1]);
		
		float l1 = ((point2[1] - point3[1]) * (location[0] - point3[0]) + 
				(point3[0] - point2[0]) * (location[1] - point3[1])) / determinant;
		float l2 = ((point3[1] - point1[1]) * (location[0] - point3[0]) + 
				(point1[0] - point3[0]) * (location[1] - point3[1])) / determinant;
		float l3 = 1f - l1 - l2;
		
		return l1 * point1[2] + l2 * point2[2] + l3 * point3[2];
	}
}
