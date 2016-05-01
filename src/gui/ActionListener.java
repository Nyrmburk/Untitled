package gui;

/**
 * Created by Nyrmburk on 4/30/2016.
 */
public abstract class ActionListener {

	private GUIElement parent;

	public abstract void update(int delta);

	public abstract void actionPerformed();

	protected GUIElement getParent() {

		return parent;
	}

	protected void setParent(GUIElement parent) {

		this.parent = parent;
	}
}
