package activity;

import gui.*;
import gui.Button;
import gui.Panel;
import gui.event.*;
import gui.event.Event;
import gui.layout.GUIBoxLayout;
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

		view.addRedrawListener(new RedrawListener() {
			@Override
			public void actionPerformed(Event event) {
//				System.out.println(guiElements);
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

		Button button = new Button();
		button.setName("Button");
		button.setText("Button");
		button.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				System.out.println(event.state);
			}
		});
		view.addChild(button);

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
