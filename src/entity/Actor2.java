package entity;

import graphics.ModelLoader;
import input.InputInterface;

public class Actor2 extends Entity {

	private InputInterface input;
	
	public Actor2(String name, float[] location, ModelLoader model) {
		super(name, location, model);
	}
	
	public void update(int delta) {
		
		input.update(delta);
	}
	
	public void setInput(InputInterface input) {
		
		this.input = input;
	}
}
