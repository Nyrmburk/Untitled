package physics.JBox2D;

import physics.Shape2;

/**
 * Created by Nyrmburk on 7/13/2016.
 */
public interface JBox2DShape extends Shape2 {

	org.jbox2d.collision.shapes.Shape getShape();
}
