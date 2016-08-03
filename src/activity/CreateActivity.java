package activity;

import draftform.*;
import graphics.Camera;
import entity.MaterialEntity;
import game.Material;
import game.PlayerController;
import game.SimpleModelGenerator;
import graphics.InstanceAttributes;
import graphics.ModelGroup;
import graphics.ModelLoader;
import graphics.RenderContext;
import graphics.modelconverter.LineConverter;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;
import main.Line;
import matrix.*;
import matrix.Vec2;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import physics.*;
import physics.Polygon;
import tools.*;
import tools.Toolkit;

import java.awt.*;
import java.util.*;
import java.util.List;

import static physics.JBox2D.convert;
import static physics.JBox2D.shapeFromPolygon;

public class CreateActivity extends Activity {

	private Draftform draftform = new Draftform();
	private tools.Toolkit toolkit = new Toolkit(draftform);
	private ModelGroup draftformGroup = new ModelGroup();
	private LineConverter lc = new LineConverter();
	private InstanceAttributes draftformInstance = new InstanceAttributes();
	private Plane drawPlane = new Plane(0, 0, 0, 0, 0, 1);
	private ModelLoader vertexModel = createVertexModel(0.1f);
	private Body selectBody;
	TextBox fps;
	TextBox position;
	TextBox hover;

