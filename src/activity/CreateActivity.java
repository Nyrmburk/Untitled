package activity;

import draftform.Curve;
import draftform.Draftform;
import draftform.Vec2;
import draftform.Vertex;
import graphics.InstanceAttributes;
import graphics.ModelGroup;
import graphics.ModelLoader;
import graphics.modelconverter.LineConverter;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;
import main.Line;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec3;
import tools.*;
import tools.Toolkit;

import java.awt.*;
import java.util.ArrayList;
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

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());
		view.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {

				Point point = getPointerLocation();
				Vec2 vec = new Vec2(point.x, point.y);

//				System.out.println(getCurrentState());
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

	private void drawDraftform() {

		getRenderContext().getModelGroup().removeModelGroup(draftformGroup);
		draftformGroup.clear();

		ArrayList<InstanceAttributes> vertices = new ArrayList<>(draftform.getCurves().size() * 4) ;

		for (Curve curve : draftform.getCurves()) {

			Vec2[] verts = curve.linearize(curve.recommendedSubdivisions());

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
		vertexAttributes.setTransform(
				Transform.setPosition(Mat4.identity(),
				new Vec3(vertex.getX(), vertex.getY(), 0)));
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
