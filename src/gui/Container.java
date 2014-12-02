package gui;

import java.util.ArrayList;

public abstract class Container extends GUIElement {
	
	ArrayList<GUIElement> children = new ArrayList<GUIElement>();
	GUILayoutManager layoutManager = new GUIFlowLayout();
	
	public Container() {
		
		super();
	}
	
	public void addChild(String name, GUIElement component) {
		
		addChild(name, component, null);
	}
	
	public void addChild(String name, GUIElement element, Object constraint) {
		
		children.add(element);
		layoutManager.setConstraint(element, constraint);
		layoutManager.layout(this);
	}
	
	public GUIElement getChild(int index) {
		
		return children.get(index);
	}
	
	public void renderChildren() {
		
		for (GUIElement child : children) {
			
			if (child.isVisible()) child.draw();
		}
	}
	
	public void setlayout(GUILayoutManager manager) {
		
		this.layoutManager = manager;
		if (layoutManager != null) layoutManager.layout(this);
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
		
		valid = true;
	}
}
