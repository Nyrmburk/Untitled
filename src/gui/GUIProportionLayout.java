package gui;

import java.awt.*;

/**
 * Created by Nyrmburk on 4/28/2016.
 */
public class GUIProportionLayout extends GUILayoutManager {

	public enum constraint {

		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
	}

	@Override
	public void layout() {

	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {

	}

	@Override
	public Dimension getPreferredSize() {
		return null;
	}

	public class Anchor {

		GUIElement start;
		constraint startSide;

		GUIElement move;
		constraint moveSide;

		GUIElement end;
		constraint endSide;
	}
}
