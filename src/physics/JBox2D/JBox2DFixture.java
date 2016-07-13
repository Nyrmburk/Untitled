package physics.JBox2D;

import org.jbox2d.dynamics.FixtureDef;
import physics.Body;
import physics.Sensor;
import physics.Shape2;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public class JBox2DFixture implements Body, Sensor {

	private FixtureDef fixtureDef = new FixtureDef();
	private Shape2 shape;

	JBox2DFixture (boolean isSensor) {

		fixtureDef.setSensor(isSensor);
	}

	FixtureDef getFixtureDef() {

		return fixtureDef;
	}

	@Override
	public float getDensity() {

		return fixtureDef.getDensity();
	}

	@Override
	public void setDensity(float density) {

		fixtureDef.setDensity(density);
	}

	@Override
	public float getRestitution() {

		return fixtureDef.getRestitution();
	}

	@Override
	public void setRestitution(float restitution) {

		fixtureDef.setRestitution(restitution);
	}

	@Override
	public float getFriction() {

		return fixtureDef.getFriction();
	}

	@Override
	public void setFriction(float friction) {

		fixtureDef.setFriction(friction);
	}

	@Override
	public Shape2 getShape() {

		return shape;
	}

	@Override
	public void setShape(Shape2 shape) {

		this.shape = shape;
		fixtureDef.setShape(((JBox2DShape) shape).getShape());
	}
}
