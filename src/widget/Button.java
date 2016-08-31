package widget;

import entity.Entity;
import graphics.InstanceAttributes;
import graphics.ModelLoader;
import main.Resource;
import main.ResourceManager;

import java.util.List;

/**
 * Created by Nyrmburk on 8/29/2016.
 */
public class Button extends Widget {

	{
		ResourceManager.getResource("button",
				loadedResource -> setModel((ModelLoader) loadedResource),
				ModelLoader.class);
	}

	public Button() {
		super(0, 1);
	}

	@Override
	public void update(float delta) {

		receive();
	}

	private boolean isButtonDown() {

		return false;
	}

	@Override
	public void fire(List<Synapse> inputs, List<Synapse> outputs) {

		outputs.get(0).fire(isButtonDown() ? 0 : 1);
	}

	@Override
	public String getInputName(int index) {
		return "";
	}

	@Override
	public String getOutputName(int index) {
		return "button";
	}
}
