package gui;

import java.awt.image.BufferedImage;

import graphics.UIRenderEngine;

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
	public BufferedImage render(UIRenderEngine renderEngine) {
		
		return renderEngine.renderProgressBar(this);
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
	public int getCurrentValue() {
		
		return currentValue;
	}
	
	public int getResolution() {
		
		return resolution;
	}

	@Override
	protected void onPositionChange(int oldX, int oldY) {
	}

	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {
	}
}
