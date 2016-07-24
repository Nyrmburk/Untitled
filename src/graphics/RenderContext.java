package graphics;

public class RenderContext {

	private Camera camera;
	private ModelGroup modelGroup = new ModelGroup();

	// graphics properties such as disable depth buffer, etc.
	// lights

	public RenderContext(Camera camera) {

		setCamera(camera);
	}

	public ModelGroup getModelGroup() {

		return modelGroup;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {

		this.camera = camera;
	}
}
