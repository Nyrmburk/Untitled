package graphics;

public class RenderContext {

	private entity.Camera camera; //TODO remove the 'entity.' part after graphics.camera is gone
	private ModelGroup modelGroup = new ModelGroup();

	public RenderContext(entity.Camera camera) {

		setCamera(camera);
	}

	public ModelGroup getModelGroup() {

		return modelGroup;
	}

	public entity.Camera getCamera() {
		return camera;
	}

	public void setCamera(entity.Camera camera) {

		this.camera = camera;
	}
}
