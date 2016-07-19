package activity;

import entity.Camera;
import entity.Entity;
import entity.MaterialEntity;
import game.*;
import graphics.ModelLoader;
import gui.*;
import main.Engine;
import main.ResourceManager;
import matrix.*;
import physics.*;
import physics.Polygon;

import java.awt.*;
import java.io.IOException;

public class LoadingActivity extends Activity {

	private float percentComplete = 0;
	private float waitTime = 0.5f;
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
	public void onUpdate(float delta) {
		// TODO Auto-generated method stub
		percentComplete += 100 * delta / waitTime;
		if (percentComplete > 100) {

			percentComplete = 100;
			Activity.stopCurrentActivity();

			Rectangle viewport = Engine.renderEngine.getViewport();
			float aspect = (float) (viewport.getWidth() / viewport.getHeight());
			Mat4 projection = Projection.perspective(60, aspect, 0.1f, 1000f);
			Engine.level = new Level(new Camera(projection){{
				Transform.setPosition(getTransform(), new Vec3(0, 0, -10));
			}});
			try {
				Engine.level.load(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Material material = new Material();
			material.setModelGenerator(new SimpleModelGenerator());
			MaterialEntity entity = new MaterialEntity();
			entity.setLevel(Engine.level);
			entity.setMaterial(material);
			Vec2[] verts = {
					new Vec2(-1, -1),
					new Vec2(1, -1),
					new Vec2(1, 1),
					new Vec2(-1, 1)};
			PhysicsObjectDef ObjectDef = entity.getLevel().physicsEngine.newPhysicsObjectDef(
					PhysicsObject.Type.DYNAMIC);
			Body body = entity.getLevel().physicsEngine.newBody();
			body.setFriction(0.5f);
			body.setRestitution(0.15f);
			body.setDensity(1);
			body.setShape(Body.ShapeType.COMPLEX_POLYGON, verts);
			PhysicsObject object = entity.setPhysicsObject(ObjectDef);
			object.createBody(body);
			entity.setShape(verts);

			Entity player = new Entity();
			PlayerController controller = new PlatformerPlayerController();
			controller.setPawn(player);
			Engine.level.players.add(controller);
			player.setLevel(Engine.level);
			Vec2[] playerVertices = {
					new Vec2(-0.35f, 0),
					new Vec2(0.35f, 0),
					new Vec2(0.35f, 1.8f),
					new Vec2(-0.35f, 1.8f)};
			PhysicsObjectDef objectDef = player.getLevel().physicsEngine.newPhysicsObjectDef(
					PhysicsObject.Type.DYNAMIC);
			PhysicsObject playerObject = player.setPhysicsObject(objectDef);
			Body playerBody = player.getLevel().physicsEngine.newBody();
			playerBody.setShape(Body.ShapeType.COMPLEX_POLYGON, playerVertices);
			playerObject.createBody(playerBody);
			Mat4 transform = Mat4.identity();
			Transform.setPosition(transform, new Vec3(-3, 0, 0));
			player.setTransform(transform);

			//lambda expressions make this pretty
			ResourceManager.getResource(
					"Player.obj",
					loadedResource -> player.setModel((ModelLoader) loadedResource),
					ModelLoader.class);
		}
		loadingText.setText(String.format(loadingMessage, (int) percentComplete));
	}
}
