package graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.fit.cssbox.layout.ElementBox;

public class UIRenderContext2 {

	//images
	ArrayList<TextureInterface> textures;

	//TODO work needs to be done to support an atlas texture
	ArrayList<TextureInterface> fontAtlases;

	//colors of plain elements
	ArrayList<Color> colors;

	//size and position of quads
	ArrayList<Rectangle> textureQuads;
	ArrayList<Rectangle> textQuads;
	ArrayList<Rectangle> plainQuads;

	//hashmap holding the index of the box with the ElementBox as the key
	HashMap<ElementBox, Integer> indexMap;

	public UIRenderContext2() {

		//init data of ElementBoxes
		textures = new ArrayList<>();
		fontAtlases = new ArrayList<>();;
		colors = new ArrayList<>();

		//init quad locations of ElementBoxes
		textureQuads = new ArrayList<>();
		textQuads = new ArrayList<>();
		plainQuads = new ArrayList<>();
	}

	public void putTextureElement(ElementBox elementBox, TextureInterface texture) {

		Integer index = indexMap.get(elementBox);
		if (index == null) {
			textureQuads.add(elementBox.getAbsoluteBounds());
			textures.add(texture);
			index = textures.size();
			indexMap.put(elementBox, index);
		} else {

			textureQuads.set(index, elementBox.getAbsoluteBounds());
			textures.set(index, texture).release();
		}
	}

	public void putTextElement(ElementBox elementBox, TextureInterface texture) {

		Integer index = indexMap.get(elementBox);
		if (index == null) {
			textQuads.add(elementBox.getAbsoluteBounds());
			fontAtlases.add(texture);
			index = fontAtlases.size();
			indexMap.put(elementBox, index);
		} else {

			textQuads.set(index, elementBox.getAbsoluteBounds());
			fontAtlases.set(index, texture).release();
		}
	}

	public void putColorElement(ElementBox elementBox, Color color) {

		Integer index = indexMap.get(elementBox);
		if (index == null) {
			plainQuads.add(elementBox.getAbsoluteBounds());
			colors.add(color);
			index = colors.size();
			indexMap.put(elementBox, index);
		} else {

			plainQuads.set(index, elementBox.getAbsoluteBounds());
			colors.set(index, color);
		}
	}
	
	//damn this opens a can of worms
	public void removeElement(ElementBox elementBox) {
		
		int index = indexMap.get(elementBox);
		indexMap.remove(index);

//		for (Integer i : indices) {
//
//			if (i > index)
//				i--;
//		}
	}
	
	public void clear() {
		
		for (TextureInterface texture : textures)
			texture.release();

		for (TextureInterface texture : fontAtlases)
			texture.release();
		
		textures.clear();
		fontAtlases.clear();
		colors.clear();

		textureQuads.clear();
		textQuads.clear();
		plainQuads.clear();

		indexMap.clear();
	}
}
