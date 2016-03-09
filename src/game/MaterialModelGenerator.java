package game;

import graphics.ModelLoader;
import physics.Polygon;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public interface MaterialModelGenerator {

	ModelLoader generate(Polygon shape);
}
