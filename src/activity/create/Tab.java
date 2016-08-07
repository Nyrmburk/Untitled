package activity.create;

import gui.Panel;

/**
 * Created by Nyrmburk on 8/6/2016.
 */
public abstract class Tab extends Panel {

	private String handle;

	public Tab(String handle) {

		this.handle = handle;
	}

	public String getHandle() {

		return handle;
	}

	public abstract void update(float delta);
}
