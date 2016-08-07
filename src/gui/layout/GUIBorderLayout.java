package gui.layout;

import gui.*;
import gui.Container;

import java.awt.*;

public class GUIBorderLayout extends GUILayoutManager {

	public enum layouts {
		TOP, BOTTOM, LEFT, RIGHT, CENTER
	}

	GUIElement top = new gui.Panel();
	GUIElement bottom = new gui.Panel();
	GUIElement left = new gui.Panel();
	GUIElement right = new gui.Panel();
	GUIElement center = new gui.Panel();

	@Override
	public void layout() {

		Container parent = getParent();

		int widthLeft = parent.getWidth() - (left.getWidth() + right.getWidth());
		int heightLeft = parent.getHeight() - (top.getHeight() + bottom.getHeight());

		top.setBounds(parent.getX(), parent.getY(), parent.getWidth(), top.getHeight());

		bottom.setBounds(parent.getX(),
				parent.getY() + parent.getHeight() - bottom.getHeight(),
				parent.getWidth(), bottom.getHeight());

		left.setBounds(parent.getX(), parent.getY() + top.getHeight(), left.getWidth(), heightLeft);

		right.setBounds(parent.getX() + parent.getWidth() - right.getWidth(),
				parent.getY() + top.getHeight(), right.getWidth(), heightLeft);

		center.setBounds(parent.getX() + left.getWidth(),
				parent.getY() + top.getHeight(), widthLeft, heightLeft);
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

	@Override
	public Dimension getPreferredSize() {
		return getParent().getSize();
	}
}
