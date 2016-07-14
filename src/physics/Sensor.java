package physics;

import matrix.Vec2;

public interface Sensor {

	void setShape(Body.ShapeType type, Vec2[] vertices);
}
