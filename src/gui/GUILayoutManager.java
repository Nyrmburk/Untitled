package gui;

public interface GUILayoutManager {
	
	/**
	 * Lay out a container and all it's elements.
	 * 
	 * @param parent
	 *            The container that owns this <code>GUILayoutManager</code>.
	 */
	public void layout(Container parent);
	
	public void setConstraint(GUIElement element, Object Constraint);
	
}
