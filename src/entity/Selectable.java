package entity;

/**
 * If an entity is selectable, it must implement this interface for it to be 
 * selected.
 * @author Christopher Dombroski
 *
 */
public interface Selectable {
	
	public int[] getCoord();
	
	public int getSelectionPriority();
	
	public boolean isSelected();
	
	public void checkSelected(int[] lowestCoord, int[] highestCoord);
}