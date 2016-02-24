package entity;

import graphics.ModelLoader;

public class Camera {

	private float aspectRatio;
//	private float fieldOfView = 90;
//	private float[] quaternion = new float[16];
	
	public float getAspectRatio() {
		
		return aspectRatio;
	}
	
	public float[] getLocation() {
		
		return new float[3];
	}
	
	public float[] getRotation() {
		
		return new float[4];
	}
}
