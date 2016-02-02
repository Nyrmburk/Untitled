package input;

import entity.Actor;

/**
 * @author Nyrmburk
 *
 *         State is a way of making some actions available under certain
 *         circumstances. For example, the jump action is only available when
 *         not airborne. It works using a stack to keep track of the state.
 *         Several states can be used concurrently to minimize copying.
 */
public abstract class State {

	Actor parent;
	int stackID;

	public State(int stackID, Actor parent) {

		this.stackID = stackID;
		this.parent = parent;
	}

	public void enter() {

		parent.pushState(stackID, this);
	}

	public void exit() {

		parent.popState(stackID);
	}

	// public abstract void handleInput(Intent command);

	public abstract void update(int delta);
}
