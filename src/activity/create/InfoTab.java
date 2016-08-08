package activity.create;

import graphics.RenderContext;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import gui.TextBox;
import main.Engine;
import matrix.*;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.Fixture;

/**
 * Created by Nyrmburk on 8/6/2016.
 */
public class InfoTab extends Tab {

	private Plane drawPlane = new Plane(0, 0, 0.5f, 0, 0, 1);
	private Vec3 worldPoint;

	private TextBox txtFps = new TextBox();
	private TextBox txtPosition = new TextBox();
	private TextBox txtHover = new TextBox();

	public InfoTab() {

		super("Info");

		setlayout(new GUIBoxLayout());

		txtFps.setText("fps");
		addChild(txtFps, 0);

		txtPosition.setText("world position");
		addChild(txtPosition, 0);

		txtHover.setText("current hover");
		addChild(txtHover, 0);
	}

	@Override
	public void update(float delta) {

		txtFps.setText(String.format("%6.2f fps", 1f / delta));
		updatePosition();
		updateHover();
	}

	@Override
	public void pointerEvent(PointerListener.PointerEvent event) {

		Vec2 screenPoint = new Vec2(event.pointer.x, view.getHeight() - event.pointer.y);
		worldPoint = getWorldPoint(screenPoint);
	}

	private Vec3 getWorldPoint(Vec2 point) {

		RenderContext world = Engine.level.getRenderContext();
		Ray3[] rays = Projection.unproject(world.getCamera(), Engine.renderEngine.getViewport(), point);

		Ray3 ray;
		if (rays == null) {
			return null;
		} else {
			ray = rays[0];
		}

		return ray.point(drawPlane.intersect(ray));
	}

	private void updatePosition() {

		if (worldPoint != null)
			txtPosition.setText(String.format("%8.2f, %8.2f", worldPoint.x, worldPoint.y));
	}

	private void updateHover() {

		if (worldPoint != null) {
			org.jbox2d.common.Vec2 b2Point = new org.jbox2d.common.Vec2(worldPoint.x, worldPoint.y);
			AABB aabb = new AABB(new org.jbox2d.common.Vec2(worldPoint.x - 0.1f, worldPoint.y - 0.1f), new org.jbox2d.common.Vec2(worldPoint.x + 0.1f, worldPoint.y + 0.1f));
			txtHover.setText("nothing");
			Engine.level.physicsEngine.queryAABB(new QueryCallback() {
				@Override
				public boolean reportFixture(Fixture fixture) {
					if (fixture.testPoint(b2Point)) {
						String name = fixture.getBody().toString();
						txtHover.setText(name.substring(name.indexOf('@')));
						return false;
					}
					return true;
				}
			}, aabb);
		}
	}
}
