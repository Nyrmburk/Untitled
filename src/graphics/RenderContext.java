package graphics;

import java.util.*;

// This class needs some help. I will probably fix it next commit.
public class RenderContext {

	private Set<InstancedModel> models = new HashSet<>();
	private Camera camera;

	public void addModel(ModelLoader model, InstanceAttributes attributes) {

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

	// FIXME: 5/9/2016
	// closed until I can figure wtf this is supposed to do.
//	public boolean removeModel(ModelLoader model) {
//
//		Iterator<InstancedModel> it = models.iterator();
//
//		boolean removed = false;
//
//		while (it.hasNext()) {
//
//			InstancedModel imodel = it.next();
//
//			if (imodel.removeInstance(model.getId()) != null) {
//
//				if (imodel.attributes.isEmpty()) {
//					it.remove();
//				}
//
//				removed = false;
//				break;
//			}
//		}
//
//		return removed;
//	}

	public Set<InstancedModel> getIModels() {

		return models;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {

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
