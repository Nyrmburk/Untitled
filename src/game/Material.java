package game;

import graphics.ModelLoader;
import graphics.Texture;
import matrix.Vec2;
import physics.Polygon;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class Material {

	private Texture texture;
	private MaterialModelGenerator modelGenerator;

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public MaterialModelGenerator getModelGenerator() {
		return modelGenerator;
	}

	public void setModelGenerator(MaterialModelGenerator modelGenerator) {
		this.modelGenerator = modelGenerator;
	}

	public ModelLoader getModel(Vec2[] vertices) {

		ModelLoader model = getModelGenerator().generate(vertices);
//		model.setTexture(getTexture());
		return model;
	}
}
