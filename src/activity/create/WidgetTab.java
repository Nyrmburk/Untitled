package activity.create;

import entity.Entity;
import graphics.RenderContext;
import gui.Button;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import main.Engine;
import matrix.*;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import widget.Inverter;

/**
 * Created by Nyrmburk on 8/8/2016.
 */
public class WidgetTab extends Tab {

	private Plane drawPlane = new Plane(0, 0, 0.5f, 0, 0, 1);
	private Vec3 worldPoint;

	private Button inverter = new Button();
	private Button splitter = new Button();
	private Button merger = new Button();
	private Button button = new Button();

	private PointerListener worldEvent;

	public WidgetTab() {
		super("Widgets");
		setlayout(new GUIBoxLayout());

		inverter.setText("Inverter");
		inverter.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				worldEvent = new PointerListener() {
					@Override
					public void actionPerformed(PointerEvent event) {
						if (event.state != State.CLICK)
							return;

						org.jbox2d.common.Vec2 point2d = new org.jbox2d.common.Vec2(worldPoint.x, worldPoint.y);
						BodyQuery callback = new BodyQuery(point2d);

						Engine.level.physicsEngine.queryAABB(callback, callback.getAABB());

						Body body = callback.returnBody;
						if (body == null)
							return;

						Entity entity = (Entity) body.getUserData();
						Inverter inverter = new Inverter();
						inverter.setLevel(Engine.level);
						point2d = body.getLocalPoint(point2d);
						inverter.attach(entity, new Vec2(point2d.x, point2d.y));
					}
				};
			}
		});
		addChild(inverter, 0);

		splitter.setText("Splitter");
		splitter.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
//				if (event.state == State.CLICK)
//					jointType = JointType.DISTANCE;
			}
		});
		addChild(splitter, 0);

		merger.setText("Merger");
		merger.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
//				if (event.state == State.CLICK)
//					jointType = JointType.FRICTION;
			}
		});
		addChild(merger, 0);

		button.setText("Button");
		button.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
//				if (event.state == State.CLICK)
//					jointType = JointType.GEAR;
			}
		});
		addChild(button, 0);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void pointerEvent(PointerListener.PointerEvent event) {

		Vec2 screenPoint = new Vec2(event.pointer.x, view.getHeight() - event.pointer.y);
		worldPoint = getWorldPoint(screenPoint);
		if (worldEvent != null)
			worldEvent.actionPerformed(event);
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

	private class BodyQuery implements QueryCallback {

		public Body returnBody;
		org.jbox2d.common.Vec2 b2Point;

		public BodyQuery(org.jbox2d.common.Vec2 b2Point) {

			this.b2Point = b2Point;
		}

		@Override
		public boolean reportFixture(Fixture fixture) {
			if (fixture.testPoint(b2Point)) {

				returnBody = fixture.getBody();
				return false;
			}
			return true;
		}

		private AABB getAABB() {

			return new AABB(
					new org.jbox2d.common.Vec2(b2Point.x - 0.1f, b2Point.y - 0.1f),
					new org.jbox2d.common.Vec2(b2Point.x + 0.1f, b2Point.y + 0.1f));
		}
	}
}