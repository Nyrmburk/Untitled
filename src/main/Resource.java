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
	private RegisterListener registerListener;
	private ReleaseListener releaseListener;
	private Object userData;

	public abstract String getName();

	public Object getUserData() {

		return userData;
	}

	public void setUserData(Object userData) {

		this.userData = userData;
	}

	public final void register() {

		references++;

		if (registerListener != null)
			registerListener.onRegister();
	}

	public final void release() {

		references--;
		if (references <= 0 && releaseListener != null)
			releaseListener.onRelease();
	}

	public final int getReferenceCount() {

		return references;
	}

	public abstract void save(Path path) throws IOException;
	public abstract void load(Path path) throws IOException;

	public void setRegisterListener(RegisterListener listener) {

		this.registerListener = listener;
	}

	public void setReleaseListener(ReleaseListener listener) {

		this.releaseListener = listener;
	}

	public interface RegisterListener {

		/**
		 * onRegister is called by the main thread (useful for graphics)
		 */
		void onRegister();
	}

	public interface ReleaseListener {

		void onRelease();
	}
}
