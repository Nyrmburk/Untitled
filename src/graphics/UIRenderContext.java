package graphics;

import java.awt.Rectangle;
import java.util.ArrayList;

import gui.GUIElement;

public class UIRenderContext {

	//images
	ArrayList<TextureInterface> textures;
	
	//size and position of quads
	ArrayList<Rectangle> coords;
	
	//map to tell where an element has its data
	//technically not a map anymore because the order must be constant, meaning a hashmap will no longer work
	ArrayList<GUIElement> map;
	
	//the rendering order of the elements
	ArrayList<Integer> indices;
	
	//iterate through indices to get image and quad and render to screen
	//if done properly, the indices may be implied
	
	public UIRenderContext() {
		
		textures = new ArrayList<TextureInterface>();
		coords = new ArrayList<Rectangle>();
		map = new ArrayList<GUIElement>();
		indices = new ArrayList<Integer>();
	}
	
	public void putElement(GUIElement element, TextureInterface texture, int index) {
		
		map.add(element);
		textures.add(texture);
		coords.add(element.getBounds());
		indices.add(index);
	}
	
	//damn this opens a can of worms
	public void removeElement(GUIElement element) {
		
		int index = map.indexOf(element);
		map.remove(index);
		textures.remove(index);
		coords.remove(index);
		indices.remove((Integer) index);
		
		for (Integer i : indices) {
			
			if (i > index)
				i--;
		}
	}
	
	public void clear() {
		
		for (TextureInterface texture : textures)
			texture.release();
		
		textures.clear();
		coords.clear();
		map.clear();
		indices.clear();
	}
}
