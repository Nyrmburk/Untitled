package graphics.modelconverter;

import graphics.ModelLoader;
import graphics.Texture;
import gui.Container;
import gui.ContextBox;
import gui.GUIElement;

/**
 * Created by Nyrmburk on 5/3/2016.
 */
public class GUIConverter implements ModelConverter<GUIElement> {

	private int totalIndex;
	public ModelLoader convert(GUIElement element) {

		//TODO remove renderGU() in RenderEngine and replace it with this. I might have to do some work to make it
		// easy to replace with another model generator.

		totalIndex = 0;

		ModelLoader model = new ModelLoader();

		elementConvert(model, element);

		return model;
	}

	private void elementConvert(ModelLoader model, GUIElement element) {

		boxConvert(model, element.getBox());

		if (element instanceof Container) {

			for (GUIElement child : ((Container) element).getChildren())
				elementConvert(model, child);
		}
	}

	private void boxConvert(ModelLoader model, ContextBox box) {

		//put vertices
		//    0--------3
		//    |        |
		//    1--------2
		model.vertices.put((float) box.x, box.y, 0);
		model.vertices.put((float) box.x, box.y + box.height, 0);
		model.vertices.put((float) box.x + box.width, box.y + box.height, 0);
		model.vertices.put((float) box.x + box.width, box.y, 0);

		//put colors
		//I'm not sure this will work yet
		if (box.color != null) {
			model.color.add(box.color.getRGB());
			model.color.add(box.color.getRGB());
			model.color.add(box.color.getRGB());
			model.color.add(box.color.getRGB());
		}

		// FIXME: 5/17/2016 A problem arises if multiple textures are present
		//put texture and texture coords
		Texture t = box.texture;
		if (t != null) {
			model.texture = t;
			model.textureCoords.put(0, 0);
			model.textureCoords.put(0, 1);
			model.textureCoords.put(1, 1);
			model.textureCoords.put(1, 0);
		}

		//put indices
		model.addFace(totalIndex++, totalIndex++, totalIndex++, totalIndex++);
	}
}
