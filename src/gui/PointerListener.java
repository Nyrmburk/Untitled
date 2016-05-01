package gui;

import activity.Activity;
import input.*;
import input.InputContext;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by Nyrmburk on 4/30/2016.
 * <p>
 * For listeners: First, addActionListener is caled on the GUIElement in question. The ActionListener itself manages
 * the events, not the component. After it detects an event, it calls the actionPerformed method with the action in
 * question. The new ActionEvent tells the class it is in (to make sure it is the right type of event) and has an object
 * specific to the event. In the case of a mouse listener, the object would return info on the button pressed, location
 * of mouse, etc. This approach is extensible and easy to implement. It also works on all components.
 * <p>
 * Steps to implement
 * remove implementation from Activity
 * refactor PointerListener into a separate object that handles the listening itself.
 * refactor Component so that it no longer accepts ActionListener
 * refactor Component so that it no longer delegates ActionEvents
 */
public abstract class PointerListener extends ActionListener {

	private static List<PointerListener> pointerListeners = new LinkedList<>();
	private static LinkedList<PointerListener> activeListeners = new LinkedList<>();
	private static LinkedList<PointerListener> previousListeners = new LinkedList<>();

	private static final String ACTIVITY_INPUT = "pointer_input";

	private static GUIElement previousElement;
	private static GUIElement currentElement;

	private static Point pointerLocation;

	private State currentState;

	public enum State {
		ENTER,
		EXIT,
		PRESS,
		HOLD,
		RELEASE,
		CLICK,
		MOVE,
		DRAG,
	}

	private static MultiInput pointerInput = new MultiInput(ACTIVITY_INPUT) {

		{
			this.setInputs(new SimpleInput("x_axis"), new SimpleInput("y_axis"));
		}

		@Override
		public void onUpdate(Input[] inputs, int delta) {

			int x = (int) inputs[0].getValue();
			int y = (int) inputs[1].getValue();

			pointerLocation = new Point(x, y);

			Activity currentActivity = Activity.currentActivity();
			GUIElement element = currentActivity.getView().getMouseOver(pointerLocation);
			previousListeners.clear();
			previousListeners.addAll(activeListeners);
			activeListeners.clear();

			for (PointerListener listener : pointerListeners) {
				if (listener.getParent() == element) {
					activeListeners.add(listener);
				}
			}

			if (previousElement != null && previousElement != element) {
				for (PointerListener listener : previousListeners) {
					listener.setCurrentState(State.EXIT);
					listener.actionPerformed();
				}
				previousElement = null;
				currentElement = null;
			}

			if (!activeListeners.isEmpty()) {
				currentElement = element;

				if (previousElement != currentElement) {

					for (PointerListener listener : activeListeners) {
						listener.setCurrentState(State.ENTER);
						listener.actionPerformed();
					}
					previousElement = currentElement;
				}
			}
		}
	};

	private static input.Button pointerButton = new input.Button("primary") {

		private GUIElement pressElement;

		{
//			Engine.pointer.addInput(this, );
			Binding.delegate(this);
		}

		@Override
		public void onPress() {
			if (currentElement != null) {
				for (PointerListener listener : activeListeners) {
					listener.setCurrentState(State.PRESS);
					listener.actionPerformed();
				}
			}
			pressElement = currentElement;
		}

		@Override
		public void onRelease() {
			if (currentElement != null) {
				for (PointerListener listener : activeListeners) {
					listener.setCurrentState(State.RELEASE);
					listener.actionPerformed();
				}
			}
			if (pressElement == currentElement) {
				for (PointerListener listener : activeListeners) {
					listener.setCurrentState(State.CLICK);
					listener.actionPerformed();
				}
			}
		}

		@Override
		public void onHold(int delta) {
			if (currentElement != null) {
				for (PointerListener listener : activeListeners) {
					listener.setCurrentState(State.HOLD);
					listener.actionPerformed();
				}
			}
		}
	};


	static {
		InputContext inputContext = new InputContext();
		inputContext.setAsCurrentContext();
		inputContext.inputs.add(pointerInput);
		inputContext.inputs.add(pointerButton);
		System.out.println("Initialized");
	}

	{
		pointerListeners.add(this);
	}

	@Override
	public void update(int delta) {
	}

	public Point getPointerLocation() {

		return pointerLocation;
	}

	public State getCurrentState() {
		return currentState;
	}

	private void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
