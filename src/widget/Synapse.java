package widget;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 8/27/2016.
 */
public class Synapse {

	private float value;
	private Widget parent;
	private List<Widget> outputs = new LinkedList<>();

	public Synapse(Widget parent) {

		this.parent = parent;
	}

	public void fire(float value) {

		this.value = value;
		for (Widget widget : outputs)
			widget.receive();
	}

	public void connect(Widget widget) {
		outputs.add(widget);
	}

	public void disconnect(Widget widget) {
		outputs.remove(widget);
	}

	public float getValue() {
		return value;
	}

	public Widget getParent() {
		return parent;
	}
}
