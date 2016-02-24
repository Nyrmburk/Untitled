package graphics;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.GUIElement;

public class UIRenderContext {

	//images
	List<TextureInterface> textures;
	
	//size and position of quads
	List<Rectangle> quads;

	Map<GUIElement, Integer> indexMap;
	
	//iterate through indices to get image and quad and render to screen
	//if done properly, the indices may be implied
	
	public UIRenderContext() {
		
		textures = new ArrayList<TextureInterface>();
		quads = new ArrayList<Rectangle>();
		indexMap = new HashMap<GUIElement, Integer>();
	}
	
	public void putElement(GUIElement element, TextureInterface texture) {

		if (texture == null)
			return;

		Integer index = indexMap.get(element);
		if (index == null) {
			quads.add(element.getBounds());
			textures.add(texture);
			index = textures.size() - 1;
			indexMap.put(element, index);
		} else {

			quads.set(index, element.getBounds());
			textures.set(index, texture).release();
		}
	}
	
	//damn this opens a can of worms
	public void removeElement(GUIElement element) {
		
		Integer index = indexMap.remove(element);
		if (index == null)
			return;

		textures.remove(index);
		quads.remove(index);

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
		quads.clear();
		indexMap.clear();
	}
}
