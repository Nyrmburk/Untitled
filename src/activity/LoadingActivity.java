package activity;

import gui.*;
import gui.Panel;

import java.awt.*;

public class LoadingActivity extends Activity {

	private int percentComplete = 0;
	String loadingMessage = "Loading... %d%%";
	TextBox loadingText;

	@Override
	protected void onCreate() {

		Panel view = new Panel();
		view.setWidthLayout(GUIElement.layout.FILL_PARENT);
		view.setHeightLayout(GUIElement.layout.FILL_PARENT);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.GRAY);

		loadingText = new TextBox();
		loadingText.setText(String.format(loadingMessage, percentComplete / 1000));
		view.addChild(loadingText);

		gui.Button play = new gui.Button();
		play.setText("Play");
		view.addChild(play);

		setView(view);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(int delta) {
		// TODO Auto-generated method stub
		percentComplete += delta;
//		loadingText.setText(String.format(loadingMessage, percentComplete / 1000));
		loadingText.setText("aloha.");
	}
}
