package gui;

import java.awt.*;
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

		setSize(getPreferredSize());

		for (GUIElement child : children)
			child.layout();

		layoutManager.layout();
	}

	@Override
	protected void redraw(List<GUIElement> toDraw) {

		for (GUIElement child : children)
				child.redraw(toDraw);

		super.redraw(toDraw);
	}

	public void setlayout(GUILayoutManager manager) {

		this.layoutManager = manager;
		manager.setParent(this);
		if (layoutManager != null) invalidate();
	}

	public GUILayoutManager getLayout() {

		return layoutManager;
	}

	@Override
	public Dimension getPreferredSize() {

		Dimension size = prefSize;
		if (size == null)
			size = layoutManager.getPreferredSize();
		return size;
	}
}
