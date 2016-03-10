package main;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Nyrmburk on 2/22/2016.
 *
 * Resource is an abstract class for stuff like images, shaders, externalized strings, css, and more. It keeps a
 * reference count to the number of times it has been used. if the reference count stays at zero for a short amount of
 * time, then the resource will be released.
 */
public abstract class Resource {

	private int references = 0;

	public abstract String getName();

	public final void register() {

		references++;
		onRegister();
	}

	/**
	 * onRegister is called by the main thread (useful for graphics)
	 */
	protected abstract void onRegister();

	public final void release() {

		references--;
	}

	protected abstract void onRelease();

	public final int getReferenceCount() {

		return references;
	}

	public abstract void save(Path path);
	public abstract void load(Path Path) throws IOException;
}
