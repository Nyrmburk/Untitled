package widget;

import entity.Entity;
import graphics.InstanceAttributes;
import graphics.ModelLoader;
import main.Resource;
import main.ResourceManager;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec2;

/**
 * Created by Nyrmburk on 8/28/2016.
 */
public abstract class SimpleWidget extends Widget {

	Entity attachedEntity;
	Vec2 offset;

	{
		ResourceManager.getResource(
				"widget.obj",
				(loadedModel) -> setModel((ModelLoader) loadedModel),
				ModelLoader.class);
	}

	public SimpleWidget(int inputs, int outputs) {
		super(inputs, outputs);
	}

	public void attach(Entity attachedEntity, Vec2 offset) {
		this.attachedEntity = attachedEntity;
		this.offset = offset;
	}

	@Override
	public void update(float delta) {

		Mat4 transform = getTransform();
		transform.set(attachedEntity.getTransform());
		Transform.translate(transform, offset.asVec3(0.5f));
		setTransform(transform);
	}
}