	@Override
	protected void onCreate() {

		toolkit.setSnapRadius(0.15f);
		toolkit.setSnapToPoints(true);

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());
		view.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {

				Point pointer = getPointerLocation();

				Vec2 screenpoint = new Vec2(pointer.x, getView().getHeight() - pointer.y);

				RenderContext world = Engine.level.getRenderContext();
				Ray3 ray = Projection.unproject(world.getCamera(), Engine.renderEngine.getViewport(), screenpoint)[0];

				Vec3 worldPoint = ray.point(drawPlane.intersect(ray));
				draftform.Vec2 draftformPoint = new draftform.Vec2(worldPoint.x, worldPoint.y);

				if (getCurrentState() == State.PRESS)
					toolkit.start(draftformPoint);
				if (getCurrentState() == State.DRAG)
					toolkit.modify(draftformPoint);
				if (getCurrentState() == State.RELEASE) {
					toolkit.end();
				}

				position.setText(String.format("%8.2f, %8.2f", worldPoint.x, worldPoint.y));

				org.jbox2d.common.Vec2 b2Point = new org.jbox2d.common.Vec2(worldPoint.x, worldPoint.y);
				AABB aabb = new AABB(new org.jbox2d.common.Vec2(worldPoint.x-0.1f, worldPoint.y-0.1f), new org.jbox2d.common.Vec2(worldPoint.x+0.1f, worldPoint.y+0.1f));
				hover.setText("nothing");
				Engine.level.physicsEngine.queryAABB(new QueryCallback() {
					@Override
					public boolean reportFixture(Fixture fixture) {
						if (fixture.testPoint(b2Point)) {
							String name = fixture.getBody().toString();
							hover.setText(name.substring(name.indexOf('@')));
							return true;
						}
						return false;
					}
				}, aabb);

				if (getCurrentState() == State.PRESS) {

					Engine.level.physicsEngine.queryAABB(new QueryCallback() {
						@Override
						public boolean reportFixture(Fixture fixture) {
							if (fixture.testPoint(b2Point)) {
								selectBody = fixture.getBody();
								return true;
							}
							return false;
						}
					}, aabb);
				} else if (this.getCurrentState() == State.RELEASE) {

					if (selectBody != null)
						selectBody.setLinearVelocity(new org.jbox2d.common.Vec2(0, 0));
					selectBody = null;
				}

				if (selectBody != null) {
					float x = (worldPoint.x - selectBody.getWorldCenter().x) * 120;
					float y = (worldPoint.y - selectBody.getWorldCenter().y) * 120;
					selectBody.setLinearVelocity(new org.jbox2d.common.Vec2(x, y));
				}

				if (getCurrentState() != State.MOVE) drawDraftform();
			}
		});

		Panel panel = new Panel(new GUIBoxLayout());
		panel.setBackgroundColor(new Color(220, 220, 220, 200));
		view.getLayout().setConstraint(panel, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.LEFT,
				GUIProportionLayout.Constraint.RIGHT, 0.2f,
				view, GUIProportionLayout.Constraint.RIGHT));
		view.getLayout().setConstraint(panel, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.TOP,
				GUIProportionLayout.Constraint.BOTTOM, 1f,
				view, GUIProportionLayout.Constraint.BOTTOM));
		view.addChild(panel);

		fps = new TextBox();
		fps.setText("fps");
		panel.addChild(fps, 0);

		position = new TextBox();
		position.setText("position");
		panel.addChild(position, 0);

		hover = new TextBox();
		hover.setText("hover");
		panel.addChild(hover, 0);

		Button select = new Button();
		select.setText("Select");
		select.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (this.getCurrentState() == State.CLICK) {
					toolkit.setTool(new SelectTool());
					System.out.println("Select");
				}
			}
		});
		panel.addChild(select, 0);

		Button pen = new Button();
		pen.setText("Pen");
		pen.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {

				if (this.getCurrentState() == State.CLICK) {
					toolkit.setTool(new PenTool());
					System.out.println("Pen");
				}
			}
		});
		panel.addChild(pen, 0);

		Button vertex = new Button();
		vertex.setText("Vertex");
		vertex.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (this.getCurrentState() == State.CLICK) {
					toolkit.setTool(new VertexTool());
					System.out.println("Vertex");
				}
			}
		});
		panel.addChild(vertex, 0);

		Button clear = new Button();
		clear.setText("Clear");
		clear.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (this.getCurrentState() == State.CLICK) {
					System.out.println("Clearing drawing");

					toolkit.clearSelection();
					toolkit.currentTool.reset();
					draftform.getCurves().clear();
					draftform.getVerts().clear();
					drawDraftform();
				}
			}
		});
		panel.addChild(clear, 0);

		Button commit = new Button();
		commit.setText("Commit");
		commit.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (this.getCurrentState() == State.CLICK) {
					System.out.println("Committing drawn shape to world");

					commitDraftform();
				}
			}
		});
		panel.addChild(commit, 0);

		Button back = new Button();
		back.setText("Back");
		back.addActionListener(new PointerListener() {

			@Override
			public void actionPerformed() {
				if (getCurrentState() == State.CLICK)
					Activity.stopCurrentActivity();
			}
		});
		panel.addChild(back, 0);

