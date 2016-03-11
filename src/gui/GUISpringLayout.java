package gui;

import java.awt.*;

/**
 * Created by Nyrmburk on 3/10/2016.
 */
public class GUISpringLayout extends GUILayoutManager {

	public enum constraint {

		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
	}

	constraint getConstraints(GUIElement element) {

		return null;
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {


	}

	public Spring getConstraint() {

		return null;
	}

	public void putConstraint(String string, GUIElement element, int meh, String idk, GUIElement other) {

	}

	public void putConstraint(String string, GUIElement element, String idk, GUIElement other) {

	}

	@Override
	public void layout() {

	}

	@Override
	public Dimension getPreferredSize() {
		return null;
	}

	public static class Spring {

		int min, preferred, max, value;

		GUIElement startElement, endElement;
		constraint startConstraint, endConstraint;

		public Spring sum() {

			return null;
		}

		public Spring max() {

			return null;
		}

		public Spring minus() {

			return null;
		}

		int getMinimumValue() {

			return min;
		}

		int getPreferredValue() {

			return preferred;
		}

		int getMaximumValue() {

			return max;
		}

		int getValue() {

			return value;
		}

		void setValue(int value) {

			this.value = value;
		}
	}
}
