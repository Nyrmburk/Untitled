package gui;

import main.Engine;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<GUIElement> getChildren() {
		
		return children;
	}

	public void clearChildren() {

		children.clear();
	}

	@Override
	protected void validate() {

		if (isValid())
			return;

		super.validate();

		for (GUIElement child : getChildren()) {

			child.validate();
		}
	}

	@Override
	protected void layout() {

		switch (widthLayout) {
		case DISCRETE:
			break;
		case FILL_PARENT:

			if (parent != null) {
				setBounds(parent.getX(), getY(), parent.getWidth(), getHeight());
			} else {
				setBounds(0, getY(), Engine.renderEngine.getWidth(), getHeight());
			}
			break;
		case WRAP_CONTENT:
//			pack();
			break;
		}

		switch (heightLayout) {
		case DISCRETE:
			break;
		case FILL_PARENT:

			if (parent != null) {
				setBounds(getX(), parent.getY(), getWidth(), parent.getHeight());
			} else {
				setBounds(getX(), 0, getWidth(), Engine.renderEngine.getHeight());
			}
			break;
		case WRAP_CONTENT:
			break;
		}

		for (GUIElement child : children)
			child.layout();
		layoutManager.layout();
	}
	
	public void setlayout(GUILayoutManager manager) {
		
		this.layoutManager = manager;
		manager.setParent(this);
		if (layoutManager != null) invalidate();
	}
	
	public GUILayoutManager getLayout() {
		
		return layoutManager;
	}
}
