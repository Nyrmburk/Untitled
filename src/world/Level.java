package world;

import main.Player;
import physics.JBox2DPhysicsEngine;
import physics.PhysicsEngine;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;

//The world has a physicsEngine instance?
//players list?
//
public class Level {
	
	public PhysicsEngine physicsEngine;
	public PhysicsObject test;
	Player player;

	//players?
	//checkpoints?
	//physicsEngine?
	//
	
	public Level() {
		
		physicsEngine = new JBox2DPhysicsEngine(new float[]{0, -9.81f});
		PhysicsObjectDef testing = physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, new float[]{0,0, 1,0, 1,1, 0,1});
		testing.setDensity(1);
		testing.setFriction(0.3f);
		testing.setPosition(1, 1);
		test = physicsEngine.createPhysicsObject(testing);
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}
}
