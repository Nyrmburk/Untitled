package gui.css;

import org.fit.cssbox.layout.Box;
import script.Script;

/**
 * Created by Nyrmburk on 2/28/2016.
 */
public class CSSButton extends CSSBox {

	Script script;

	public CSSButton(Box box, Script script) {

		super(box);
		this.script = script;
	}
}
