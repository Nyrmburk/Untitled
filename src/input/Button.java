package input;

public abstract class Button extends Input {
	
	public Button(Object binding, String name) {
		super(binding, name, 1);
	}

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
		} else {
			
			onHold();
		}
	}
	
	public abstract void onPress();
	
	public abstract void onRelease();
	
	public abstract void onHold();
}