package activity;

import entity.MaterialEntity;
import game.Material;
import game.Player;
import game.SimpleModelGenerator;
import graphics.ModelLoader;
import gui.*;
import main.Engine;
import main.ResourceManager;
import main.Vec2;
import physics.*;
import game.Level;
import physics.Polygon;

import java.awt.Color;
import java.io.IOException;

public class LoadingActivity extends Activity {

	private float percentComplete = 0;
	String loadingMessage = "Loading... %d%%";
	TextBox loadingText;

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.WHITE);

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
//		if (percentComplete > 100) {

			percentComplete = 100;
			Activity.stopCurrentActivity();

			Engine.level = new Level();
			try {
				Engine.level.load(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Material material = new Material();
			material.setModelGenerator(new SimpleModelGenerator());
			Vec2[] verts = {
					new Vec2(0, 0),
					new Vec2(1, 0),
					new Vec2(1, 1),
					new Vec2(0, 1)};
			PhysicsObjectDef shape = Engine.level.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, new Polygon(verts));
			MaterialEntity entity = new MaterialEntity();
			entity.setPhysicsObject(shape);
			entity.setLevel(Engine.level);
			entity.setMaterial(material);
			entity.setShape(new Polygon(verts));

			Player player = new Player();
			player.setLevel(Engine.level);
			Vec2[] playerVertices = {
					new Vec2(-0.35f, 0),
					new Vec2(0.35f, 0),
					new Vec2(0.35f, 1.8f),
					new Vec2(-0.35f, 1.8f)};
			Polygon meh = new Polygon(playerVertices);
			PhysicsObjectDef objectDef = Engine.level.physicsEngine.getPhysicsObjectDef(PhysicsObject.Type.DYNAMIC, meh);
			player.setPhysicsObject(objectDef);
			player.setLocation(new float[]{-3, 0});

			//lambda expressions make this pretty
			ResourceManager.getResource(
					"Player.obj",
					loadedResource -> player.setModel((ModelLoader) loadedResource),
					ModelLoader.class);
//		}
		loadingText.setText(String.format(loadingMessage, (int) percentComplete));
	}
}
