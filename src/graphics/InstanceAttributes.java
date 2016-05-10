package graphics;

import main.Transform;

//Beef this up.
public class InstanceAttributes {

	public Transform transform;

	public InstanceAttributes(float[] location, float[] rotation) {

		this.location = location;
		this.rotation = rotation;
	}
}
