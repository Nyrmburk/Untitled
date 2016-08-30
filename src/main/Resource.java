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
	private boolean modified = false;
	private ResourceUser userData;

	public abstract String getName();

	public ResourceUser getUserData() {

		if (modified && userData != null) {
			userData.onModify();
			modified = false;
		}
		return userData;
	}

	public void setUserData(ResourceUser userData) {

		if (this.userData != null)
			this.userData.onRelease();
		this.userData = userData;
	}

	public final void register() {
		references++;
	}

	public final void release() {

		references--;
		if (references <= 0 && userData != null)
			userData.onRelease();
	}

	protected final void onModify() {
		modified = true;
	}

	public final int getReferenceCount() {
		return references;
	}

	public abstract void save(Path path) throws IOException;
	public abstract void load(Path path) throws IOException;
}
