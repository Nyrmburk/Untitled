package activity.create;

import draftform.Draftform;
import gui.Button;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;

import java.awt.*;


/**
 * Created by Nyrmburk on 8/6/2016.
 */
public class DrawTab extends Tab {

	private final Button btnSelect = new Button();
	private final Button btnPen = new Button();
	private final Button btnClear = new Button();
	private final Button btnCommit = new Button();

	private Draftform draftform = new Draftform();

	public DrawTab() {
		super("Draw");

		setBackgroundColor(Color.LIGHT_GRAY);
		setlayout(new GUIBoxLayout());

		btnSelect.setText("Select");
		btnSelect.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				select();
			}
		});
		addChild(btnSelect, 0);

		btnPen.setText("Pen");
		btnSelect.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				pen();
			}
		});
		addChild(btnPen, 0);

		btnClear.setText("Clear");
		btnSelect.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				clear();
			}
		});
		addChild(btnClear, 0);

		btnCommit.setText("Commit");
		btnSelect.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				commit();
			}
		});
		addChild(btnCommit, 0);
	}

	private void select() {

	}

	private void pen() {

	}

	private void clear() {

	}

	private void commit() {

	}

	@Override
	public void update(float delta) {

	}
}
