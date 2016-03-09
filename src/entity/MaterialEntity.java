package entity;

import game.Material;
import physics.Polygon;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class MaterialEntity extends Entity {

	private Material material;

	public Material getMaterial() {

		return material;
	}

	public void setMaterial(Material material) {

		this.material = material;
	}

	public void setShape(Polygon polygon) {

		setModel(material.getModel(polygon));
	}
}
