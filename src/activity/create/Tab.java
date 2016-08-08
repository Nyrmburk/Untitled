package activity.create;

import gui.Panel;
import gui.View;
import gui.event.PointerListener;

/**
 * Created by Nyrmburk on 8/6/2016.
 */
public abstract class Tab extends Panel {

	private String handle;
	protected View view;

	public Tab(String handle) {

		this.handle = handle;
	}

	public String getHandle() {

		return handle;
	}

	public void setView(View view) {
		this.view = view;
	}

	public abstract void update(float delta);

	public abstract void pointerEvent(PointerListener.PointerEvent event);
}
