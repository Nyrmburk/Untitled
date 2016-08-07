package gui;

import java.awt.*;

public abstract class GUILayoutManager {
	
	Container parent;
	
	protected void setParent(gui.Container parent) {
		
		this.parent = parent;
	}

	public Container getParent() {

		return parent;
	}

	public abstract void layout();
	
	public abstract void setConstraint(GUIElement element, Object constraint);

	public abstract Dimension getPreferredSize();
}
