package main;

/**
 * Hold some variables relevant to the context.
 * 
 * @author Christopher Dombroski
 *
 */
public class Logic {

	public boolean ctrlDown;
	public boolean shiftDown;
	public boolean altDown;

	public boolean itemSelected;
	public boolean entitySelected;
	
	public boolean drawMode;

	public Logic() {

		clear();
	}

	public void clear() {

		ctrlDown = false;
		shiftDown = false;
		altDown = false;

		itemSelected = false;
		entitySelected = false;
	}
}
