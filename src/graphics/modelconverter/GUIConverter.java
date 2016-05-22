package graphics.modelconverter;

import graphics.ModelLoader;
import graphics.Texture;
import gui.Container;
import gui.ContextBox;
import gui.GUIElement;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 5/3/2016.
 */
public class GUIConverter implements ModelConverter<GUIElement> {

	public List<ModelLoader> convert(GUIElement element) {

		//TODO remove renderGU() in RenderEngine and replace it with this. I might have to do some work to make it
		// easy to replace with another model generator.

		List<ModelLoader> modelList = new LinkedList<>();

		elementConvert(modelList, element);

		return modelList;
	}

	private void elementConvert(List<ModelLoader> modelList, GUIElement element) {

		boxConvert(modelList, element.getBox());

		if (element instanceof Container) {

			for (GUIElement child : ((Container) element).getChildren())
				elementConvert(modelList, child);
		}
	}

	private void boxConvert(List<ModelLoader> modelList, ContextBox box) {

//		if (box.color == null && box.texture == null && box.texts == null)
//			return;

		ModelLoader model = new ModelLoader();

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
		int color = 0;
		if (box.color != null) {
			// the bit twiddling is to convert argb to rgba
			color = box.color.getRGB() << 8 | box.color.getRGB() >>> 24;
		}

		if (box.texture != null)
			color = 0xFF;
		
		model.color.add(color);
		model.color.add(color);
		model.color.add(color);
		model.color.add(color);

		//put texture and texture coords

		//0,1-----------1,1
		// |             |
		// |             |
		// |             |
		//0,0-----------1,0
		Texture t = box.texture;
		if (t != null) {
			model.texture = t;
			model.textureCoords.put(0f, t.getHeightRatio());
			model.textureCoords.put(0f, 0f);
			model.textureCoords.put(t.getWidthRatio(), 0f);
			model.textureCoords.put(t.getWidthRatio(), t.getHeightRatio());
		}

		//put indices
		model.addFace(0, 1, 2, 3);

		modelList.add(model);
	}
}
