package gui.css;

import org.fit.cssbox.layout.Box;
import org.fit.cssbox.layout.VisualContext;

import java.awt.*;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSBox extends Rectangle {

	boolean transparent;

	Color color;

	public CSSBox () {
	}

	public CSSBox (Box box) {

		super(box.getAbsoluteBounds());
		VisualContext context = box.getVisualContext();
		color = context.getColor();
	}

	public Color getColor() {

		return color;
	}
}
