package tool;

import matrix.Mat3;
import matrix.Mat4;
import matrix.Vec2;

/**
 * Created by Nyrmburk on 10/2/2016.
 */
public class Transform {

	private Selection selection;

	public Transform(Selection selection) {

		setSelection(selection);
	}

	public Transform setSelection(Selection selection) {
		this.selection = selection;
		return this;
	}

	public Transform transform(Mat4 transform) {

		return this;
	}

	public Transform transform(Mat3 transform) {

		return this;
	}

	public Transform translate(Vec2 position) {

		return this;
	}

	public Transform scale(Vec2 scale) {

		return this;
	}

	public Transform scale(float scale) {

		return this;
	}

	public Transform rotate(Vec2 point, float rotation) {

		return this;
	}

	public Transform rotate(float rotation) {

		return this;
	}
}
