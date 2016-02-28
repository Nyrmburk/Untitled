package activity;

import gui.Panel;
import gui.TextBox;
import gui.css.CSSCanvas;
import gui.css.CSSElementBox;
import gui.css.CSSTextBox;

/**
 * Created by Nyrmburk on 2/26/2016.
 */
public class CSSActivity extends Activity {

	private CSSCanvas canvas;

	public CSSActivity(CSSCanvas canvas) {

		this.canvas = canvas;
	}

	@Override
	protected void onCreate() {

		Panel view = new Panel();
		view.setBounds(canvas.getBounds());

		for (CSSElementBox elembox : canvas.getComposite().elementBoxes) {

			if (elembox.getWidth() > 0 && elembox.getHeight() > 0)
				view.addChild(new Panel(){{
					setBounds(elembox.getBounds());
				}});
		}

		for (CSSTextBox txtbox : canvas.getComposite().textBoxes) {

			view.addChild(new TextBox(){{
				setText(txtbox.getText());
				setBounds(txtbox.getBounds());
			}});
		}

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
	public void onUpdate(int delta) {

	}
}
