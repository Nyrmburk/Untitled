package graphics.animation;

/**
 * Created by Nyrmburk on 8/11/2016.
 */
public abstract class Animation {

	private AnimationFrame animationFrame;
	private float duration;
	private float elapsed;

	public Animation(AnimationFrame animationFrame, float duration) {

		this.animationFrame = animationFrame;
		this.duration = duration;
	}

	public void update(float delta) {

		elapsed += delta;
		animationFrame.animate(map(elapsed / duration));
	}

	public abstract float map(float percent);

	public interface AnimationFrame {

		void animate(float sample);
	}
}
