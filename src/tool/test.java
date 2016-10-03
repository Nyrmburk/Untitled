package tool;

import entity.Entity;
import matrix.Vec2;
import widget.Widget;

/**
 * Created by Nyrmburk on 10/2/2016.
 */
public class test {

	public void test() {

		Selection<Entity>  selection = new Selection<>();

		Transform transform = new Transform(selection);
		transform
				.translate(new Vec2(65, 56))
				.rotate(6);

		selection.clear();

		transform
				.translate(new Vec2(10, 15))
				.rotate(89);

		selection.stream()
				.filter(t -> t.getModel() != null)
				.filter(t -> t instanceof Widget)
				.close();
	}
}
