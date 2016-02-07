package graphics;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import gui.GUIElement;

public class UIRenderContext {

	//images
	ArrayList<TextureInterface> textures;
	
	//size and position of quads
	ArrayList<Rectangle> coords;

	HashMap<GUIElement, Integer> indexMap;
	
	//iterate through indices to get image and quad and render to screen
	//if done properly, the indices may be implied
	
	public UIRenderContext() {
		
		textures = new ArrayList<TextureInterface>();
		coords = new ArrayList<Rectangle>();
		indexMap = new HashMap<GUIElement, Integer>();
	}
	
	public void putElement(GUIElement element, TextureInterface texture) {

		Integer index = indexMap.get(element);
		if (index == null) {
			coords.add(element.getBounds());
			textures.add(texture);
			index = textures.size();
			indexMap.put(element, index-1);
		} else {

			coords.set(index, element.getBounds());
			textures.set(index, texture).release();
		}
	}
	
	//damn this opens a can of worms
	public void removeElement(GUIElement element) {
		
		Integer index = indexMap.remove(element);
		if (index == null)
			return;

		textures.remove(index);
		coords.remove(index);

		//TODO figure out if this hack actually works
		for (Integer i : indexMap.values()) {
			
			if (i > index)
				i--;
		}
	}
	
	public void clear() {
		
		for (TextureInterface texture : textures)
			texture.release();
		
		textures.clear();
		coords.clear();
		indexMap.clear();
	}
}
