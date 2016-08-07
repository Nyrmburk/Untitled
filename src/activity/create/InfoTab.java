package activity.create;

import gui.layout.GUIBoxLayout;
import gui.TextBox;

/**
 * Created by Nyrmburk on 8/6/2016.
 */
public class InfoTab extends Tab {

	private TextBox txtFps = new TextBox();
	private TextBox txtPosition = new TextBox();
	private TextBox txtHover = new TextBox();

	public InfoTab() {

		super("Info");

		setlayout(new GUIBoxLayout());

		txtFps.setText("fps");
		addChild(txtFps, 0);

		txtPosition.setText("world position");
		addChild(txtPosition, 0);

		txtHover.setText("current hover");
		addChild(txtHover, 0);
	}

	@Override
	public void update(float delta) {

		txtFps.setText(String.format("%6.2f fps", 1f / delta));
	}
}
