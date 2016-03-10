package game;

import entity.Entity;
import entity.MaterialEntity;
import graphics.ModelLoader;
import graphics.RenderContext;
import main.AssetManager;
import main.Resource;
import main.ResourceManager;
import physics.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//The game has a physicsEngine instance?
//players list?
//
public class Level extends Resource {
	
	public PhysicsEngine physicsEngine;
	private RenderContext renderContext = new RenderContext();
	private List<Entity> entities = new ArrayList<Entity>();
	Player player;

	//players?
	//checkpoints?
	//physicsEngine?
	//
	
	public Level() {
		
		physicsEngine = new JBox2DPhysicsEngine(new float[]{0, -9.81f});
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
	protected void onRegister() {

	}

	@Override
	protected void onRelease() {

	}

	@Override
	public void save(Path path) {

	}

	@Override
	public void load(Path Path) throws IOException {

		Material material = new Material();
		material.setModelGenerator(new SimpleModelGenerator());
		MaterialEntity floor = new MaterialEntity();
		floor.setLevel(this);
//		floor.setModel(AssetManager.getModel("floor.obj"));
		Vec2[] floorVertices = {
				new Vec2(-100, 1),
				new Vec2(-100, -1),
				new Vec2(100, -1),
				new Vec2(100, 1)};
		Polygon shape = new Polygon(floorVertices);
		floor.setMaterial(material);
		floor.setShape(shape);
		PhysicsObjectDef objectDef = this.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.KINEMATIC, shape);
		objectDef.setDensity(1);
		floor.setPhysicsObject(objectDef);
		floor.setLocation(new float[]{0, -5});

		Level level = this;

		ResourceManager.getResource("Player.obj", new ResourceManager.AsyncLoad() {
			@Override
			public void onLoad(Resource loadedResource) {

				Player player = new Player();
				player.setLevel(level);
				player.setModel((ModelLoader) loadedResource);
				Vec2[] playerVertices = {
						new Vec2(-0.35f, 0),
						new Vec2(0.35f, 0),
						new Vec2(0.35f, 1.8f),
						new Vec2(-0.35f, 1.8f)};
				Polygon shape = new Polygon(playerVertices);
				PhysicsObjectDef objectDef = level.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, shape);
				player.setPhysicsObject(objectDef);
				player.setLocation(new float[]{-3, 0});
			}
		}, ModelLoader.class);

//		Player player = new Player();
//		player.setLevel(this);
//		player.setModel(AssetManager.getModel("Player.obj"));
//		Vec2[] playerVertices = {
//				new Vec2(-0.35f, 0),
//				new Vec2(0.35f, 0),
//				new Vec2(0.35f, 1.8f),
//				new Vec2(-0.35f, 1.8f)};
//		shape = new Polygon(playerVertices);
//		objectDef = this.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, shape);
//		player.setPhysicsObject(objectDef);
//		player.setLocation(new float[]{-3, 0});
	}
}
