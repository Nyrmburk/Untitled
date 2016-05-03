package graphics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

public class RenderContext {

	private RenderEngine engine;
	public HashSet<InstancedModel> models = new HashSet<InstancedModel>();
	private Random random = new Random();

	/**
	 * Add a model to the render context. The context returns a random value
	 * that is used as ID for the lookup of removal.
	 * 
	 * @param model
	 * @param attributes
	 * @return
	 */
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

			if (engine != null)
				getEngine().addModel(instancedModel);
		}

		int id = random.nextInt();
		model.setId(id);

		instancedModel.addInstance(id, attributes);
	}

	/**
	 * Remove a specific model.
	 * 
	 * @param model
	 * @return
	 */
	public boolean removeModel(ModelLoader model) {

		Iterator<InstancedModel> it = models.iterator();

		boolean removed = false;

		while (it.hasNext()) {

			InstancedModel imodel = it.next();

			if (imodel.removeInstance(model.getId()) != null) {

				if (imodel.attributes.isEmpty()) {

					if (engine != null)
						getEngine().removeModel(imodel);
					it.remove();
				}

				removed = false;
				break;
			}
		}

		return removed;
	}

	public RenderEngine getEngine() {
		return engine;
	}

	public void setEngine(RenderEngine engine) {
		this.engine = engine;
	}

	public class InstancedModel {

		public ModelLoader model;
		public TreeMap<Integer, InstanceAttributes> attributes;

		InstancedModel(ModelLoader model) {

			this.model = model;
			attributes = new TreeMap<Integer, InstanceAttributes>();
		}

		public void addInstance(int id, InstanceAttributes attributes) {

			this.attributes.put(id, attributes);
		}

		public InstanceAttributes removeInstance(int id) {

			return this.attributes.remove(id);
		}
	}
}
