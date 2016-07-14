package physics.JBox2D;

import matrix.Vec2;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.FixtureDef;
import physics.Body;
import physics.Polygon;
import physics.Sensor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static physics.JBox2D.Convert.convert;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public class JBox2DFixture implements Body, Sensor {

	private LinkedList<FixtureDef> fixtureDefs = new LinkedList<>();

	{
		fixtureDefs.add(new FixtureDef());
	}

	JBox2DFixture(boolean isSensor) {

		for (FixtureDef fixture : fixtureDefs)
			fixture.setSensor(isSensor);
	}

	LinkedList<FixtureDef> getFixtureDefs() {

		return fixtureDefs;
	}

	@Override
	public float getDensity() {

		return fixtureDefs.getFirst().getDensity();
	}

	@Override
	public void setDensity(float density) {

		for (FixtureDef fixture : fixtureDefs)
			fixture.setDensity(density);
	}

	@Override
	public float getRestitution() {

		return fixtureDefs.getFirst().getRestitution();
	}

	@Override
	public void setRestitution(float restitution) {

		for (FixtureDef fixture : fixtureDefs)
			fixture.setRestitution(restitution);
	}

	@Override
	public float getFriction() {

		return fixtureDefs.getFirst().getFriction();
	}

	@Override
	public void setFriction(float friction) {

		for (FixtureDef fixture : fixtureDefs)
			fixture.setFriction(friction);
	}

	@Override
	public void setShape(ShapeType type, Vec2[] vertices) {

		Shape shape = null;

		switch (type) {

			case COMPLEX_POLYGON:

				List<Vec2[]> decomposed = Polygon.approximateDecomposition(vertices);

				Iterator<Vec2[]> it = decomposed.iterator();
				fixtureDefs.getFirst().setShape(polygonShape(vertices));

				while (it.hasNext()) {

					Vec2[] poly = it.next();

					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.setFriction(getFriction());
					fixtureDef.setRestitution(getRestitution());
					fixtureDef.setDensity(getDensity());
					fixtureDef.setSensor(fixtureDefs.getFirst().isSensor());
					fixtureDef.setShape(polygonShape(poly));
					fixtureDefs.add(fixtureDef);
				}
				return;
			case CIRCLE:
				
				shape = new CircleShape();
				break;
			case EDGE:

				shape = new EdgeShape();
				break;
			case POLYGON:

				shape = polygonShape(vertices);
				break;
			case CHAIN:

				shape = new ChainShape();
				break;
		}

		fixtureDefs.getFirst().setShape(shape);
	}

	private Shape polygonShape(Vec2[] poly) {

		Shape shape = new PolygonShape();
		((PolygonShape) shape).set(convert(poly), poly.length);

		return shape;
	}
}
