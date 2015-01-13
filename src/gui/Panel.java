package gui;

public class Panel extends Container {
	
	@Override
	public void draw() {
		
		renderChildren();
	}
	
	@Override
	protected void onPositionChange(int oldX, int oldY) {
		
		for (GUIElement child : children) {
			
			child.translate(x - oldX, y - oldY);
		}
	}
	
	@Override
	protected void onSizeChange(int oldWidth, int oldHeight) {
		
		invalidate();
		
//		int deltaWidth = width - oldWidth;
//		int deltaHeight = height - oldHeight;
//		
//		for (GUIElement child : children) {
//			
//			child.setSize(child.width + deltaWidth, child.height + deltaHeight);
//		}
	}
}
