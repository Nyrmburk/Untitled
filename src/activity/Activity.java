package activity;

import java.awt.*;
import java.util.*;
import java.util.List;

import graphics.*;
import graphics.modelconverter.GUIConverter;
import graphics.modelconverter.ModelConverter;
import gui.*;

import gui.event.Event;
import gui.event.RedrawListener;
import main.Engine;
import matrix.Mat4;
import matrix.Projection;

public abstract class Activity {

	private static Stack<Activity> stack = new Stack<Activity>();
	private static boolean killCurrentActivity = false;

	private View view;

	private RenderContext renderContext;
	private ModelConverter<GUIElement> guiConverter = new GUIConverter();
	private Map<GUIElement, List<ModelLoader>> modelMap = new TreeMap<>();
	private InstanceAttributes guiModelAttributes = new InstanceAttributes();
	private ModelGroup guiModels = new ModelGroup();


	protected abstract void onCreate();
	protected abstract void onStart();
	protected abstract void onResume();
	protected abstract void onPause();
	protected abstract void onStop();
	protected abstract void onDestroy();

	public abstract void onUpdate(float delta);

	public static void update(float delta) {

		if (killCurrentActivity) {
			killCurrentActivity();
			return;
		}

		Activity currentActivity = Activity.currentActivity();

		if (currentActivity != null) {

			currentActivity.onUpdate(delta);

			boolean rebuild = !currentActivity.getView().isValid();
			currentActivity.getView().revalidate();

			// something odd is going on. somehow the view is updating without being invalid
			// I'll look into it later but in the meantime I have to disable this check.
//			if (rebuild)
//				currentActivity.rebuildModel();
		}
	}

	public void setView(View view) {

		this.view = view;
		view.addRedrawListener(new RedrawListener() {
			@Override
			public void actionPerformed(Event event) {

				rebuildModel();

				//TODO make the below code the actual code.
				// It mostly works but the problem is with order.
				// Once an element is updated, it is pushed to the top.

//				for (GUIElement element : guiElements) {
//
//					List<ModelLoader> models = modelMap.get(element);
//
//					if (models != null) {
//						for (ModelLoader model : models)
//							renderContext.getModelGroup().removeInstance(model, guiModelAttributes);
//					}
//
//					models = guiConverter.convert(element);
//
//					modelMap.put(element, models);
//
//					for (ModelLoader model : models)
//						renderContext.getModelGroup().addInstance(model, guiModelAttributes);
//				}


			}
		});
//		view.revalidate();
	}

	public View getView() {

		return view;
	}

	public void setRenderContext(RenderContext renderContext) {

		this.renderContext = renderContext;
	}

	public RenderContext getRenderContext() {

		return renderContext;
	}

	public static void createActivity(Activity activity) {

		if (killCurrentActivity)
			killCurrentActivity();

		if (!stack.isEmpty()) {

			Activity previousActivity = stack.peek();

			stopActivity(previousActivity);
		}

		stack.push(activity);

		activity.onCreate();
		startActivity(activity);

		if (activity.getView() == null) {

			stack.remove(activity);
			stopActivity(activity);
			try {
				throw new Exception("A view must be created");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (activity.getRenderContext() == null) {

			//2d ortho projection
			Mat4 projection = Projection.ortho(
					0, activity.getView().getWidth(),
					activity.getView().getHeight(), 0, -1, 1);
			activity.setRenderContext(new RenderContext(new GUICamera()));
		}

//		activity.rebuildModel();
	}

	public static void stopCurrentActivity() {

		killCurrentActivity = true;
	}

	private static void killCurrentActivity() {

		killCurrentActivity = false;
		Activity currentActivity = stack.pop();
		stopActivity(currentActivity);

		if (!stack.isEmpty()) {

			startActivity(stack.peek());
		}
	}

	public static Activity currentActivity() {

		Activity currentActivity;

		if (stack.isEmpty()) {

			currentActivity = null;
		} else {

			currentActivity = stack.peek();
		}

		return currentActivity;
	}

	private static void startActivity(Activity activity) {

		activity.onStart();
		activity.onResume();
	}

	private static void stopActivity(Activity activity) {

		activity.onPause();
		activity.onStop();
	}

	public void setSize(Dimension size) {

		view.setPreferredSize(size);
		view.invalidate();
	}

	public ModelConverter<GUIElement> getGUIConverter() {
		return guiConverter;
	}

	public void setGuiConverter(ModelConverter<GUIElement> guiConverter) {
		this.guiConverter = guiConverter;
	}

	private void rebuildModel() {

		// get the new model converter

		// clear the old models
		getRenderContext().getModelGroup().removeModelGroup(guiModels);
		guiModels.clear();

		// get the fresh models
		List<ModelLoader> models = guiConverter.convert(getView());

		// convert models into instances
		for (ModelLoader model : models)
			guiModels.addInstance(model, guiModelAttributes);

		// add the new models to the RenderContext.
		getRenderContext().getModelGroup().addModelGroup(guiModels);

	}
		{
		Engine.renderEngine.addViewportChangedListener(new RenderEngine.ViewportChangedListener() {
			@Override
			public void onActionPerformed() {
				setSize(renderEngine.getViewport().getSize());
			}
		});
	}
}
