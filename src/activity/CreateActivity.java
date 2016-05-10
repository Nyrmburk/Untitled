package activity;

import draftform.Curve;
import draftform.Draftform;
import draftform.Vec2;
import graphics.InstanceAttributes;
import graphics.modelconverter.LineConverter;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;
import main.Line;
import tools.*;
import tools.Toolkit;

import java.awt.*;

public class CreateActivity extends Activity {

	Draftform draftform = new Draftform();
	tools.Toolkit toolkit = new Toolkit(draftform);
	LineConverter lc = new LineConverter();

	@Override
	protected void onCreate() {

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



				for (Curve curve : draftform.getCurves()) {

					Vec2[] verts = curve.linearize(curve.recommendedSubdivisions());

					Line line = new Line(verts.length, false);

					for (int i = 0; i < verts.length; i++) {

						matrix.Vec2 converted = new matrix.Vec2(verts[i].getX(), verts[i].getY());
						line.setData(i, converted, 1, Color.BLACK);
					}

					getRenderContext().addModel(lc.convert(line), new InstanceAttributes(new float[]{0, 0, 0}, new float[]{0, 0, 0}));
				}
//				System.out.println(vec);
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
}
