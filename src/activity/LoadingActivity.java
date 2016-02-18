package activity;

import entity.Entity;
import graphics.ModelLoader;
import gui.*;
import gui.Panel;
import main.AssetManager;
import main.Engine;
import physics.PhysicsObject;
import physics.PhysicsObjectDef;
import world.Level;

import java.awt.*;

public class LoadingActivity extends Activity {

	private float percentComplete = 0;
	String loadingMessage = "Loading... %d%%";
	TextBox loadingText;

	@Override
	protected void onCreate() {

		Panel view = new Panel();
		view.setWidthLayout(GUIElement.layout.FILL_PARENT);
		view.setHeightLayout(GUIElement.layout.FILL_PARENT);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.GRAY);

		loadingText = new TextBox();
		loadingText.setText(String.format(loadingMessage, (int) percentComplete));
		view.addChild(loadingText);

		setView(view);
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
		percentComplete += ((float) delta) / 5;
		if (percentComplete > 100) {

			percentComplete = 100;
			Activity.stopCurrentActivity();

			Engine.level = new Level();

			ModelLoader model = AssetManager.getModel("monkey.obj");
			PhysicsObjectDef shape = Engine.level.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, new float[]{0, 0, 0, 1, 1, 1, 1, 0});
			Entity entity = new Entity("monkeybox");
			entity.setModel(model);
			entity.setPhysicsObject(shape);
		}
		loadingText.setText(String.format(loadingMessage, (int) percentComplete));
	}
}
