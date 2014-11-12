package entity;

/**
 * If an entity is selectable, it must implement this interface for it to be
 * selected. In order for the entity to be selectable, the extended entity must
 * include this line:
 * 
 * <pre>
 * <code>super.addSelectable(this)</code>
 * </pre>
 * 
 * It is very similar to EventListeners for awt.
 * 
 * @author Christopher Dombroski
 */
public interface Selectable {
	
	/**
	 * @return Whether or not the entity is selected.
	 */
	public boolean isSelected();
	
	/**
	 * Called when the entity is within selection bounds.
	 */
	public void selected(boolean selected);
}