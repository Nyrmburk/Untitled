package gui.css;

import main.Resource;
import org.w3c.dom.Document;

import java.io.File;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSDocument implements Resource {

	String name;
	Document document;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Resource save(File file) {
		return null;
	}

	@Override
	public Resource load(File file) {
		return null;
	}
}
