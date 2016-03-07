package activity;

import entity.Entity;
import gui.GUIBoxLayout;
import gui.GUIElement;
import gui.Panel;
import gui.View;
import main.Engine;

public class CreateActivity extends Activity {

	private Entity entity;

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIBoxLayout());
		setView(view);

		LoadingActivity loading = new LoadingActivity();
//		loading.loadElements(something);
		createActivity(loading);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(int delta) {
		// TODO Auto-generated method stub
//		float[] location = entity.getLocation();
//		location[0] += 0.02f;
//		entity.setLocation(location);
	}
}
