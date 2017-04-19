package game;

import input.Binding;
import input.Button;
import input.Input;
import input.InputContext;
import matrix.Vec3;
import org.jbox2d.dynamics.Body;

import static physics.JBox2D.convert;

/**
 * Created by Bubba on 3/24/2017.
 */
public class SpaceshipPlayerController extends PlayerController {

    {
        InputContext inputContext = new InputContext();
        inputContext.setAsCurrentContext();
    }

    private Input thrust = new Input("thrust") {

        private float thrustAcceleration = 0.05f;

        {
            Binding.delegate(this);
            InputContext.getCurrentContext().inputs.add(this);
        }

        @Override
        public void onUpdate(float delta) {

            Body physObj = getPawn().getPhysicsObject();
            float angle = physObj.getAngle() + 1.5707963267948966192313216916398f;
            Vec3 thrustDirection = new Vec3((float) Math.cos(angle), (float) Math.sin(angle), 0);

            Vec3 thrust = thrustDirection.multiply(thrustAcceleration * this.getValue());
            physObj.applyLinearImpulse(convert(thrust), physObj.getWorldCenter(), true);
        }
    };

    private Input strafe = new Input("strafe") {

        private float strafeAcceleration = 0.02f;

        {
            Binding.delegate(this);
            InputContext.getCurrentContext().inputs.add(this);
        }

        @Override
        public void onUpdate(float delta) {

            Body physObj = getPawn().getPhysicsObject();
            float angle = physObj.getAngle();
            Vec3 thrustDirection = new Vec3((float) Math.cos(angle), (float) Math.sin(angle), 0);
            Vec3 thrust = thrustDirection.multiply(strafeAcceleration * this.getValue());
            physObj.applyLinearImpulse(convert(thrust), physObj.getWorldCenter(), true);
        }
    };

    private Input yaw = new Input("yaw") {

        private float yawAcceleration = 0.02f;

        {
            Binding.delegate(this);
            InputContext.getCurrentContext().inputs.add(this);
        }

        @Override
        public void onUpdate(float delta) {

            Body physObj = getPawn().getPhysicsObject();
            physObj.applyAngularImpulse(-yawAcceleration * this.getValue());
        }
    };

    private Input fire = new Button("fire") {

        float cooldownLength = 0.1f;
        float cooldown = 0f;

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
        public void onHold(float delta) {

            if (cooldown <= 0f) {
                shoot();
                cooldown = cooldownLength;
            }
        }

        @Override
        public void onUpdate(float delta) {
            super.onUpdate(delta);

            cooldown -= delta;
        }

        private void shoot() {
            System.out.println("shooted the bad guys");
            // load plasma in the beginning before this is called
            // set velocity of projectile to ship's velocity + direction of front of ship

        }
    };
}
