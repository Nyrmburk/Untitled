package gui.css;

import org.fit.cssbox.layout.Box;

import java.awt.*;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSBox extends Rectangle {

	boolean transparent;

	public CSSBox () {
	}

	public CSSBox (Box box) {

		super(box.getAbsoluteBounds());
	}
}
