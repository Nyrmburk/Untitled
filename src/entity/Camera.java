package entity;

import graphics.ModelLoader;

public class Camera extends Entity {

	private float aspectRatio;
//	private float fieldOfView = 90;
//	private float[] quaternion = new float[16];
	
	public Camera(String name, float[] location, ModelLoader model) {
		super(name);
	}
	
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
