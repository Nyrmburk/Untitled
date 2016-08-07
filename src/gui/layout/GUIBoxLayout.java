package gui.layout;

import gui.Container;
import gui.GUIElement;
import gui.GUILayoutManager;

import java.awt.*;
import java.util.HashMap;

public class GUIBoxLayout extends GUILayoutManager {
	
	public static final float CENTER = 0;
	public static final float LEFT = 0.5f;
	public static final float RIGHT = 1.0f;

	Dimension prefSize;

	HashMap<GUIElement, Float> constraints = new HashMap<GUIElement, Float>();
	
	@Override
	public void layout() {

		Container parent = getParent();
		int childY = parent.getY() + parent.getInsets().top;
		int maxX = 0;

		for (GUIElement child : parent.getChildren()) {
			
			int childX = parent.getX() + parent.getInsets().left;
			
			float constraint = constraints.get(child);
			
			childX += (parent.getWidth() - child.getWidth()
					- parent.getInsets().left - parent.getInsets().right) * constraint;

			if (childX + child.getWidth() > maxX)
				maxX = childX + child.getWidth();

			child.setPosition(childX, childY);

			childY += child.getHeight() + parent.getInsets().bottom;
		}

		prefSize = new Dimension(maxX  - parent.getX(), childY - parent.getY());
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {

		float constraintVal = 0.5f;

		if (constraint != null)
			constraintVal = ((Number) constraint).floatValue();

		constraints.put(element, constraintVal);
	}

	@Override
	public Dimension getPreferredSize() {

		Dimension size = prefSize;
		if (size == null)
			size = getParent().getSize();
		return size;
	}
}
