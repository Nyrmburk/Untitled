package entity;

import graphics.ModelLoader;
import input.InputContext;

public class Mob2 extends Actor {

	public Mob2(String name, float[] location, ModelLoader model) {
		super(name, location, model);
	}

	public void move(float angle) {

	}
	
	public void jump() {
		
	}
	
	public void crouch() {
		
	}
}

class MoveInputContext extends InputContext {
	
	public MoveInputContext() {
		
//		this.register(new Input2(null, "x", 1));
//		this.register(new Input2(null, "y", 1));
	}

	@Override
	public boolean isValid() {
		
		return true;
	} 

	@Override
	public void update(int delta) {
		
	}
}

class JumpInputContext extends InputContext {
	
	public JumpInputContext() {
		
//		this.register(new Input2(null, "jump", 1));
	}

	@Override
	public boolean isValid() {
		//if on ground return true
		return false;
	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub
		
	}
}