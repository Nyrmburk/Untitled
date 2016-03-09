package game;

import graphics.ModelLoader;
import physics.Polygon;
import physics.Vec2;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class SimpleModelGenerator implements MaterialModelGenerator {

	@Override
	public ModelLoader generate(Polygon shape) {

		ModelLoader model = new ModelLoader();
		Vec2[] vertices = shape.getVertices();

		//front of model
		for (Vec2 point : vertices)
			model.vertices.put(point.getX(), point.getY(), 0.5f);

		//back of model
		for (Vec2 point : vertices)
			model.vertices.put(point.getX(), point.getY(), -0.5f);

		//front face
		int[] indices = new int[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			indices[i] = i;
		}
		model.addFace(indices);

		indices = new int[4];
		for (int i = 0; i < vertices.length; i++) {

			int nextIndex = (i + 1) % vertices.length;
			indices[0] = i;
			indices[1] = i + vertices.length;
			indices[2] = nextIndex + vertices.length;
			indices[3] = nextIndex;
			model.addFace(indices);
		}

		model.generateNormals();
		return model;
	}
}
