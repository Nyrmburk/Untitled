package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class JBox2DPhysicsEngine implements PhysicsEngine {

	private World world;
	
	private int velocityIterations = 8;
	private int positionIterations = 3;
	
	public JBox2DPhysicsEngine(float[] gravity) {
		
		world = new World(new Vec2(gravity[0],  gravity[1]));
	}
	
	@Override
	public void update(int milliseconds) {

		world.step(milliseconds / 1000, velocityIterations, positionIterations);
	}
	
	@Override
	public void addShape(PhysicsShape shape) {

		createBodyFromPhysicsShape(shape);
	}
	
	@Override
	public void removeShape(PhysicsShape shape) {
		//TODO make work
		
		//world.destroyBody(previously referenced shape);
	}

	@Override
	public String[] settingsNames() {
		
		return new String[]{"Position Iterations", "Velocity Iterations"};
	}

	@Override
	public String settingOptions(int setting) {
		
		final String[] OPTIONS = {"integer > 0", "integer > 0"};
		
		return OPTIONS[setting];
	}

	@Override
	public boolean setSetting(int setting, Object option) {
		
		switch (setting) {
		
		case(0): positionIterations = (int) option;
			break;
		case(1): velocityIterations = (int) option;
			break;
		default:
			return false;
		}
		
		return true;
	}

	private Body createBodyFromPhysicsShape(PhysicsShape shape) {
		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.setPosition(arrToVec(shape.position));
		bodyDef.setLinearVelocity(arrToVec(shape.velocity));
		bodyDef.setLinearDamping(shape.damping);
		
		bodyDef.setAngle(shape.angle);
		bodyDef.setAngularVelocity(shape.angularVelocity);
		bodyDef.setAngularDamping(shape.angularDamping);
		
		switch (shape.physicsType) {
		case DYNAMIC:
			bodyDef.setType(BodyType.DYNAMIC);
			break;
		case KINEMATIC:
			bodyDef.setType(BodyType.KINEMATIC);
			break;
		case STATIC:
			bodyDef.setType(BodyType.STATIC);
			break;
		}
		
		bodyDef.allowSleep = true;
		
		FixtureDef fixtureDef = new FixtureDef();
		
		fixtureDef.setDensity(shape.density);
		fixtureDef.setRestitution(shape.restitiution);
		fixtureDef.setFriction(shape.friction);
		fixtureDef.setShape(null);//TODO
		
		Body body = world.createBody(bodyDef);
		
		body.createFixture(fixtureDef);
		
		return body;
	}
	
	private Vec2 arrToVec(float[] array) {
		
		return new Vec2(array[0], array[1]);
	}
}
