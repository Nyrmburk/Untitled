package activity;

import entity.Entity2;
import graphics.ModelLoader;
import gui.GUIBoxLayout;
import gui.GUIElement;
import gui.Panel;
import main.AssetManager;
import main.Engine;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;
import world.Level;

public class CreateActivity extends Activity {

	private Entity2 entity;

	@Override
	protected void onCreate() {

		Panel view = new Panel();
		view.setWidthLayout(GUIElement.layout.FILL_PARENT);
		view.setHeightLayout(GUIElement.layout.FILL_PARENT);
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
