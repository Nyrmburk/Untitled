package activity;

import java.awt.Point;
import java.util.Stack;

import graphics.UIRenderContext;
import gui.Container;
import gui.GUIElement;
import gui.PointerListener;
import main.Engine;

public abstract class Activity {
	
	private static Stack<Activity> stack = new Stack<Activity>();
	
	protected Container view;
	private UIRenderContext context;
	
	protected abstract void onCreate();
	protected abstract void onStart();
	protected abstract void onResume();
	protected abstract void onPause();
	protected abstract void onStop();
	protected abstract void onDestroy();
	
	private static PointerListener previousListener;
	
	public abstract void onUpdate(int delta);
	
	public static void update(int delta) {
		
		int x = (int) Engine.pointer.getX().getValue();
		int y = (int) Engine.pointer.getY().getValue();

		Point pointer = new Point(x, y);

		Activity currentActivity = Activity.currentActivity();
		GUIElement element = currentActivity.getView().getMouseOver(pointer);

		if (previousListener != null && previousListener != element) {
			
			System.out.println("exiting  " + previousListener);
			previousListener.onExit();
			previousListener = null;
		}
		
		if (element != null && element instanceof PointerListener) {
			// System.out.println(element);
			PointerListener listener = (PointerListener) element;

			if (previousListener != listener) {

				System.out.println("entering " + listener);
				listener.onEnter();
				previousListener = listener;
			}
			
//			if (Engine.pointer.a)
		}

		currentActivity.onUpdate(delta);
		currentActivity.getView().revalidate();
	}
	
	public void setView(Container view) {
		
		this.view = view;
		view.revalidate();
		if (context != null) {
			context.clear();
			context = null;
		}
		context = Engine.UIRenderEngine.getUIRenderContext(view);
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
		
		if (!stack.isEmpty()) {
			
			Activity previousActivity = stack.peek();
			
			stopActivity(previousActivity);
		}
		
		stack.push(activity);
		
		activity.onCreate();
		startActivity(activity);
	}
	
	public static void stopCurrentActivity() {
		
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
