package activity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import graphics.Texture;
import gui.*;
import main.Engine;
import main.ResourceManager;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate() {

		View view = new View(Engine.renderEngine);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.WHITE);
		
		Panel image = new Panel();
		ResourceManager.getResource(
				"untitled.png",
				loadedResource -> image.setImage((Texture) loadedResource),
				Texture.class);
		view.addChild(image);
		
		Panel aligner = new Panel();
		aligner.setlayout(new GUIBoxLayout());
		view.addChild(aligner, 0.15);

		Button play = new Button();
		play.setText("Play");
		aligner.addChild(play, 0);

		Button create = new Button();
		create.setText("Create");
		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand().equals(Button.CLICKED))
					create();
			}
		});
		aligner.addChild(create, 0);

		Button options = new Button();
		options.setText("Options");
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand().equals(Button.CLICKED))
					createActivity(new BindingActivity());
			}
		});
		aligner.addChild(options, 0);

		Button quit = new Button();
		quit.setText("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand().equals(Button.CLICKED))
					Engine.close();
			}
		});
		aligner.addChild(quit, 0);
		
		setView(view);
		
//		RenderContext context = new RenderContext();
//		context.addModel(model, attributes);
//		Engine.renderEngine.setContext(context);
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

	}
	
	private void create() {
		
		System.out.println("opening the 'create' activity");
		createActivity(new CreateActivity());
	}
}
