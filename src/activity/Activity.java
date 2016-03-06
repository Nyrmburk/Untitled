package activity;

import java.awt.*;
import java.util.Stack;

import gui.Container;
import gui.GUIElement;
import gui.Panel;
import gui.PointerListener;
import input.*;
import input.Button;
import main.Engine;

public abstract class Activity {
	
	private static Stack<Activity> stack = new Stack<Activity>();
	private static boolean killCurrentActivity = false;
	
	protected Container view;
	
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
			this.setInputs(new SimpleInput("x_axis"), new SimpleInput("y_axis"));
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
	private static Button pointerButton = new Button("primary") {

		{
//			Engine.pointer.addInput(this, );
			Binding.delegate(this);
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
		public void onHold(int delta) {
		}
	};
	
	public abstract void onUpdate(int delta);
	
	public static void update(int delta) {

		if (killCurrentActivity) {
			killCurrentActivity();
			return;
		}

		Activity currentActivity = Activity.currentActivity();

		if (currentActivity != null) {

			currentActivity.onUpdate(delta);
//			currentActivity.getView().revalidate();
		}
	}
	
	public void setView(Container view) {

		this.view = view;
		view.revalidate();
	}

	public Container getView() {

		return view;
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

		this.view.setBounds(0, 0, size.width, size.height);
	}
}
