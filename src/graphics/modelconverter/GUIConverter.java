package graphics.modelconverter;

import graphics.ModelLoader;
import gui.ContextBox;

/**
 * Created by Nyrmburk on 5/3/2016.
 */
public class GUIConverter implements ModelConverter<ContextBox> {

	public ModelLoader convert(ContextBox contextBox) {

		//TODO remove renderGU() in RenderEngine and replace it with this. I might have to do some work to make it
		// easy to replace with another model generator.
		return null;
	}
}
