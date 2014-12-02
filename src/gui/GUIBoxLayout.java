package gui;

import java.util.HashMap;

public class GUIBoxLayout implements GUILayoutManager {
	
	public static final int CENTER = 0;
	public static final int LEFT = CENTER + 1;
	public static final int RIGHT = LEFT + 1;
	
	HashMap<GUIElement, Integer> constraints = new HashMap<GUIElement, Integer>();
	
	@Override
	public void layout(Container parent) {
		
		int childY = parent.y;
		
		for (GUIElement child : parent.children) {
			
			int childX = parent.x;
			
			Integer constraint = constraints.get(child);
			if (constraint == null) constraint = LEFT;
			
			switch (constraint) {
			case CENTER:
				childX += (parent.width - child.width) / 2;
				break;
			case LEFT:
				break;
			case RIGHT:
				childX += parent.width - child.width;
				break;
			}
			
			child.setPosition(childX, childY);
			childY += child.height;
		}
	}

	@Override
	public void setConstraint(GUIElement element, Object Constraint) {

		constraints.put(element, (Integer) Constraint);
	}
}
