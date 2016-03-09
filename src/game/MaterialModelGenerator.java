package game;

import graphics.ModelLoader;
import graphics.TextureInterface;

import java.awt.*;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public interface MaterialModelGenerator {

	ModelLoader generate(Polygon shape, Material material);
}
