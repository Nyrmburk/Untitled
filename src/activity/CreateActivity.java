package activity;

import draftform.Draftform;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;
import tools.*;
import tools.Toolkit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateActivity extends Activity {

	Draftform draftform;
	tools.Toolkit toolkit = new Toolkit(draftform);

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());

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
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Button.CLICKED)) {
					toolkit.setTool(new SelectTool());
					System.out.println("Select");
				}
			}
		});
		panel.addChild(select, 0);

		Button pen = new Button();
		pen.setText("Pen");
		pen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Button.CLICKED)) {
					toolkit.setTool(new PenTool());
					System.out.println("Pen");
				}
			}
		});
		panel.addChild(pen, 0);

		Button vertex = new Button();
		vertex.setText("Vertex");
		vertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Button.CLICKED)) {
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
