package activity.create;

import graphics.RenderContext;
import gui.Button;
import gui.TextBox;
import gui.event.PointerListener;
import gui.layout.GUIBoxLayout;
import main.Engine;
import matrix.*;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.*;

/**
 * Created by Nyrmburk on 8/8/2016.
 */
public class PhysicsTab extends Tab {

	private Plane drawPlane = new Plane(0, 0, 0.5f, 0, 0, 1);
	private Vec3 startPoint;

	private TextBox manipulate = new TextBox();
	private Button move = new Button();

	private TextBox constraints = new TextBox();
	private Button constantVolume = new Button();
	private Button distance = new Button();
	private Button friction = new Button();
	private Button gear = new Button();
	private Button motor = new Button();
	private Button mouse = new Button();
	private Button prismatic = new Button();
	private Button pulley = new Button();
	private Button revolute = new Button();
	private Button rope = new Button();
	private Button weld = new Button();
	private Button wheel = new Button();

	private JointType jointType;

	public PhysicsTab() {
		super("Physics");
		setlayout(new GUIBoxLayout());

		manipulate.setText("Manipulate");
		addChild(manipulate);

		move.setText("Move");
		addChild(move, 0);

		constraints.setText("Constraints");
		addChild(constraints);

		constantVolume.setText("Constant Volume");
		constantVolume.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.CONSTANT_VOLUME;
			}
		});
		addChild(constantVolume, 0);

		distance.setText("Distance");
		distance.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.DISTANCE;
			}
		});
		addChild(distance, 0);

		friction.setText("Friction");
		friction.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.FRICTION;
			}
		});
		addChild(friction, 0);

		gear.setText("Gear");
		gear.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.GEAR;
			}
		});
		addChild(gear, 0);

		motor.setText("Motor");
		motor.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.MOTOR;
			}
		});
		addChild(motor, 0);

		mouse.setText("Mouse");
		mouse.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.MOUSE;
			}
		});
		addChild(mouse, 0);

		prismatic.setText("Prismatic");
		prismatic.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.PRISMATIC;
			}
		});
		addChild(prismatic, 0);

		pulley.setText("Pulley");
		pulley.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.PULLEY;
			}
		});
		addChild(pulley, 0);

		revolute.setText("Revolute");
		revolute.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.REVOLUTE;
			}
		});
		addChild(revolute, 0);

		rope.setText("Rope");
		rope.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.ROPE;
			}
		});
		addChild(rope, 0);

		weld.setText("Weld");
		weld.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.WELD;
			}
		});
		addChild(weld, 0);

		wheel.setText("Wheel");
		wheel.addPointerListener(new PointerListener() {
			@Override
			public void actionPerformed(PointerEvent event) {
				if (event.state == State.CLICK)
					jointType = JointType.WHEEL;
			}
		});
		addChild(wheel, 0);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void pointerEvent(PointerListener.PointerEvent event) {

		Vec2 screenPoint = new Vec2(event.pointer.x, view.getHeight() - event.pointer.y);
		Vec3 worldPoint = getWorldPoint(screenPoint);
		switch (event.state) {
			case PRESS:
				startPoint = worldPoint;
				break;
			case RELEASE:
				createJoint(startPoint, worldPoint);
				break;
		}
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

	public void createJoint(Vec3 startPoint, Vec3 endPoint) {

		Body startBody;
		org.jbox2d.common.Vec2 b2StartPoint = new org.jbox2d.common.Vec2(startPoint.x, startPoint.y);
		AABB aabb = new AABB(
				new org.jbox2d.common.Vec2(b2StartPoint.x - 0.1f, b2StartPoint.y - 0.1f),
				new org.jbox2d.common.Vec2(b2StartPoint.x + 0.1f, b2StartPoint.y + 0.1f));
		BodyQuery callback = new BodyQuery(b2StartPoint);
		Engine.level.physicsEngine.queryAABB(callback, aabb);
		startBody = callback.returnBody;

		Body endBody;
		org.jbox2d.common.Vec2 b2EndPoint = new org.jbox2d.common.Vec2(endPoint.x, endPoint.y);
		aabb = new AABB(
				new org.jbox2d.common.Vec2(b2EndPoint.x - 0.1f, b2EndPoint.y - 0.1f),
				new org.jbox2d.common.Vec2(b2EndPoint.x + 0.1f, b2EndPoint.y + 0.1f));
		callback = new BodyQuery(b2EndPoint);
		Engine.level.physicsEngine.queryAABB(callback, aabb);
		endBody = callback.returnBody;

		World world = Engine.level.physicsEngine;

		switch (jointType) {
			case UNKNOWN:
				break;
			case REVOLUTE:
				RevoluteJointDef revoluteDef = new RevoluteJointDef();
				break;
			case PRISMATIC:
				PrismaticJointDef prismaticDef = new PrismaticJointDef();
				break;
			case DISTANCE:
				if (startBody == null || endBody == null)
					return;
				DistanceJointDef distanceDef = new DistanceJointDef();
				distanceDef.initialize(startBody, endBody, b2StartPoint, b2EndPoint);
				distanceDef.collideConnected = true;
				world.createJoint(distanceDef);
				System.out.println("created distance constraint");
				break;
			case PULLEY:
				PulleyJointDef pulleyDef = new PulleyJointDef();
				break;
			case MOUSE:
				MouseJointDef mouseDef = new MouseJointDef();
				break;
			case GEAR:
				GearJointDef gearDef = new GearJointDef();
				break;
			case WHEEL:
				WheelJointDef wheelDef = new WheelJointDef();
				break;
			case WELD:
				WeldJointDef weldDef = new WeldJointDef();
				break;
			case FRICTION:
				FrictionJointDef frictionDef = new FrictionJointDef();
				break;
			case ROPE:
				RopeJointDef ropeDef = new RopeJointDef();
				break;
			case CONSTANT_VOLUME:
				ConstantVolumeJointDef volumeDef = new ConstantVolumeJointDef();
				break;
			case MOTOR:
				MotorJointDef motorDef = new MotorJointDef();
				break;
		}
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
	}
}