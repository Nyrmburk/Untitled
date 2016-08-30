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

	private static ModelLoader model;
	InstanceAttributes instance = new InstanceAttributes();

	Entity button;

	static {
		ResourceManager.getResource("button", new ResourceManager.AsyncLoad() {
			@Override
			public void onLoad(Resource loadedResource) {
				model = (ModelLoader) loadedResource;
			}
		}, ModelLoader.class);
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

	@Override
	public ModelLoader getModel() {
		return model;
	}

	@Override
	public InstanceAttributes getInstance() {
		return instance;
	}
}
