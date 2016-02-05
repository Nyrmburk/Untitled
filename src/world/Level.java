package world;

import main.Player;
import physics.JBox2DPhysicsEngine;
import physics.PhysicsEngine;
import physics.PhysicsShape;

//The world has a physicsEngine instance?
//players list?
//
public class Level {
	
	public PhysicsEngine physicsEngine;
	Player player;

	//players?
	//checkpoints?
	//physicsEngine?
	//
	
	public Level() {
		
		physicsEngine = new JBox2DPhysicsEngine(new float[]{0, -9.81f});
		physicsEngine.addShape(new PhysicsShape(PhysicsShape.Type.KINEMATIC, new float[]{0,0, 1,0, 1,1, 0,1}));
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}
}
