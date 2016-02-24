package gui;

import java.util.ArrayList;

public abstract class Container extends GUIElement {
	
	ArrayList<GUIElement> children = new ArrayList<GUIElement>();
	private GUILayoutManager layoutManager;
	
	public Container() {
		
		this(new GUIFlowLayout());
	}
	
	public Container(GUILayoutManager layout) {
		
		super();
		this.setlayout(layout);
	}
	
	public void addChild(GUIElement component) {
		
		addChild(component, null);
	}
	
	public void addChild(GUIElement element, Object constraint) {
		
		children.add(element);
		layoutManager.setConstraint(element, constraint);
		element.parent = this;
		invalidate();
	}
	
	public boolean containsChild(String childName) {
		
		for (GUIElement child : children) {
			
			if (child.getName().equals(childName)) {
				
				return true;
				
			} else if (child instanceof Container) {
				
				((Container) child).containsChild(childName);
			}
		}
		
		return false;
	}
	
	public GUIElement getChild(int index) {
		
		return children.get(index);
	}
	
	public GUIElement getChild(String childName) {
		
		for (GUIElement child : children) {
			
			if (child.getName().equals(childName)) {
				
				return child;
				
			} else if (child instanceof Container) {
				
				((Container) child).containsChild(childName);
			}
		}
		
		return null;
	}
	
	public ArrayList<GUIElement> getChildren() {
		
		return children;
	}
	
	public final void updateChildren() {
		
		for (GUIElement child : children) {
			
			child.update();
		}
	}
	
	public void renderChildren() {
		
		for (GUIElement child : children) {
			
			if (child.isVisible()) child.draw();
		}
	}
	
	public void setlayout(GUILayoutManager manager) {
		
		this.layoutManager = manager;
		manager.setParent(this);
		if (layoutManager != null) invalidate();
	}
	
	public GUILayoutManager getLayout() {
		
		return layoutManager;
	}
	
	public void pack() {

		int maxRight = 0;
		int maxBottom = 0;
		for (GUIElement child : children) {

			int right = child.getX() + child.getWidth() - getX();
			if (right > maxRight)
				maxRight = right;

			int bottom = child.getY() + child.getHeight() - getY();
			if (bottom > maxBottom)
				maxBottom = bottom;
		}

		if (maxRight == 0 && maxBottom == 0)
			System.out.println(this + ", no height or width");

//		this.setBounds(this.getX(), this.getY(), maxRight, maxBottom);
		this.width = maxRight;
		this.height = maxBottom;
		repaint = true;
//		this.setBounds(this.getX(), this.getY(), this.getWidthRatio(), this.getHeightRatio());
	}
	
//	public void update() {
//		
//		for (GUIElement child : children) {
//			
//			child.update();
//		}
//	}
	
	public void revalidate() {
		
		super.revalidate();
		
		this.layoutManager.layout();

		// summons demons from a dark deep forgotten temple of the underworld
		// TODO find and close portal to hell
//		pack();

		for (GUIElement child : children) {

			child.revalidate();
		}

		valid = true;
	}
}
