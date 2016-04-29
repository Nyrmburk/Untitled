package activity;

import draftform.Draftform;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;

import java.awt.*;

public class CreateActivity extends Activity {

	Draftform draftform;

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());

		Panel panel = new Panel(new GUIBoxLayout());
		panel.setBackgroundColor(new Color(220, 220, 220, 200));
		view.getLayout().setConstraint(panel, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.LEFT,
				GUIProportionLayout.Constraint.RIGHT, 0.25f,
				view, GUIProportionLayout.Constraint.RIGHT));
		view.getLayout().setConstraint(panel, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.TOP,
				GUIProportionLayout.Constraint.BOTTOM, 1f,
				view, GUIProportionLayout.Constraint.BOTTOM));
		view.addChild(panel);

		Button select = new Button();
		select.setText("Select");
		panel.addChild(select, 0);

		Button Bezier = new Button();
		Bezier.setText("Bezier");
		panel.addChild(Bezier, 0);

		Button Vertex = new Button();
		Vertex.setText("Vertex");
		panel.addChild(Vertex, 0);

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
