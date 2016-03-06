package gui;

public class ProgressBar extends GUIElement {
	
	private int currentValue = 0;
	private int resolution = 100;

	public void setProgress(int progress) {
		
		this.currentValue = progress;
	}
	
	public void setResolution(int resolution) {
		
		this.resolution = resolution;
	}

	@Override
	protected void layout() {

	}

	public int getCurrentValue() {

		return currentValue;
	}

	public int getResolution() {

		return resolution;
	}

	@Override
	protected ContextBox createBox() {
		return null;
	}
}
