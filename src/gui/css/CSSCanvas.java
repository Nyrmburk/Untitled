package gui.css;

import graphics.TextureInterface;
import main.Engine;
import org.fit.cssbox.layout.*;

import java.awt.*;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSCanvas {

	private CSSDocument document;
	private Dimension bounds;
	private Viewport viewport;

	private Composite composite = new Composite();

	//TODO integrate CSSDocuemnt
	public CSSCanvas(CSSDocument document, Dimension bounds) {

		this.document = document;
		getBounds(bounds);
	}

	public void layout() {

		//TODO figure out boxfactory's arguments
		BoxFactory factory = new BoxFactory(null, null);
		factory.setConfig(null);
		factory.reset();

		VisualContext ctx = new VisualContext(null, factory);
		viewport = factory.createViewportTree(null, null, ctx, bounds.width, bounds.height);
		viewport.setVisibleRect(new Rectangle(bounds));
		viewport.initSubtree();

		viewport.doLayout(bounds.width, true, true);

		//unsure
		viewport.updateBounds(bounds);

		viewport.absolutePositions();

		populateComposite(viewport);
	}

	private void populateComposite(Box root)
	{
		if (root instanceof TextBox)
		{
			TextBox text = (TextBox) root;

			composite.textBoxes.add(new CSSTextBox(text));
		}
		else if (root instanceof ElementBox)
		{
			ElementBox el = (ElementBox) root;

			composite.elementBoxes.add(new CSSElementBox(el));

			if (!el.getBackgroundImages().isEmpty()) {

				for (BackgroundImage bgImage : el.getBackgroundImages()) {

					TextureInterface texture = Engine.renderEngine.getTextureFromImage(bgImage.getBufferedImage());
					CSSImageBox imageBox = new CSSImageBox(texture);
					imageBox.setBounds(el.getBounds());
					composite.imageBoxes.add(imageBox);
				}
			}

			//TODO find a way to get borders and outlines

			for (int i = el.getStartChild(); i < el.getEndChild(); i++)
				populateComposite(el.getSubBox(i));
		}
	}

	public Composite getComposite() {

		return composite;
	}

	public CSSDocument getDocument() {

		return document;
	}

	public void getBounds(Dimension bounds) {

		this.bounds = bounds;
		layout();
	}

	public Dimension getBounds() {

		return bounds;
	}
}
