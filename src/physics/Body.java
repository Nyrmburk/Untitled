package physics;

import matrix.Vec2;

/**
 * Created by Nyrmburk on 7/12/2016.
 */
public interface Body {

	enum ShapeType {

		CIRCLE,
		EDGE,
		POLYGON,
		COMPLEX_POLYGON,
		CHAIN,
	}

	float getDensity();

	void setDensity(float density);

	float getRestitution();

	void setRestitution(float restitution);

	float getFriction();

	void setFriction(float friction);

	void setShape(ShapeType type, Vec2[] vertices);

	//TODO filters
}
