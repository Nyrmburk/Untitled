package gui;

public class GUIBorderLayout extends GUILayoutManager {

	public enum layouts {
		TOP, BOTTOM, LEFT, RIGHT, CENTER
	}

	GUIElement top = new Panel();
	GUIElement bottom = new Panel();
	GUIElement left = new Panel();
	GUIElement right = new Panel();
	GUIElement center = new Panel();

	@Override
	public void layout() {
		
		int widthLeft = parent.getWidth() - (left.getWidth() + right.getWidth());
		int heightLeft = parent.getHeight() - (top.getHeight() + bottom.getHeight());
		
		top.x = parent.getX();
		top.y = parent.getY();
		top.width = parent.getWidth();
		
		bottom.x = parent.getX();
		bottom.y = parent.getY() + parent.getHeight() - bottom.getHeight();
		bottom.width = parent.getWidth();
		
		left.x = parent.getX();
		left.y = parent.getY() + top.getHeight();
		left.height = heightLeft;
		
		right.x = parent.getX() + parent.getWidth() - right.getWidth();
		right.y = parent.getY() + top.getHeight();
		right.height = heightLeft;
		
		center.x = parent.getX() + left.getWidth();
		center.y = parent.getY() + top.getHeight();
		center.width = widthLeft;
		center.height = heightLeft;
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {

		switch ((layouts) constraint) {
		case TOP:
			top = element;
			break;
		case BOTTOM:
			bottom = element;
			break;
		case LEFT:
			left = element;
			break;
		case RIGHT:
			right = element;
			break;
		case CENTER:
			center = element;
			break;
		}
	}
}
