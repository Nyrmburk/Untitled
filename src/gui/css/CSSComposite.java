package gui.css;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSComposite {

	Rectangle clipping;

	public List<CSSTextBox> textBoxes = new ArrayList<CSSTextBox>();
	public List<CSSElementBox> elementBoxes = new ArrayList<CSSElementBox>();
	public List<CSSImageBox> imageBoxes = new ArrayList<CSSImageBox>();
	public List<CSSBorder> borders = new ArrayList<CSSBorder>();

	public CSSComposite(Rectangle clipping) {

		setBounds(clipping);
	}

	public void setBounds(Rectangle clipping) {

		this.clipping = clipping;
	}

	public Rectangle getBounds() {

		return clipping;
	}

	public void clear() {

		elementBoxes.clear();
		textBoxes.clear();
		imageBoxes.clear();
		borders.clear();
	}
}
