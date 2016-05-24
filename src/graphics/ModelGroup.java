package graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 5/21/2016.
 */
public class ModelGroup {

	private List<InstancedModel> models;

	public ModelGroup() {

		models = new ArrayList<>();
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

	public void addModelGroup(ModelGroup modelGroup) {

		for (InstancedModel childIModel : modelGroup.getIModels()) {
			InstancedModel iModel = null;

			for (InstancedModel parentIModel : this.getIModels()) {

				if (childIModel.model == parentIModel.model) {

					iModel = parentIModel;
					break;
				}
			}

			if (iModel == null) {
				iModel = new InstancedModel(childIModel.model);
				models.add(iModel);
			}

			for (InstanceAttributes iAttributes : childIModel.attributes)
				iModel.attributes.add(iAttributes);
		}
	}

	public void removeModelGroup(ModelGroup modelGroup) {

		for (InstancedModel iModel : modelGroup.getIModels()) {

			for (InstancedModel parentIModel : this.getIModels()) {

				if (iModel.model == parentIModel.model) {

					for (InstanceAttributes iAttributes : iModel.attributes)
						parentIModel.attributes.remove(iAttributes);

					if (parentIModel.attributes.isEmpty())
						models.remove(parentIModel);

					break;
				}
			}
		}
	}

	public List<InstancedModel> getIModels() {

		return models;
	}

	public void clear() {

		models.clear();
	}

	public static class InstancedModel {

		public ModelLoader model;
		public List<InstanceAttributes> attributes = new LinkedList<>();

		InstancedModel(ModelLoader model) {

			this.model = model;
		}
	}
}
