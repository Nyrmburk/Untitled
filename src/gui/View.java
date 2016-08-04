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
		setPreferredSize(renderEngine.getViewport().getSize());
	}
}
