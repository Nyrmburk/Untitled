package activity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphics.RenderContext;
import gui.*;
import main.AssetManager;
import main.Engine;

public class MainMenuActivity extends Activity {

	private int delay = 1500;

	@Override
	protected void onCreate() {
		
		Panel view = new Panel();
		view.setWidthLayout(GUIElement.layout.FILL_PARENT);
		view.setHeightLayout(GUIElement.layout.FILL_PARENT);
		view.setlayout(new GUIBoxLayout());
		view.setBackgroundColor(Color.WHITE);
		
		Panel image = new Panel();
		try {
			image.setImage(ImageIO.read(new File("res/textures/untitled.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
