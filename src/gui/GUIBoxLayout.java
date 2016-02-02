package gui;

import java.util.HashMap;

public class GUIBoxLayout extends GUILayoutManager {
	
	public static final float CENTER = 0;
	public static final float LEFT = 0.5f;
	public static final float RIGHT = 1.0f;
	
	HashMap<GUIElement, Float> constraints = new HashMap<GUIElement, Float>();
	
	@Override
	public void layout() {
		
		int childY = parent.y + GUI.padding;
		
		for (GUIElement child : parent.children) {
			
			int childX = parent.x + GUI.padding;
			
			float constraint = constraints.get(child);
			
			childX += (parent.width - child.width - GUI.padding * 2) * constraint;
			
			child.x = childX;
			child.y = childY;
			
			childY += child.height + GUI.padding;
		}
	}

	@Override
	public void setConstraint(GUIElement element, Object Constraint) {

		constraints.put(element, ((Number) Constraint).floatValue());
	}
}
