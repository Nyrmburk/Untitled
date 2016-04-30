package gui;

import java.awt.*;

/**
 * Created by Nyrmburk on 4/30/2016.
 * <p>
 * For listeners: First, addActionListener is caled on the GUIElement in question. The ActionListener itself manages the events, not the component. After it detects an event, it calls the actionPerformed method with the action in question. The new ActionEvent tells the class it is in (to make sure it is the right type of event) and has an object specific to the event. In the case of a mouse listener, the object would return info on the button pressed, location of mouse, etc. This approach is extensible and easy to implement. It also works on all components.
 * <p>
 * Steps to implement
 * remove implementation from Activity
 * refactor PointerListener into a separate object that handles the listening itself.
 * refactor Component so that it no longer accepts ActionListener
 * refactor Component so that it no longer delegates ActionEvents
 */
public abstract class PointerListener2 implements ActionListener {

	private Point pointerLocation;

	@Override
	public void update(int delta) {
	}
}
