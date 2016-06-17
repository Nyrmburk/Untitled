package gui;

import graphics.RenderEngine;

import java.awt.*;

/**
 * Created by Nyrmburk on 3/7/2016.
 */
public class View extends Panel {

	RenderEngine renderEngine;

	public View (RenderEngine renderEngine) {

		this.renderEngine = renderEngine;
	}

	@Override
	public Dimension getPreferredSize() {

		Dimension size = null;

		if (parent != null)
			size = parent.getPreferredSize();

		if (size == null)
			size = renderEngine.getViewport().getSize();

		return size;
	}
}
