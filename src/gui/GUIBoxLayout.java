package gui;

import java.util.HashMap;

public class GUIBoxLayout extends GUILayoutManager {
	
	public static final float CENTER = 0;
	public static final float LEFT = 0.5f;
	public static final float RIGHT = 1.0f;

	HashMap<GUIElement, Float> constraints = new HashMap<GUIElement, Float>();
	
	@Override
	public void layout() {
		
		int childY = parent.getY() + parent.getInsets().top;

		for (GUIElement child : parent.children) {
			
			int childX = parent.getX() + parent.getInsets().left;
			
			float constraint = constraints.get(child);
			
			childX += (parent.getWidth() - child.getWidth()
					- parent.getInsets().left - parent.getInsets().right) * constraint;

			child.setPosition(childX, childY);

			childY += child.getHeight() + parent.getInsets().bottom;
		}
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {

		float constraintVal = 0.5f;

		if (constraint != null)
			constraintVal = ((Number) constraint).floatValue();

		constraints.put(element, constraintVal);
	}
}
