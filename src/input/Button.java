package input;

public abstract class Button extends Input {
	
	public Button(String name) {
		super(name);
	}

	@Override
	public void setValue(float value) {
		
		if (value > 0) {
			
			value = getRange();
		} else {
			
			value = -getRange();
		}
		
		super.setValue(value);

		if (getDelta() < getRange()) {

			onRelease();
		} else if (getDelta() >= getRange()) {

			onPress();
		}
	}

	@Override
	public void onUpdate(int delta) {

//		System.out.println(this.getName() + " = " + getDelta());

		if (getDelta() >= getRange()) {
			onHold(delta);
		}
	}
	
	public abstract void onPress();
	
	public abstract void onRelease();
	
	public abstract void onHold(int delta);
}
