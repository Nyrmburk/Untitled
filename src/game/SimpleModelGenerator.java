package game;

import graphics.ModelLoader;
import physics.Polygon;
import matrix.Vec2;

import java.util.List;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class SimpleModelGenerator implements MaterialModelGenerator {

	@Override
	public ModelLoader generate(Vec2[] vertices) {

		ModelLoader model = new ModelLoader();

		//front of model
		for (Vec2 point : vertices)
			model.vertices.put(point.x, point.y, 0.5f);

		//back of model
		for (Vec2 point : vertices)
			model.vertices.put(point.x, point.y, -0.5f);

		//front face
		int[] indices;
		int j = vertices.length * 2;
		List<Vec2[]> polys = Polygon.approximateDecomposition(vertices);
		for (Vec2[] poly : polys) {
			for (Vec2 vec : poly) {
				model.vertices.put(vec.x, vec.y, 0.5f);
			}
			indices = new int[poly.length];
			for (int i = 0; i < poly.length; i++)
				indices[i] = i + j;
			model.addFace(indices);
			j += poly.length;
		}

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
