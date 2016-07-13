package game;

import entity.Camera;
import entity.Entity;
import entity.MaterialEntity;
import graphics.RenderContext;
import main.Resource;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec2;
import matrix.Vec3;
import physics.*;
import physics.JBox2D.JBox2DPhysicsEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//The game has a physicsEngine instance?
//players list?
//
public class Level extends Resource {
	
	public PhysicsEngine physicsEngine;
	private Camera camera;
	private RenderContext renderContext;
	private List<Entity> entities = new ArrayList<Entity>();
	public List<PlayerController> players = new ArrayList<>();

	//players?
	//checkpoints?
	//physicsEngine?
	//
	
	public Level(Camera camera) {
		
		physicsEngine = new JBox2DPhysicsEngine(new float[]{0, -9.81f});
		this.setCamera(camera);
		renderContext = new RenderContext(camera);
	}

	public void update(int delta) {

		physicsEngine.update(delta);

		for (Entity entity : entities) {

			entity.update(delta);
		}
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
		PhysicsObjectDef objectDef = this.physicsEngine.newPhysicsObjectDef(PhysicsObject.Type.KINEMATIC);
		PhysicsObject object = floor.setPhysicsObject(objectDef);
		Body body = this.physicsEngine.newBody();
		Shape2 shape = this.physicsEngine.newShape2(Shape2.ShapeType.COMPLEX_POLYGON, floorVertices);
		body.setShape(shape);
		body.setDensity(1);
		object.createBody(body);
		Mat4 transform = Mat4.identity();
		Transform.setPosition(transform, new Vec3(0, -5, 0));
		floor.setTransform(transform);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
