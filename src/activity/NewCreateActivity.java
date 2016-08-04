package activity;

import game.Level;
import game.PlayerController;
import graphics.Camera;
import graphics.PerspectiveCamera;
import gui.*;
import gui.Button;
import gui.Panel;
import main.Engine;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec3;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static physics.JBox2D.convert;

/**
 * This Activity is aimed at displaying tools and data using a bar on the bottom
 * for tabs, a panel on the right to show selections, and panels for each tab.
 *
 * Each tab will be determined by plugins?
 * If each tab is an activity, then it would be exceedingly easy to add new ones.
 * The problem arises when finding new activities to add.
 * Since the other activities are files, they should be queryable.
 * Fuck the plugin part.
 * Right now I need to get some progress
 * Once the plugin spec is finished, I will add the feature.
 *
 * Created by Nyrmburk on 8/2/2016.
 */
public class NewCreateActivity extends Activity {

	private Level level;

	private View view;
	private Panel menuBar = new Panel();

	private Button btnInfo = new Button();
	private Panel pnlInfo = new Panel();

	private Button btnDraw = new Button();
	private Panel pnlDraw = new Panel();
	private Button btnDrawSelect = new Button();
	private Button btnDrawPen = new Button();
	private Button btnDrawClear = new Button();
	private Button btnDrawCommit = new Button();

	private Button btnBack = new Button();

	private List<Panel> tabs = new LinkedList<>();

	@Override
	protected void onCreate() {

		createLevel();

		createActivity(new LoadingActivity(level));

		initUI();

		setView(view);
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
	public void onUpdate(float delta) {

		updateFollowCam(new Vec3(0, 4, 10));
	}

	private void createLevel() {

		Camera cam = new PerspectiveCamera();
		Transform.setPosition(cam.getTransform(), new Vec3(0, 0, -10));
		level = new Level(cam);
		Engine.level = level;
	}

	private void initUI() {

		view = new View(Engine.renderEngine);
		view.setlayout(new GUIProportionLayout());

		menuBar.setlayout(new GUIBoxLayout());
		menuBar.setBackgroundColor(Color.WHITE);
		view.getLayout().setConstraint(menuBar, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.LEFT,
				GUIProportionLayout.Constraint.RIGHT, 0.075f,
				view, GUIProportionLayout.Constraint.RIGHT));
		view.getLayout().setConstraint(menuBar, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.TOP,
				GUIProportionLayout.Constraint.BOTTOM, 1f,
				view, GUIProportionLayout.Constraint.BOTTOM));
		view.addChild(menuBar);

		createInfoTab();
		createDrawTab();

		btnBack.setText("Back");
		btnBack.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (getCurrentState() == State.CLICK)
					Activity.stopCurrentActivity();
			}
		});
		menuBar.addChild(btnBack, 0);
	}

	private void createTab(Panel tab) {

		view.getLayout().setConstraint(tab, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.BOTTOM,
				GUIProportionLayout.Constraint.TOP, 1f,
				view, GUIProportionLayout.Constraint.TOP));
		view.getLayout().setConstraint(tab, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.TOP,
				GUIProportionLayout.Constraint.BOTTOM, 1f,
				view, GUIProportionLayout.Constraint.BOTTOM));
		view.getLayout().setConstraint(tab, new GUIProportionLayout.Anchor(
				view, GUIProportionLayout.Constraint.RIGHT,
				GUIProportionLayout.Constraint.LEFT, 1f,
				menuBar, GUIProportionLayout.Constraint.RIGHT));
		view.getLayout().setConstraint(tab, new GUIProportionLayout.Anchor(
				menuBar, GUIProportionLayout.Constraint.RIGHT,
				GUIProportionLayout.Constraint.RIGHT, 0.2f,
				view, GUIProportionLayout.Constraint.RIGHT));
		view.addChild(tab);

		tabs.add(tab);
	}

	private void activateTab(Panel tab) {

		boolean visibility = !tab.isVisible();

		for (Panel panel : tabs)
			panel.setVisible(false);

		tab.setVisible(visibility);
	}

	private void createInfoTab() {

		btnInfo.setText("Info");
		btnInfo.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (getCurrentState() == State.CLICK) {
					activateTab(pnlInfo);
				}
			}
		});
		menuBar.addChild(btnInfo, 0);

		pnlInfo.setBackgroundColor(Color.LIGHT_GRAY);
		pnlInfo.setlayout(new GUIBoxLayout());
		pnlInfo.setVisible(false);
		createTab(pnlInfo);
	}

	private void createDrawTab() {

		btnDraw.setText("Draw");
		btnDraw.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				if (getCurrentState() == State.CLICK) {
					activateTab(pnlDraw);
				}
			}
		});
		menuBar.addChild(btnDraw, 0);

		pnlDraw.setBackgroundColor(Color.LIGHT_GRAY);
		pnlDraw.setlayout(new GUIBoxLayout());
		pnlDraw.setVisible(false);
		createTab(pnlDraw);

		btnDrawSelect.setText("Select");
		btnDrawSelect.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				drawSelect();
			}
		});
		pnlDraw.addChild(btnDrawSelect, 0);

		btnDrawPen.setText("Pen");
		btnDrawSelect.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				drawPen();
			}
		});
		pnlDraw.addChild(btnDrawPen, 0);

		btnDrawClear.setText("Clear");
		btnDrawSelect.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				drawClear();
			}
		});
		pnlDraw.addChild(btnDrawClear, 0);

		btnDrawCommit.setText("Commit");
		btnDrawSelect.addActionListener(new PointerListener() {
			@Override
			public void actionPerformed() {
				drawCommit();
			}
		});
		pnlDraw.addChild(btnDrawCommit, 0);
	}

	private void drawSelect() {

	}

	private void drawPen() {

	}

	private void drawClear() {

	}

	private void drawCommit() {

	}

	private void updateFollowCam(Vec3 cameraOffset) {

		Vec3 avgPosition = new Vec3();

		for (PlayerController player : Engine.level.players)
			avgPosition = avgPosition.add(convert(player.getPawn().getPhysicsObject().getPosition()).asVec3());

		avgPosition.divide(Engine.level.players.size());

		Vec3 cameraPosition = avgPosition.add(cameraOffset);

		Camera camera = Engine.level.getRenderContext().getCamera();
		cameraOffset.z = 0;

		Mat4 transform = Mat4.identity();
		Vec3 up = new Vec3(0, 1, 0);
		Vec3 target = avgPosition.add(cameraOffset);
		Transform.pointAt(transform, cameraPosition, target, up);

		camera.setTransform(transform);
	}
}
