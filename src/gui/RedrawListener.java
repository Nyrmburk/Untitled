package gui;

import java.util.List;

/**
 * Created by Nyrmburk on 8/3/2016.
 */
public abstract class RedrawListener extends ActionListener {

	protected List<GUIElement> guiElements;

	@Override
	public void update(int delta) {
	}

	public void setElements(List<GUIElement> elements) {

		this.guiElements = elements;
	}
}
