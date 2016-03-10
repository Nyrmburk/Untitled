package game;

import graphics.ModelLoader;
import graphics.Texture;
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

	public ModelLoader getModel(Polygon polygon) {

		ModelLoader model = getModelGenerator().generate(polygon);
//		model.setTexture(getTexture());
		return model;
	}
}
