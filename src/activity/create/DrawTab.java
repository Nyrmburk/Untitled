package activity.create;

import draftform.Curve;
import draftform.Draftform;
import draftform.Vertex;
import entity.MaterialEntity;
import game.Material;
import game.SimpleModelGenerator;
import graphics.InstanceAttributes;
import graphics.ModelGroup;
import graphics.ModelLoader;
import graphics.RenderContext;
import graphics.modelconverter.LineConverter;
import gui.Button;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import main.Engine;
import main.Line;
import matrix.*;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import tools.PenTool;
import tools.SelectTool;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static physics.JBox2D.shapeFromPolygon;


/**
 * Created by Nyrmburk on 8/6/2016.
 */
public class DrawTab extends Tab {

	private Vec3 worldPoint;
	private Plane drawPlane = new Plane(0, 0, 0.5f, 0, 0, 1);
	private Body selectBody;
	private Draftform draftform = new Draftform();
	private tools.Toolkit toolkit = new tools.Toolkit(draftform);
	private ModelGroup draftformGroup = new ModelGroup();
	private LineConverter lc = new LineConverter();
	private InstanceAttributes draftformInstance = new InstanceAttributes();
	private ModelLoader vertexModel = createVertexModel(0.1f);

	private final Button btnSelect = new Button();
	private final Button btnPen = new Button();
	private final Button btnClear = new Button();
	private final Button btnCommit = new Button();

	public DrawTab() {
		super("Draw");
		vertexModel.register();

		Mat4 transform = draftformInstance.getTransform();
		Vec3 pos = Transform.getPosition(transform);
		pos.z = 0.5f;
		Transform.setPosition(transform, pos);

		toolkit.setSnapRadius(0.15f);
		toolkit.setSnapToPoints(true);

		setBackgroundColor(Color.LIGHT_GRAY);
		setlayout(new GUIBoxLayout());

		btnSelect.setText("Select");
		btnSelect.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					select();
			}
		});
		addChild(btnSelect, 0);

		btnPen.setText("Pen");
		btnPen.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					pen();
			}
		});
		addChild(btnPen, 0);

		btnClear.setText("Clear");
		btnClear.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					clear();
			}
		});
		addChild(btnClear, 0);

		btnCommit.setText("Commit");
		btnCommit.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					commit();
			}
		});
		addChild(btnCommit, 0);
	}

	private void select() {
		toolkit.setTool(new SelectTool());
	}

	private void pen() {
		toolkit.setTool(new PenTool());
	}

	private void clear() {
		toolkit.clearSelection();
		toolkit.currentTool.reset();
		draftform.getCurves().clear();
		draftform.getVerts().clear();
		drawDraftform();
	}

	private void commit() {

		LinkedList<Vec2> pointsList = new LinkedList<>();

		for (Curve curve : draftform.getCurves()) {

			for (draftform.Vec2 dVec : curve.linearize(curve.recommendedSubdivisions()))
				pointsList.add(new Vec2(dVec.getX(), dVec.getY()));

			pointsList.removeLast();
		}

		Vec2[] points = new Vec2[pointsList.size()];
		pointsList.toArray(points);
		if (!physics.Polygon.isWindingCCW(points))
			physics.Polygon.reverseWinding(points);
		if (physics.Polygon.selfIntersects(points)) {

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

	@Override
	public void update(float delta) {

	}

	@Override
	public void pointerEvent(PointerListener.PointerEvent event) {

		Vec2 screenPoint = new Vec2(event.pointer.x, view.getHeight() - event.pointer.y);
		worldPoint = getWorldPoint(screenPoint);

		updateTool(event.state);
	}

	private Vec3 getWorldPoint(Vec2 point) {

		RenderContext world = Engine.level.getRenderContext();
		Ray3[] rays = Projection.unproject(world.getCamera(), Engine.renderEngine.getViewport(), point);

		Ray3 ray;
		if (rays == null) {
			return null;
		} else {
			ray = rays[0];
		}

		return ray.point(drawPlane.intersect(ray));
	}

	private void updateTool(PointerListener.State state) {

		draftform.Vec2 draftformPoint = new draftform.Vec2(worldPoint.x, worldPoint.y);
		switch (state) {
			case PRESS:
				toolkit.start(draftformPoint);
				break;
			case DRAG:
				toolkit.modify(draftformPoint);
				break;
			case RELEASE:
				toolkit.end();
				break;
		}

		if (state != PointerListener.State.MOVE) drawDraftform();
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

			java.util.List<ModelLoader> lineModels = lc.convert(line);

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
		Transform.setPosition(transform, new Vec3(vertex.getX(), vertex.getY(), 0.5f));
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
