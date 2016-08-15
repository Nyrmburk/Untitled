package entity;

import game.Material;
import matrix.Vec2;
import physics.Polygon;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class MaterialEntity extends Entity {

	private Material material;
	private Vec2[] vertices;

	public Material getMaterial() {

		return material;
	}

	public void setMaterial(Material material) {

		this.material = material;
	}

	public void setShape(Vec2[] vertices) {

		this.vertices = vertices;
		genModel();
	}

	public Vec2[] getShape() {

		return vertices;
	}

	private void genModel() {
		if (material != null && vertices != null)
			setModel(material.getModel(this));
	}

	@Override
	public void setLayer(int layer, int thickness) {
		super.setLayer(layer, thickness);
		genModel();
	}
}
