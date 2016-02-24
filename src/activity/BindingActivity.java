package activity;

import gui.*;
import gui.Panel;
import input.Input;
import input.InputInterface;

import java.awt.*;
import java.util.Map;

/**
 * Created by Nyrmburk on 2/21/2016.
 */
public class BindingActivity extends Activity {

	@Override
	protected void onCreate() {

		Panel view = new Panel();
		view.setWidthLayout(GUIElement.layout.FILL_PARENT);
		view.setHeightLayout(GUIElement.layout.FILL_PARENT);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.DARK_GRAY);

		for (InputInterface inputInterface : InputInterface.getInterfaces()) {
			for (Map.Entry<Object, Input> entry : inputInterface.inputs.entrySet()) {

				TextBox actionName = new TextBox();
				actionName.setText(entry.getValue().getName() + " = " + inputInterface.getBindingName(entry.getKey()));
				view.addChild(actionName, 0);
			}
		}

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
	public void onUpdate(int delta) {

	}
}
