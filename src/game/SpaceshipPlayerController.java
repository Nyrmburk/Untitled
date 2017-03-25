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

    private Input thrust = new Button("thrust") {

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

            Body physObj = getPawn().getPhysicsObject();
            physObj.applyLinearImpulse(convert(new Vec3(0, 0.05f, 0)), physObj.getPosition(), true);
        }
    };
}
