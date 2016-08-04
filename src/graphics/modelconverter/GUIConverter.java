package graphics.modelconverter;

import graphics.GraphicsFont;
import graphics.ModelLoader;
import graphics.Text;
import graphics.Texture;
import gui.Container;
import gui.ContextBox;
import gui.GUIElement;

import java.util.*;

/**
 * Created by Nyrmburk on 5/3/2016.
 */
public class GUIConverter implements ModelConverter<GUIElement> {

	public List<ModelLoader> convert(GUIElement element) {

		//TODO remove renderGU() in RenderEngine and replace it with this. I might have to do some work to make it
		// easy to replace with another model generator.

		List<ModelLoader> modelList = new LinkedList<>();
		HashMap<GraphicsFont, List<Text.TextInstance>> text = new HashMap<>();

		elementConvert(modelList, text, element);

		Text[] texts = new Text[text.size()];
		int i = 0;
		for (Map.Entry<GraphicsFont, List<Text.TextInstance>> entry : text.entrySet()) {
			texts[i] = new Text();
			texts[i].font = entry.getKey();
			texts[i].instances = entry.getValue();
			i++;
		}

		TextConverter textConverter = new TextConverter();
		modelList.addAll(textConverter.convert(texts));

		return modelList;
	}

	private void elementConvert(List<ModelLoader> modelList, Map<GraphicsFont, List<Text.TextInstance>> text, GUIElement element) {

		if (!element.isVisible())
			return;

		boxConvert(modelList, text, element.getBox());

		//TODO remove once models are more recursive
		if (element instanceof Container) {

			for (GUIElement child : ((Container) element).getChildren())
				elementConvert(modelList, text, child);
		}
	}

	private void boxConvert(List<ModelLoader> modelList, Map<GraphicsFont, List<Text.TextInstance>> text, ContextBox box) {

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
		int color = 0;
		if (box.color != null) {
			// the bit twiddling is to convert argb to rgba
			color = box.color.getRGB() << 8 | box.color.getRGB() >>> 24;
		} else if (box.texture != null)
			color = 0xFFFFFFFF;

		model.color.add(color);
		model.color.add(color);
		model.color.add(color);
		model.color.add(color);

		//put texture and texture coords
		// 0,1-----1,1
		//  |       |
		//  |       |
		// 0,0-----1,0
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

		//put text
		if (box.texts != null) {
			for (Text textObject : box.texts) {

				List<Text.TextInstance> instances = text.get(textObject.font);

				if (instances == null)
					instances = new LinkedList<>();

				instances.addAll(textObject.instances);

				text.put(textObject.font, instances);
			}
		}

		modelList.add(model);

		if (box.subBoxes != null) {
			for (ContextBox subBox : box.subBoxes)
				boxConvert(modelList, text, subBox);
		}
	}
}
