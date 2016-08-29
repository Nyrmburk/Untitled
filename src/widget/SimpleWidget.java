package widget;

import entity.Entity;
import graphics.InstanceAttributes;
import graphics.ModelLoader;
import matrix.Mat4;
import matrix.Transform;
import matrix.Vec2;

/**
 * Created by Nyrmburk on 8/28/2016.
 */
public abstract class SimpleWidget extends Widget {

	Entity attachedEntity;
	Vec2 offset;
	InstanceAttributes instance = new InstanceAttributes();

	public SimpleWidget(int inputs, int outputs, Entity attachedEntity, Vec2 offset) {
		super(inputs, outputs);
		this.attachedEntity = attachedEntity;
		this.offset = offset;
	}

	@Override
	public void update(float delta) {

		Mat4 transform = instance.getTransform();
		transform.set(attachedEntity.getTransform());
		Transform.translate(transform, offset.asVec3());
	}

	@Override
	public ModelLoader getModel() {
		return null;
	}

	@Override
	public InstanceAttributes getInstance() {
		return instance;
	}
}
