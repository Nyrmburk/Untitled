package gui;

import java.util.HashMap;

public class GUIBoxLayout extends GUILayoutManager {
	
	public static final int CENTER = 0;
	public static final int LEFT = CENTER + 1;
	public static final int RIGHT = LEFT + 1;
	
	HashMap<GUIElement, Integer> constraints = new HashMap<GUIElement, Integer>();
	
	public GUIBoxLayout(Container parent) {
		super(parent);
	}
	
	@Override
	public void layout() {
		
		int childY = parent.y + GUI.padding;
		
		for (GUIElement child : parent.children) {
			
			int childX = parent.x + GUI.padding;
			
			Integer constraint = constraints.get(child);
			if (constraint == null) constraint = LEFT;
			
			switch (constraint) {
			case CENTER:
				childX += (parent.width - child.width) / 2 - GUI.padding;
				break;
			case LEFT:
				break;
			case RIGHT:
				childX += parent.width - child.width - GUI.padding * 2;
				break;
			}
			
			child.x = childX;
			child.y = childY;
			
			childY += child.height;
		}
	}

	@Override
	public void setConstraint(GUIElement element, Object Constraint) {

		constraints.put(element, (Integer) Constraint);
	}
}
