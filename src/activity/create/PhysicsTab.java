package activity.create;

import gui.Button;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;

/**
 * Created by Nyrmburk on 8/8/2016.
 */
public class PhysicsTab extends Tab {

	private Button move = new Button();

	public PhysicsTab() {
		super("Physics");
		setlayout(new GUIBoxLayout());

		move.setText("Move");
		addChild(move, 0);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void pointerEvent(PointerListener.PointerEvent event) {

	}
}
