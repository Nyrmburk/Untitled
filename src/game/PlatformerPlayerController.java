package game;

import input.Binding;
import input.Button;
import input.Input;
import input.InputContext;
import physics.PhysicsObject;

/**
 * Created by Nyrmburk on 6/16/2016.
 */
public class PlatformerPlayerController extends PlayerController {

	private float speed = 6;
	private float acceleration = 0.3f;
	private float jumpheight = 1.2f;

	private Input jump = new Button("jump") {

		{
			Binding.delegate(this);
			InputContext.getCurrentContext().inputs.add(this);
		}

		@Override
		public void onPress() {

			PhysicsObject physObj = getPawn().getPhysicsObject();
			physObj.applyLinearImpulse(new float[]{0, 4.905f}, physObj.getPosition());
		}

		@Override
		public void onRelease() {
		}

		@Override
		public void onHold(int delta) {

			PhysicsObject physObj = getPawn().getPhysicsObject();
			physObj.applyLinearImpulse(new float[]{0, 0.04905f}, physObj.getPosition());
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

			PhysicsObject physObj = getPawn().getPhysicsObject();
			float velChange = Math.max(-speed - physObj.getLinearVelocity()[0], -acceleration);
			float impulse = velChange * getPawn().getPhysicsObject().getMass();
			physObj.applyLinearImpulse(new float[]{impulse, 0}, physObj.getPosition());
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

			PhysicsObject physObj = getPawn().getPhysicsObject();
			float velChange = Math.min(speed - physObj.getLinearVelocity()[0], acceleration);
			float impulse = velChange * getPawn().getPhysicsObject().getMass();
			physObj.applyLinearImpulse(new float[]{impulse, 0}, physObj.getPosition());
		}
	};

	{
		inputs.add(jump);
		inputs.add(left);
		inputs.add(right);
	}
}
