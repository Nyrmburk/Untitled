package main;


import entity.Entity;
import input.*;
import physics.PhysicsObject;
import world.Level;

public class Player extends Entity {

	private float speed = 6;
	private float acceleration = 6f;
	private float jumpheight = 1;

	public Player(Level level) {
		super(level);
	}

	private Input jump = new Button("jump") {

		{
			Binding.delegate(this);
			InputContext.getCurrentContext().inputs.add(this);
		}

		@Override
		public void onPress() {

			getPhysicsObject().applyLinearImpulse(new float[]{0, 4.905f}, new float[]{0, 0});
		}

		@Override
		public void onRelease() {
		}

		@Override
		public void onHold(int delta) {

			getPhysicsObject().applyLinearImpulse(new float[]{0, 0.04905f}, new float[]{0, 0});
		}
	};

	private Input left = new Button("left") {

		{
			Binding.delegate(this);
			InputContext.getCurrentContext().inputs.add(this);
		}

		@Override
		public void onPress() {
		}

		@Override
		public void onRelease() {
		}

		@Override
		public void onHold(int delta) {

			PhysicsObject physObj = getPhysicsObject();
			float velChange = Math.max(-speed - physObj.getLinearVelocity()[0], -acceleration);
			float impulse = convert(velChange);// / (((float) delta) / 1000);
			physObj.applyForceToCenter(new float[]{impulse, 0});

//			System.out.println(impulse + ", " + physObj.getLinearVelocity()[0]);
		}
	};

	private Input right = new Button("right") {

		{
			Binding.delegate(this);
			InputContext.getCurrentContext().inputs.add(this);
		}

		@Override
		public void onPress() {
		}

		@Override
		public void onRelease() {
		}

		@Override
		public void onHold(int delta) {

			PhysicsObject physObj = getPhysicsObject();
			float velChange = Math.min(speed - physObj.getLinearVelocity()[0], acceleration);
			float impulse = convert(velChange) * (1f/70);
			physObj.applyLinearImpulse(new float[]{impulse, 0}, new float[]{0, 0});

//			System.out.println(impulse);
		}
	};

	public float convert(float value) {

		return value * getPhysicsObject().getMass();
	}
}