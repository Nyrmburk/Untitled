package game;

import graphics.ModelLoader;
import matrix.Vec2;
import physics.Polygon;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public interface MaterialModelGenerator {

	ModelLoader generate(Vec2[] vertices);
}
