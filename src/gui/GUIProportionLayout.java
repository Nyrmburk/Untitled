package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

/**
 * Created by Nyrmburk on 4/28/2016.
 * <p>
 * So, here is how it is going to work:
 * There are connectors. There are two different types (unless I can figure out how to conjoin them). Type one is fixed
 * distance and type 2 is percentage based. The connectors manipulate the edges of boxes in relation to other edges.
 * Movement of one box affects the other. That is a tricky one; which box gets moved when laying out? The moved one
 * (im not totally sure) will be invalid? idk. Maybe hold the previous values so that when the layout is calculated the
 * ones to move will be equal? That might lead to some infinite loops. idk. Sleep on it.
 * <p>
 * Sleep over. I have a redesigned idea. All the links are in the form of a percentage. Some work needs to be done to
 * define the start and end of the links. However I think there are three competing ideas. One: define the start
 * component and edge, end component and edge, moving component and edge, and the percentage. Two: Define the absolute
 * percentage, component, and the edge. Three: define the moving component, moving component edge, end component and
 * edge, and the start is the opposite edge of the moving component. I need to think of the layout ramifications of all
 * the styles to determine pros, cons, and feasibility. Perhaps some styles will result in an infinite loop.
 */
public class GUIProportionLayout extends GUILayoutManager {

	Map<GUIElement, List<Anchor>> constraints = new HashMap<>();

	public enum Constraint {

		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
	}

	@Override
	public void layout() {

		for (GUIElement child : parent.children) {

			List<Anchor> anchorList = constraints.get(child);

			Rectangle rect = new Rectangle(child.getBounds());

			for (Anchor anchor : anchorList) {

				int start = getSideValue(anchor.start, anchor.startSide);
				int end = getSideValue(anchor.end, anchor.endSide);
				int position = (int) ((end - start) * anchor.percent + start);

				switch (anchor.moveSide) {
					case TOP:
						rect.y = position;
						break;
					case BOTTOM:
						rect.height = position - rect.y;
						break;
					case LEFT:
						rect.x = position;
						break;
					case RIGHT:
						rect.width = position - rect.x;
						break;
				}
			}
			child.setBounds(rect);
		}
	}

	private int getSideValue(GUIElement element, Constraint side) {

		int value = 0;
		switch (side) {
			case TOP:
				value = element.getY();
				break;
			case BOTTOM:
				value = element.getY() + element.getHeight();
				break;
			case LEFT:
				value = element.getX();
				break;
			case RIGHT:
				value = element.getX() + element.getWidth();
				break;
		}

		return value;
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {

		if (constraint == null)
			return;

		Anchor anchor = (Anchor) constraint;

		List<Anchor> anchorList = constraints.get(element);

		if (anchorList == null) {
			anchorList = new LinkedList<>();
			constraints.put(element, anchorList);
		}
		anchorList.add(anchor);
	}

	@Override
	public Dimension getPreferredSize() {
		return parent.getSize();
	}

	public static class Anchor {

		GUIElement start;
		Constraint startSide;

		Constraint moveSide;
		float percent;

		GUIElement end;
		Constraint endSide;

		public Anchor(GUIElement start, Constraint startSide,
					  Constraint moveSide, float percent,
					  GUIElement end, Constraint endSide) {

			this.start = start;
			this.startSide = startSide;
			this.moveSide = moveSide;
			this.percent = percent;
			this.end = end;
			this.endSide = endSide;
		}
	}
}
