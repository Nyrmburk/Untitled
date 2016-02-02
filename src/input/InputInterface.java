package input;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Nyrmburk InputInterface is an interface for raw inputs. It collects
 *         raw values from some device such as a keyboard or joystick and puts
 *         the values into Inputs.
 */
public abstract class InputInterface implements Iterable<InputContext> {

	public static ArrayList<InputInterface> InputInterfaces = new ArrayList<InputInterface>();

	private ArrayList<InputContext> contexts = new ArrayList<InputContext>();

	public InputInterface() {

		InputInterfaces.add(this);
	}

	// provide some means to get hardware input and map it to Input2
	public abstract void getInputs();

	public final void update(int delta) {

		getInputs();

		for (InputContext context : contexts) {

			context.update(delta);
		}
	}

	// is this necessary?
	public void addInputContext(InputContext context) {

		contexts.add(context);
	}

	@Override
	public Iterator<InputContext> iterator() {

		return new ContextIterator(contexts.iterator());
	}

	public void save() {

	}

	public void load() {

	}

	public abstract String[] getInputNames();

	public abstract int getChangedInput();
}

class ContextIterator implements Iterator<InputContext> {

	Iterator<InputContext> iterator;

	ContextIterator(Iterator<InputContext> iterator) {

		ArrayList<InputContext> validContexts = new ArrayList<InputContext>();

		while (iterator.hasNext()) {

			InputContext context = iterator.next();

			if (context.isValid())
				validContexts.add(context);
		}
		this.iterator = validContexts.iterator();
	}

	@Override
	public boolean hasNext() {

		return iterator.hasNext();
	}

	@Override
	public InputContext next() {

		return iterator.next();
	}
}