package physics;

/**
 * Created by Nyrmburk on 7/12/2016.
 */
public interface Body {

	float getDensity();

	void setDensity(float density);

	float getRestitution();

	void setRestitution(float restitution);

	float getFriction();

	void setFriction(float friction);

	Shape2 getShape();

	void setShape(Shape2 shape);

	//TODO filters
}
