package activity;

import entity.Entity;
import game.Level;
import game.PlayerController;
import game.SpaceshipPlayerController;
import graphics.Camera;
import graphics.ModelLoader;
import graphics.PerspectiveCamera;
import gui.View;
import main.Engine;
import main.Resource;
import main.ResourceManager;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec3;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static physics.JBox2D.convert;
import static physics.JBox2D.shapeFromPolygon;

/**
 * Created by Bubba on 3/24/2017.
 */
public class SpaceGameActivity extends Activity {

    private Level level;

    @Override
    protected void onCreate() {
        View view = new View(Engine.renderEngine);

        createLevel();

        level.physicsEngine.setGravity(new Vec2(0, 0));

        setView(view);
    }

    @Override
    protected void onStart() {

        Entity starfield = new Entity();
        ResourceManager.getResource("starfield.obj", new ResourceManager.AsyncLoad() {
            @Override
            public void onLoad(Resource loadedResource) {
                ModelLoader model = (ModelLoader) loadedResource;
                starfield.setLevel(level);
                starfield.setModel(model);
                Mat4 transform = Mat4.identity();
                Transform.translate(transform, new Vec3(0, 0, -1));
                starfield.setTransform(transform);
            }
        }, ModelLoader.class);

        Entity ship = new Entity();
        ship.setLevel(level);
        PlayerController controller = new SpaceshipPlayerController();
        controller.setPawn(ship);
        level.players.add(controller);
        BodyDef objectDef = new BodyDef();
        objectDef.setType(BodyType.DYNAMIC);
        Body shipBody = ship.setPhysicsObject(objectDef);
        shipBody.setLinearDamping(0.5f);
        shipBody.setAngularDamping(0.5f);

        matrix.Vec2[] verts = {
                new matrix.Vec2(-1.4f, -0.7f),
                new matrix.Vec2(1.4f, -0.7f),
                new matrix.Vec2(0, 1.7f)
        };

        for (Shape shape : shapeFromPolygon(verts)) {
            FixtureDef fixture = new FixtureDef();
            fixture.setFriction(0.5f);
            fixture.setRestitution(0.15f);
            fixture.setDensity(1);
            fixture.setShape(shape);
            shipBody.createFixture(fixture);
        }

        ResourceManager.getResource("delta.obj", new ResourceManager.AsyncLoad() {
            @Override
            public void onLoad(Resource loadedResource) {
                ModelLoader model = (ModelLoader) loadedResource;
                ship.setModel(model);
            }
        }, ModelLoader.class);
    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onStop() {

    }

    @Override
    protected void onDestroy() {

    }

    @Override
    public void onUpdate(float delta) {
        Vec3 vec = Transform.getPosition(level.getRenderContext().getCamera().getTransform());
        vec.x = 0;
        vec.y = 2.5f;
        vec.z = -vec.z;
        updateFollowCam(vec);
    }

    private void createLevel() {

        Camera cam = new PerspectiveCamera();
        Transform.setPosition(cam.getTransform(), new Vec3(0, 0, -20));
        level = new Level(cam);
        Engine.level = getLevel();
    }

    public Level getLevel() {
        return level;
    }

    private void updateFollowCam(Vec3 cameraOffset) {

        Vec3 avgPosition = new Vec3();

        for (PlayerController player : Engine.level.players)
            avgPosition = avgPosition.add(convert(player.getPawn().getPhysicsObject().getPosition()).asVec3());

        avgPosition.divide(Engine.level.players.size());

        Vec3 cameraPosition = avgPosition.add(cameraOffset);

        Camera camera = Engine.level.getRenderContext().getCamera();
        cameraOffset.z = 0;

        Mat4 transform = Mat4.identity();
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 target = avgPosition.add(cameraOffset);
        Transform.pointAt(transform, cameraPosition, target, up);

        camera.setTransform(transform);
    }
}