//		panel.setVisible(false);

		setView(view);

		LoadingActivity loading = new LoadingActivity();
		createActivity(loading);
	}

	@Override
	protected void onStart() {
		vertexModel.register();
	}

	@Override
	protected void onResume() {
	}

	@Override
	protected void onPause() {
	}

	@Override
	protected void onStop() {
		vertexModel.release();
	}

	@Override
	protected void onDestroy() {
	}

	@Override
	public void onUpdate(float delta) {

		setVelocityCamera(new Vec3(0, 4, 10), 0);
		fps.setText(String.format("%6.2f fps", 1f / delta));
	}

	private void setVelocityCamera(Vec3 cameraOffset, float magnitude) {

		Vec3 avgPosition = new Vec3();
		Vec3 avgVelocity = new Vec3();

		for (PlayerController player : Engine.level.players) {

			avgPosition = avgPosition.add(convert(player.getPawn().getPhysicsObject().getPosition()).asVec3());
			avgVelocity = avgVelocity.add(convert(player.getPawn().getPhysicsObject().getLinearVelocity()).asVec3());
		}

		avgPosition.divide(Engine.level.players.size());
		avgVelocity.divide(Engine.level.players.size());

		Vec3 cameraPosition = avgPosition.add(cameraOffset);

		Camera camera = Engine.level.getRenderContext().getCamera();
		cameraOffset.z = 0;

		Mat4 transform = Mat4.identity();
		Vec3 up = new Vec3(0, 1, 0);
		Vec3 target = avgPosition.add(avgVelocity.multiply(magnitude).add(cameraOffset));
		Transform.pointAt(transform, cameraPosition, target, up);

		camera.setTransform(transform);
	}

	private void commitDraftform() {

		LinkedList<Vec2> pointsList = new LinkedList<>();

		for (Curve curve : draftform.getCurves()) {

			for (draftform.Vec2 dVec : curve.linearize(curve.recommendedSubdivisions()))
				pointsList.add(new Vec2(dVec.getX(), dVec.getY()));

			pointsList.removeLast();
		}

		Vec2[] points = new Vec2[pointsList.size()];
		pointsList.toArray(points);
		if (!Polygon.isWindingCCW(points))
			Polygon.reverseWinding(points);
		if (Polygon.selfIntersects(points)) {

			System.out.println("polygon is self-intersecting");
			return;
		}

		MaterialEntity newEntity = new MaterialEntity();
		newEntity.setLevel(Engine.level);
		Material material = new Material();
		material.setModelGenerator(new SimpleModelGenerator());
		newEntity.setMaterial(material);
		newEntity.setShape(points);
		BodyDef objectDef = new BodyDef();
		objectDef.setType(BodyType.DYNAMIC);
		Body object = newEntity.setPhysicsObject(objectDef);
		for (org.jbox2d.collision.shapes.Shape shape : shapeFromPolygon(points))
			object.createFixture(shape, 0.1f);

		draftform.getCurves().clear();
		draftform.getVerts().clear();
		drawDraftform();
	}

	private void drawDraftform() {

		RenderContext world = Engine.level.getRenderContext();
		world.getModelGroup().removeModelGroup(draftformGroup);
		draftformGroup.clear();

		ArrayList<InstanceAttributes> vertices = new ArrayList<>(draftform.getCurves().size() * 4) ;

		for (Curve curve : draftform.getCurves()) {


			draftform.Vec2[] verts = curve.linearize(curve.recommendedSubdivisions());

			Line line = new Line(verts.length, false);

			for (int i = 0; i < verts.length; i++) {

				matrix.Vec2 converted = new matrix.Vec2(verts[i].getX(), verts[i].getY());
				line.setData(i, converted, 0.1f, Color.WHITE);
			}

			List<ModelLoader> lineModels = lc.convert(line);

			for (ModelLoader model : lineModels)
				draftformGroup.addInstance(model, draftformInstance);

			vertices.add(getVertexAttributes(curve.getStart()));
			vertices.add(getVertexAttributes(curve.getEnd()));

			for (Vertex vertex : curve.getControlPoints())
				vertices.add(getVertexAttributes(vertex));
		}

		draftformGroup.addInstance(vertexModel, vertices);

		world.getModelGroup().addModelGroup(draftformGroup);
	}

	private InstanceAttributes getVertexAttributes(Vertex vertex) {

		InstanceAttributes vertexAttributes = new InstanceAttributes();

		Mat4 transform = Mat4.identity();
		Transform.setPosition(transform, new Vec3(vertex.getX(), vertex.getY(), 0));
		vertexAttributes.setTransform(transform);

		return vertexAttributes;
	}

	private ModelLoader createVertexModel(float radius) {

		ModelLoader vertexModel = new ModelLoader();
		vertexModel.vertices.put(-radius, radius, 0f);
		vertexModel.vertices.put(-radius, -radius, 0f);
		vertexModel.vertices.put(radius, -radius, 0f);
		vertexModel.vertices.put(radius, radius, 0f);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.addFace(0, 1, 2, 3);
//		vertexModel.addFace(3, 2, 1, 0);

		return vertexModel;
	}
}
