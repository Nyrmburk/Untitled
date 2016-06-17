package activity;

import draftform.*;
import entity.Camera;
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
import physics.PhysicsObject;
import physics.PhysicsObjectDef;
import physics.Polygon;
import tools.*;
import tools.Toolkit;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CreateActivity extends Activity {

	private Draftform draftform = new Draftform();
	private tools.Toolkit toolkit = new Toolkit(draftform);
	private ModelGroup draftformGroup = new ModelGroup();
	private LineConverter lc = new LineConverter();
	private InstanceAttributes draftformInstance = new InstanceAttributes();
	private ModelLoader vertexModel = createVertexModel();

	@Override
	protected void onCreate() {

		toolkit.setSnapRadius(3);
		toolkit.setSnapToPoints(true);

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());
		view.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {

				Point point = getPointerLocation();
				draftform.Vec2 vec = new draftform.Vec2(point.x, point.y);

				if (getCurrentState() == State.PRESS)
					toolkit.start(vec);
				if (getCurrentState() == State.DRAG)
					toolkit.modify(vec);
				if (getCurrentState() == State.RELEASE)
					toolkit.end();

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

		setVelocityCamera(new Vec3(0, 3, 10), 0.1f);
	}

	private void setVelocityCamera(Vec3 cameraOffset, float magnitude) {

		Vec3 avgPosition = new Vec3();
		Vec3 avgVelocity = new Vec3();

		for (PlayerController player : Engine.level.players) {

			float[] position = player.getPawn().getPhysicsObject().getPosition();
			float[] velocity = player.getPawn().getPhysicsObject().getLinearVelocity();
			avgPosition = avgPosition.add(new Vec3(position[0], position[1], 0));
			avgVelocity = avgVelocity.add(new Vec3(velocity[0], velocity[1], 0));
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
				pointsList.add(new Vec2(dVec.getX(), getView().getHeight() - dVec.getY()));

			pointsList.removeLast();
		}

		RenderContext world = Engine.level.getRenderContext();

		Vec2[] points = new Vec2[pointsList.size()];
		pointsList.toArray(points);

		Ray3[] rays = Projection.unproject(points, world.getCamera(), Engine.renderEngine.getViewport());

		Plane plane = new Plane(0, 0, 0, 0, 0, 1);

		Vec2[] vertices = new Vec2[rays.length];

		for (int i = 0; i < rays.length; i++) {
			Vec3 point = rays[i].point(plane.intersect(rays[i]));
			vertices[i] = new Vec2(point.x, point.y);
		}

		System.out.println(Arrays.toString(vertices));

		Polygon poly = new Polygon(vertices);

		MaterialEntity newEntity = new MaterialEntity();
		Material material = new Material();
		material.setModelGenerator(new SimpleModelGenerator());
		newEntity.setMaterial(material);
		newEntity.setShape(poly);
		PhysicsObjectDef shape = Engine.level.physicsEngine.getPhysicsObjectDef(
				PhysicsObject.Type.DYNAMIC, poly);
		shape.setDensity(0.1f);
		newEntity.setPhysicsObject(shape);
		newEntity.setLevel(Engine.level);

		draftform.getCurves().clear();
		draftform.getVerts().clear();
		drawDraftform();
	}

	private void drawDraftform() {

		getRenderContext().getModelGroup().removeModelGroup(draftformGroup);
		draftformGroup.clear();

		ArrayList<InstanceAttributes> vertices = new ArrayList<>(draftform.getCurves().size() * 4) ;

		for (Curve curve : draftform.getCurves()) {

			draftform.Vec2[] verts = curve.linearize(curve.recommendedSubdivisions());

			Line line = new Line(verts.length, false);

			for (int i = 0; i < verts.length; i++) {

				matrix.Vec2 converted = new matrix.Vec2(verts[i].getX(), verts[i].getY());
				line.setData(i, converted, 2, Color.WHITE);
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

		getRenderContext().getModelGroup().addModelGroup(draftformGroup);
	}

	private InstanceAttributes getVertexAttributes(Vertex vertex) {

		InstanceAttributes vertexAttributes = new InstanceAttributes();

		Mat4 transform = Mat4.identity();
		Transform.setPosition(transform, new Vec3(vertex.getX(), vertex.getY(), 0));
		vertexAttributes.setTransform(transform);

		return vertexAttributes;
	}

	private ModelLoader createVertexModel() {

		ModelLoader vertexModel = new ModelLoader();
		vertexModel.vertices.put(-3f, 3f, 0f);
		vertexModel.vertices.put(-3f, -3f, 0f);
		vertexModel.vertices.put(3f, -3f, 0f);
		vertexModel.vertices.put(3f, 3f, 0f);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
		vertexModel.color.add(0xFFFFFFFF);
//		vertexModel.addFace(0, 1, 2, 3);
		vertexModel.addFace(3, 2, 1, 0);

		return vertexModel;
	}
}
