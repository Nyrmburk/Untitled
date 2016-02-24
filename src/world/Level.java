package world;

import entity.Entity;
import graphics.RenderContext;
import main.AssetManager;
import main.Player;
import physics.JBox2DPhysicsEngine;
import physics.PhysicsEngine;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;

import java.util.ArrayList;
import java.util.List;

//The world has a physicsEngine instance?
//players list?
//
public class Level {
	
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
	
	public void save() {
		
	}
	
	public void load() {

		Entity floor = new Entity(this);
		floor.setModel(AssetManager.getModel("floor.obj"));
		float[] floorVertices = {-100, 1, -100, -1, 100, -1, 100, 1};
//		float[] floorVertices = {-100, 1, 100, 1, 100, -1, -100, -1};
		PhysicsObjectDef objectDef = this.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.KINEMATIC, floorVertices);
		floor.setPhysicsObject(objectDef);
		floor.setLocation(new float[]{0, -5});

		Player player = new Player(this);
		player.setModel(AssetManager.getModel("Player.obj"));
		float[] playerVertices = {-0.5f, 0, 0.5f, 0, 0.5f, 1.8f, -0.5f, 1.8f};
//		float[] playerVertices = {-0.5f, 0, -0.5f, 1.8f, 0.5f, 1.8f, 0.5f, 0};
		objectDef = this.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, playerVertices);
		player.setPhysicsObject(objectDef);
	}

	public RenderContext getRenderContext() {

		return renderContext;
	}
}
