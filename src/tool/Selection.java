package tool;

import java.util.*;

/**
 * Created by Nyrmburk on 10/2/2016.
 */
public class Selection<T extends Selectable> extends HashSet<T> {

	public boolean add(T selectable) {

		selectable.onSelect();

		for (Selectable selection : this)
			selection.onSelectionChanged(this);

		return super.add(selectable);
	}

	public boolean remove(T selectable) {

		selectable.onDeselect();

		for (Selectable selection : this)
			selection.onSelectionChanged(this);

		return super.remove(selectable);
	}

	public void clear() {

		for (Selectable selection : this)
			selection.onDeselect();

		super.clear();
	}
}
