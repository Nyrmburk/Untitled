package gui.css;

import org.fit.cssbox.layout.ElementBox;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSElementBox extends CSSBox {

	private ElementBox elementBox;
	//simplify?
	//I think that this class should hold not ElementBox, but the data needed to render it.

	public CSSElementBox(ElementBox elementBox) {

		this.elementBox = elementBox;
		this.transparent = elementBox.getBgcolor().getAlpha() > 0;
	}

	public ElementBox getElementBox() {

		return elementBox;
	}
}
