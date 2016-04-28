package activity;

import entity.Entity;
import gui.GUIBoxLayout;
import gui.GUIElement;
import gui.Panel;
import gui.View;
import main.Engine;

import java.awt.*;

public class CreateActivity extends Activity {

	private Entity entity;

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIBoxLayout());

		Panel panel = new Panel();
		panel.setSize(256, 1080);
		panel.setBackgroundColor(new Color(31, 31, 31, 220));
		view.addChild(panel, 0);

		setView(view);

		LoadingActivity loading = new LoadingActivity();
		createActivity(loading);
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onResume() {
	}

	@Override
	protected void onPause() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	protected void onDestroy() {
	}

	@Override
	public void onUpdate(int delta) {
	}
}
