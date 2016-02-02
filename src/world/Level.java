package world;

import main.Player;
import physics.JBox2DPhysicsEngine;
import physics.PhysicsEngine;
import physics.PhysicsShape;

//The world has a physics instance?
//players list?
//
public class Level {
	
	PhysicsEngine physics;
	Player player;

	//players?
	//checkpoints?
	//physics?
	//
	
	public Level() {
		
		physics = new JBox2DPhysicsEngine(new float[]{0, -9.81f});
		physics.addShape(new PhysicsShape(PhysicsShape.Type.KINEMATIC, new float[]{0,0, 1,0, 1,1, 0,1}));
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}
}
