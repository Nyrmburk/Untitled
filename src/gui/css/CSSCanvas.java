package gui.css;

import cz.vutbr.web.css.MediaSpec;
import graphics.TextureInterface;
import main.Engine;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.layout.*;

import java.awt.*;
import java.awt.image.BufferedImage;

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

		DOMAnalyzer da = new DOMAnalyzer(document.document, null);
		da.setMediaSpec(new MediaSpec("screen"));//try and remove later
		da.attributesToStyles();
		da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.getStyleSheets();

		//TODO figure out boxfactory's arguments
		BoxFactory factory = new BoxFactory(da, null);
//		factory.setConfig(null);
		factory.reset();

		VisualContext ctx = new VisualContext(null, factory);
		BufferedImage graphicsDonor = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		viewport = factory.createViewportTree(da.getRoot(), graphicsDonor.createGraphics(), ctx, bounds.width, bounds.height);
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

			if (el.getBackgroundImages() != null) {

				for (BackgroundImage bgImage : el.getBackgroundImages()) {

					TextureInterface texture = Engine.renderEngine.getTextureFromImage(bgImage.getBufferedImage());
					CSSImageBox imageBox = new CSSImageBox(el.getBounds(), texture);
					imageBox.setBounds(el.getAbsoluteBounds());
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
