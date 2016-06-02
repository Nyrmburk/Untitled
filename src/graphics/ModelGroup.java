package graphics;

import java.util.*;

/**
 * Created by Nyrmburk on 5/21/2016.
 */
public class ModelGroup {

	private List<InstancedModel> models;

	public ModelGroup() {

		models = new LinkedList<>();
	}

	private InstancedModel getInstancedModel(ModelLoader model) {

		InstancedModel instancedModel = null;
		for (InstancedModel iModel : models) {

			if (iModel.model.equals(model)) {

				instancedModel = iModel;
				break;
			}
		}

		return instancedModel;
	}

	public void addInstance(ModelLoader model, Collection<InstanceAttributes> instanceAttributes) {

		InstancedModel instancedModel = getInstancedModel(model);

		if (instancedModel == null) {
			instancedModel = new InstancedModel(model);
			models.add(instancedModel);
		}

		instancedModel.attributes.addAll(instanceAttributes);
	}

	public void addInstance(ModelLoader model, InstanceAttributes... instanceAttributes) {
		
		addInstance(model, Arrays.asList(instanceAttributes));
	}

	public void removeInstance(ModelLoader model, Collection<InstanceAttributes> instanceAttributes) {

		InstancedModel iModel = getInstancedModel(model);

		iModel.attributes.removeAll(instanceAttributes);

		if (iModel.attributes.isEmpty())
			models.remove(iModel);
	}

	public void removeInstance(ModelLoader model, InstanceAttributes... instanceAttributes) {

		removeInstance(model, Arrays.asList(instanceAttributes));
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
