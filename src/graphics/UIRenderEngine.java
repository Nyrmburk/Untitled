package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import gui.*;
import main.Engine;

public abstract class UIRenderEngine {

	// things to consider
	// are we rendering one by one?
	// is it ordered by depth?
	// should the type be compiled into a byte so every 5 ints is a thing?
	// is the performance gain worth overdoing it?
	// possibly keep outdated rendering for individual ui elements? it sounds
	// crazy but it works well enough.
	// the downside to that is that the ui style can never be changed
	// if the ui renderer is a separate class, it will not be always compatible
	// with the current render engine i.e. directx
	// one workaround is to render all the elements into a texture and render
	// the textures onto quads.
	// that does seem rather elegant and will work cross-platform and is
	// changeable.
	// it will also be really easy to implement in the render engine because it
	// is just rendering images.
	// I suggest 3 lists, one for all the images of the element (buttons change
	// images), one for the
	// locations of the quads themselves, and another for identifying the index
	// of the quad to the image.
	// it sounds easy enough. lets go!

	// update:
	// rendering the element to a bufferedImage is slow as balls. Either have
	// the render engine implement the ui rendering itself, or provide an
	// interface to draw it natively. Having the render engine implement it
	// means no cross engine compatibility and providing an interface for the
	// drawing of elements natively is going to be tricky. Once the ui has been
	// rendered to the bufferedimage it is plenty fast. Just don't resize or
	// cause any updates that require redrawing of the element.

	// update 2:
	// so further investigation has revealed that it is not the actually
	// rendering of the images, but rather the conversion from bufferedimage to
	// the native implementation. I did not write that code and I have no
	// source. There is no way to optimize. A workaround may be to roll my own
	// or composite all the images into a single pane and convert only one
	// image.

	// is this technically a 'render engine'? (pedantic)

	public UIRenderContext getUIRenderContext(Container view) {

		// java's awt button seems to tie into a "peer" implementation that
		// binds the button to the os code. I suggest possibly making a way
		// of returning an image(?) given a UIRenderEngine. I can't
		// currently think of a better way to do it. It is a bit poor
		// because it exposes some of the render engine to the actual
		// button, which is undesirable. The plus side to it is that it is
		// very lightweight and not much worse than java's implementation.

		UIRenderContext renderContext = new UIRenderContext();

		int index = 0;

		LinkedList<ArrayList<GUIElement>> elements = new LinkedList<ArrayList<GUIElement>>();
		elements.add(new ArrayList<GUIElement>());
		elements.peek().add(view);

		while (!elements.isEmpty()) {

			for (GUIElement element : elements.poll()) {

				TextureInterface texture = Engine.renderEngine.getTextureFromImage(element.render(this));
				renderContext.putElement(element, texture, index++);

				if (element instanceof Container) {

					elements.add(((Container) element).getChildren());
				}
			}
		}

		return renderContext;
	}

	public abstract BufferedImage renderPanel(Panel panel);

	public abstract BufferedImage renderTextBox(TextBox textBox);

	public abstract BufferedImage renderProgressBar(ProgressBar progressBar);

	public abstract BufferedImage renderButton(Button button);
}
