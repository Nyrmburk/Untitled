package activity;

import entity.Entity;
import entity.MaterialEntity;
import game.*;
import graphics.ModelLoader;
import graphics.PerspectiveCamera;
import gui.*;
import main.Engine;
import main.ResourceManager;
import matrix.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import static physics.JBox2D.*;

import java.awt.*;
import java.io.IOException;

public class LoadingActivity extends Activity {

	Level level;
	
	private float percentComplete = 0;
	private float waitTime = 0.0f;
	String loadingMessage = "Loading... %d%%";
	TextBox loadingText;

	public LoadingActivity(Level level) {
		
		this.level = level;
	}
	
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

		percentComplete += 100 * delta / waitTime;
		if (percentComplete > 100) {

			percentComplete = 100;
			Activity.stopCurrentActivity();

			try {
				level.load(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Material material = new Material();
			material.setModelGenerator(new SimpleModelGenerator());
			MaterialEntity entity = new MaterialEntity();
			entity.setLevel(level);
			entity.setMaterial(material);
			Vec2[] verts = {
					new Vec2(-1, -1),
					new Vec2(1, -1),
					new Vec2(1, 1),
					new Vec2(-1, 1)};
			entity.setShape(verts);
			BodyDef objectDef = new BodyDef();
			objectDef.setType(BodyType.DYNAMIC);
			Body body = entity.setPhysicsObject(objectDef);

			for (Shape shape : shapeFromPolygon(verts)) {
				FixtureDef fixture = new FixtureDef();
				fixture.setFriction(0.5f);
				fixture.setRestitution(0.15f);
				fixture.setDensity(1);
				fixture.setShape(shape);
				body.createFixture(fixture);
			}

			Entity player = new Entity();
			PlayerController controller = new PlatformerPlayerController();
			controller.setPawn(player);
			level.players.add(controller);
			player.setLevel(level);
			Vec2[] playerVertices = {
					new Vec2(-0.35f, 0),
					new Vec2(0.35f, 0),
					new Vec2(0.35f, 1.8f),
					new Vec2(-0.35f, 1.8f)};
			objectDef = new BodyDef();
			objectDef.setType(BodyType.DYNAMIC);
			Body playerObject = player.setPhysicsObject(objectDef);
			for (Shape shape : shapeFromPolygon(playerVertices))
				playerObject.createFixture(shape, 0);
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
