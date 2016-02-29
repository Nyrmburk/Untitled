package gui.css;

import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.MediaSpec;
import graphics.GraphicsFont;
import graphics.TextureInterface;
import main.Engine;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.layout.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import script.Script;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSCanvas {

	private CSSDocument document;
	private Rectangle bounds;
	private Viewport viewport;

	private CSSComposite composite;

	private Map<Font, GraphicsFont> fonts = new HashMap<Font, GraphicsFont>();
	private Map<BufferedImage, TextureInterface> textures = new HashMap<BufferedImage, TextureInterface>();
	private Map<NamedNodeMap, Script> scripts = new HashMap<NamedNodeMap, Script>();

	Graphics2D g;
	DOMAnalyzer da;
	BoxFactory factory;

	public CSSCanvas(CSSDocument document, Rectangle bounds) {

		composite = new CSSComposite(bounds);
		this.document = document;
		da =  createDomAnalyzer(document.document);
		factory = createBoxFactory(da);
		g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

		setBounds(bounds);
	}

	public void layout() {

		factory.reset();

		VisualContext ctx = new VisualContext(null, factory);
		viewport = factory.createViewportTree(da.getRoot(), g, ctx, bounds.width, bounds.height);
		viewport.setVisibleRect(new Rectangle(bounds));
		viewport.initSubtree();

		viewport.doLayout(bounds.width, true, true);

//		unsure
		viewport.updateBounds(bounds.getSize());

		viewport.absolutePositions();

		populateComposite(viewport);
	}

	private BoxFactory createBoxFactory(DOMAnalyzer da){

		return new BoxFactory(da, null);
	}

	private DOMAnalyzer createDomAnalyzer(Document doc) {

		DOMAnalyzer da = new DOMAnalyzer(doc, null);
		da.setMediaSpec(new MediaSpec("screen"));//try and remove later
		da.attributesToStyles();
		da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT);
		da.getStyleSheets();

		return da;
	}

	private void populateComposite(Box root) {

		//TODO find a way to add scripts, buttons, iframes, and a new way to get images without contacting the web

		NamedNodeMap nnmap = root.getNode().getAttributes();

		Node node = null;
		if (nnmap != null)
			node = nnmap.getNamedItem("type");

		if (node != null) {
			switch (node.getNodeValue()) {

				case "button":
					composite.buttons.add(makeButton(root, nnmap));
					break;

				default:
					System.out.println("add " + nnmap.getNamedItem("type").getNodeValue() + " to populateComposite()");
			}
		}

		if (nnmap != null)
			node = nnmap.getNamedItem("onclick");
		if (node != null)
			System.out.println(node.getNodeValue());

		if (root instanceof TextBox) {

			TextBox textbox = (TextBox) root;

			composite.textBoxes.add(makeTextBox(textbox));
		} else if (root instanceof ElementBox) {

			ElementBox el = (ElementBox) root;

			if (el.getBgcolor() != null) {

				composite.elementBoxes.add(makeElementBox(el));

				if (el.getBackgroundImages() != null) {

					for (BackgroundImage bgImage : el.getBackgroundImages()) {

						BufferedImage image = bgImage.getBufferedImage();
						TextureInterface texture = textures.get(image);

						if (texture == null) {
							texture = Engine.renderEngine.getTextureFromImage(image);
							textures.put(image, texture);
						}

						CSSImageBox imageBox = new CSSImageBox(el.getBounds(), texture);
						imageBox.setBounds(el.getAbsoluteBounds());
						composite.imageBoxes.add(imageBox);
					}
				}

				//TODO find a way to get borders and outlines
			}

			for (int i = el.getStartChild(); i < el.getEndChild(); i++)
				populateComposite(el.getSubBox(i));
		}
	}

	private CSSButton makeButton(Box box, NamedNodeMap nnmap) {

		Script script = scripts.get(nnmap);

		if (script == null) {

			//TODO fix script and load the string here
			script = new Script();
			scripts.put(nnmap, script);
		}

		return new CSSButton(box, script);
	}

	private CSSTextBox makeTextBox(TextBox textbox) {

		VisualContext ctx = textbox.getVisualContext();

		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		List<CSSProperty.TextDecoration> decoration = ctx.getTextDecoration();

		for (CSSProperty.TextDecoration dec : decoration) {

			switch(dec.name()) {

				case "UNDERLINE":
					map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					break;

				default:
					System.out.println(dec.name());
					break;
			}
		}

		Font font = textbox.getVisualContext().getFont();
		font = font.deriveFont(map);
		GraphicsFont gFont = fonts.get(font);

		if (gFont == null) {

			gFont = new GraphicsFont(font);
			fonts.put(font, gFont);
		}

		return new CSSTextBox(textbox, gFont);
	}

	private CSSElementBox makeElementBox(ElementBox el) {

		return new CSSElementBox(el);
	}

	public CSSComposite getComposite() {

		return composite;
	}

	public CSSDocument getDocument() {

		return document;
	}

	public void setBounds(Rectangle bounds) {

		composite.clear();
		composite.setBounds(bounds);
		this.bounds = bounds;
		layout();
	}

	public Rectangle getBounds() {

		return bounds;
	}
}
