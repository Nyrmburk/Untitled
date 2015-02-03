package gui;

import java.util.ArrayList;

public abstract class Container extends GUIElement {
	
	ArrayList<GUIElement> children = new ArrayList<GUIElement>();
	GUILayoutManager layoutManager = new GUIFlowLayout(this);
	
	public Container() {
		
		super();
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
	
	public void updateChildren() {
		
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
		if (layoutManager != null) invalidate();
	}
	
	public GUILayoutManager getLayout() {
		
		return layoutManager;
	}
	
	public void pack() {
		
		//TODO make it so
	}
	
	public void revalidate() {
		
		for (GUIElement child : children) {
			
			child.revalidate();
		}
		
		this.layoutManager.layout();
		
		valid = true;
	}
}
