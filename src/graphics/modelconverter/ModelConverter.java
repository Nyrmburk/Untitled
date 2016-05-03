package graphics.modelconverter;

import graphics.ModelLoader;

/**
 * Created by Nyrmburk on 5/3/2016.
 *
 * The purpose of this class is to create a way for things like lines, gui elements, and other things to be converted
 * into a model that can be drawn by the render engine. It allows for cross-compatibility between different render
 * engines and prevents clutter in RenderEngine. Another advantage is that individual render engines can supply their
 * own converters for simple things like particle effects and line drawing. A cross-renderer particle engine might be
 * better suited to a specialized shader and therefore implemented in the render engine.
 */
public interface ModelConverter<T> {

	ModelLoader convert(T element);
}
