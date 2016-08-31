package game;

import graphics.Camera;
import entity.Entity;
import entity.MaterialEntity;
import graphics.RenderContext;
import main.Resource;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec2;
import matrix.Vec3;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.*;
import physics.JBox2D;
import widget.Widget;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Level extends Resource {
	
	public World physicsEngine;
	private RenderContext renderContext;
	private List<Entity> entities = new ArrayList<>();
	public List<PlayerController> players = new ArrayList<>();

	private int layerCount;
	
	public Level(Camera camera) {
		
		physicsEngine = new World(JBox2D.convert(new Vec2(0, -9.81f)));
		renderContext = new RenderContext(camera);
		layerCount = 3;//max 31
	}

	public void update(float delta) {

		physicsEngine.step(delta, 8, 3);

		for (Entity entity : entities)
			entity.update(delta);
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		this.entities.remove(entity);
	}

	public RenderContext getRenderContext() {
		return renderContext;
	}

	public int getLayerCount() {
		return layerCount;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void save(Path path) {

	}

	@Override
	public void load(Path path) throws IOException {

		Material material = new Material();
		material.setModelGenerator(new SimpleModelGenerator());
		MaterialEntity floor = new MaterialEntity();
		floor.setLevel(this);
		Vec2[] floorVertices = {
				new Vec2(-100, 1),
				new Vec2(-100, -1),
				new Vec2(100, -1),
				new Vec2(100, 1)};
		floor.setMaterial(material);
		floor.setShape(floorVertices);

		BodyDef objectDef = new BodyDef();
		objectDef.setType(BodyType.KINEMATIC);
		Body body = floor.setPhysicsObject(objectDef);
		for (Shape shape : JBox2D.shapeFromPolygon(floorVertices))
			body.createFixture(shape, 1);
		Mat4 transform = Mat4.identity();
		Transform.setPosition(transform, new Vec3(0, -5, 0));
		floor.setTransform(transform);
		floor.setLayer(0, getLayerCount());
	}
}
