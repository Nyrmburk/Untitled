package activity;

import gui.*;
import gui.Button;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import input.Input;
import input.InputInterface;
import main.Engine;

import java.awt.*;
import java.util.Map;

/**
 * Created by Nyrmburk on 2/21/2016.
 */
public class BindingActivity extends Activity {

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.WHITE);

		for (InputInterface inputInterface : InputInterface.getInterfaces()) {
			for (Map.Entry<Object, Input> entry : inputInterface.inputs.entrySet()) {

				TextBox actionName = new TextBox();
				actionName.setText(entry.getValue().getName() + " = " + inputInterface.getBindingName(entry.getKey()));
				view.addChild(actionName, 0);
			}
		}

		Button back = new Button();
		back.setText("Back");
		back.addPointerListener(new PointerListener() {

			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					Activity.stopCurrentActivity();
			}
		});
		view.addChild(back, 0);

		long time = System.nanoTime();
		setView(view);
		System.out.println((float) (System.nanoTime() - time) / 1000000);
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

	}
}
