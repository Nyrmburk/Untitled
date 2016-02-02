package gui;

public abstract class GUILayoutManager {
	
	Container parent;
	
	protected void setParent(Container parent) {
		
		this.parent = parent;
	}
	
	/**
	 * Lay out a container and all it's elements.
	 * 
	 * @param parent
	 *            The container that owns this <code>GUILayoutManager</code>.
	 */
	public abstract void layout();
	
	public abstract void setConstraint(GUIElement element, Object constraint);
	
}
