package graphics;

import java.util.*;

// This class needs some help. I will probably fix it next commit.
public class RenderContext {

	private Set<InstancedModel> models = new HashSet<>();
	private entity.Camera camera; //TODO remove the 'entity.' part after graphics.camera is gone

	public RenderContext(entity.Camera camera) {

		setCamera(camera);
	}

	public void addInstance(ModelLoader model, InstanceAttributes attributes) {

		InstancedModel instancedModel = null;

		for (InstancedModel iModel : models) {

			if (iModel.model.equals(model)) {

				instancedModel = iModel;
				break;
			}
		}

		if (instancedModel == null) {
			instancedModel = new InstancedModel(model);
			models.add(instancedModel);
		}

		instancedModel.attributes.add(attributes);
	}

	public boolean removeInstance(InstanceAttributes instanceAttributes) {

		Iterator<InstancedModel> it = models.iterator();

		boolean removed = false;

		while (it.hasNext()) {

			InstancedModel iModel = it.next();

			removed = iModel.attributes.remove(instanceAttributes);

			if (removed) {

				if (iModel.attributes.isEmpty())
					it.remove();
				break;
			}
		}

		return removed;
	}

	public Set<InstancedModel> getIModels() {

		return models;
	}

	public entity.Camera getCamera() {
		return camera;
	}

	public void setCamera(entity.Camera camera) {

		this.camera = camera;
	}

	public class InstancedModel {

		public ModelLoader model;
		public List<InstanceAttributes> attributes = new LinkedList<>();

		InstancedModel(ModelLoader model) {

			this.model = model;
		}
	}
}
