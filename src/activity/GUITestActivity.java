package activity;

import gui.*;
import gui.Panel;
import main.Engine;

import java.awt.*;

/**
 * Created by Nyrmburk on 8/3/2016.
 */
public class GUITestActivity extends Activity {

	TextBox strobe;
	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setName("view");
		view.setBackgroundColor(new Color(220, 220, 220, 200));
		view.setlayout(new GUIBoxLayout());

		view.addActionListener(new RedrawListener() {
			@Override
			public void actionPerformed() {
				System.out.println(guiElements);
			}
		});

		TextBox text = new TextBox();
		text.setName("textbox");
		text.setText("banana");
		view.addChild(text);

		Panel panel = new Panel();
		panel.setName("panel");
		panel.setPreferredSize(new Dimension(100, 100));
		panel.setBackgroundColor(new Color(220, 220, 220, 200));
		view.addChild(panel, null);

		strobe = new TextBox();
		strobe.setName("strobe");
		strobe.setText("strobe");
		panel.addChild(strobe);

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

	float time;
	@Override
	public void onUpdate(float delta) {

		if ((time += delta) > 0.48f) {
			strobe.setVisible(!strobe.isVisible());
			time = 0;
		}
	}
}
