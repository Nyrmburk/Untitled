package activity;

import java.awt.Color;

import activity.create.NewCreateActivity;
import graphics.Texture;
import gui.*;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import main.Engine;
import main.ResourceManager;

public class MainMenuActivity extends Activity {

	private boolean secret;
	float hue;

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
		create.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					create();
			}
		});
		aligner.addChild(create, 0);

		Button newCreate = new Button();
		newCreate.setText("New Create");
		newCreate.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					createActivity(new NewCreateActivity());
			}
		});
		aligner.addChild(newCreate, 0);

		Button options = new Button();
		options.setText("Options");
		options.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					createActivity(new BindingActivity());
			}
		});
		aligner.addChild(options, 0);

		Button quit = new Button();
		quit.setText("Quit");
		quit.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					Engine.close();
			}
		});
		aligner.addChild(quit, 0);

		Button secret = new Button();
		secret.setText("      ");
		secret.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent e) {
				if (e.state == State.CLICK)
					yaySecret();
			}
		});
		view.addChild(secret);
		
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
	public void onUpdate(float delta) {

		if (secret) {
			hue += delta;
			getView().setBackgroundColor(Color.getHSBColor(hue, 1, 1));
		}
	}
	
	private void create() {
		
		System.out.println("opening the 'create' activity");
		createActivity(new CreateActivity());
	}

	private void yaySecret() {

		secret = !secret;
	}
}
