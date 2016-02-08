package activity;

import java.awt.Point;
import java.util.Stack;

import graphics.UIRenderContext;
import gui.Container;
import gui.GUIElement;
import gui.PointerListener;
import input.*;
import main.Engine;

public abstract class Activity {
	
	private static Stack<Activity> stack = new Stack<Activity>();
	private static boolean killCurrentActivity = false;
	
	protected Container view;
	private UIRenderContext context;
	
	protected abstract void onCreate();
	protected abstract void onStart();
	protected abstract void onResume();
	protected abstract void onPause();
	protected abstract void onStop();
	protected abstract void onDestroy();
	
	private static PointerListener previousListener;
	private static PointerListener currentListener;

	private InputContext inputContext;
	private static final String ACTIVITY_INPUT = "pointer_input";
	private static MultiInput pointerInput = new MultiInput(ACTIVITY_INPUT) {

		{
			this.setInputs(
					new PartInput(PointerInput.bindings.X_COORD.toString(), Engine.pointer),
					new PartInput (PointerInput.bindings.Y_COORD.toString(), Engine.pointer));
		}

		@Override
		public void onUpdate(Input[] inputs, int delta) {

			int x = (int) inputs[0].getValue();
			int y = (int) inputs[1].getValue();

			Point pointer = new Point(x, y);

			Activity currentActivity = Activity.currentActivity();
			GUIElement element = currentActivity.getView().getMouseOver(pointer);

			if (previousListener != null && previousListener != element) {

				previousListener.onExit();
				previousListener = null;
			}

			if (element != null && element instanceof PointerListener) {
				// System.out.println(element);
				currentListener = (PointerListener) element;

				if (previousListener != currentListener) {

					currentListener.onEnter();
					previousListener = currentListener;
				}
			} else {

				currentListener = null;
			}
		}
	};
	private static Button pointerButton = new Button(PointerInput.bindings.BUTTON_0.toString()) {

		{
			Engine.pointer.addInput(this);
		}

		@Override
		public void onPress() {
			if (currentListener != null)
				currentListener.onPress();
		}

		@Override
		public void onRelease() {
			if (currentListener != null)
				currentListener.onRelease();
		}

		@Override
		public void onHold() {
		}

		@Override
		public void onUpdate(int delta) {
		}
	};
	
	public abstract void onUpdate(int delta);
	
	public static void update(int delta) {

		if (killCurrentActivity) {
			killCurrentActivity();
			return;
		}

		Activity currentActivity = Activity.currentActivity();

		currentActivity.onUpdate(delta);
		currentActivity.getView().revalidate();
	}
	
	public void setView(Container view) {
		
		if (context != null) {
			context.clear();
			context = null;
		}

		context = Engine.UIRenderEngine.getUIRenderContext(view);
		this.view = view;
		view.revalidate();
	}
	
	//me no likey
	public Container getView() {

		return view;
	}
	
	//me no likey either
	public UIRenderContext getRenderContext() {

		return context;
	}
	
	public static void createActivity(Activity activity) {

		if (killCurrentActivity)
			killCurrentActivity();

		if (!stack.isEmpty()) {
			
			Activity previousActivity = stack.peek();
			
			stopActivity(previousActivity);
		}
		
		stack.push(activity);

		activity.inputContext = new InputContext();
		activity.inputContext.setAsCurrentContext();
		activity.inputContext.inputs.add(pointerInput);
		activity.inputContext.inputs.add(pointerButton);

		activity.onCreate();
		startActivity(activity);

		if (activity.view == null) {

			stack.remove(activity);
			stopActivity(activity);
			try {
				throw new Exception("A view must be created");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		
		return stack.peek();
	}
	
	private static void startActivity(Activity activity) {

		activity.onStart();
		activity.onResume();
	}
	
	private static void stopActivity(Activity activity) {
		
		activity.onPause();
		activity.onStop();
	}
}
