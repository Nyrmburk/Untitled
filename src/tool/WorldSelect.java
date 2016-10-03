package tool;

import entity.Entity;
import game.Level;
import matrix.Vec2;

import java.util.List;

/**
 * Created by Nyrmburk on 10/2/2016.
 */
public class WorldSelect {

	Level level;

	public WorldSelect(Level level) {

		setLevel(level);
	}

	/**
	 * selects a list of entities that are contained by the bounding box
	 * @return
	 */
	public List<Entity> contained(Vec2 start, Vec2 end) {

		// I can do a couple things
		// I can make the selection screen-space or world-space
		// the name of the class seems to indicate that it would be world-space
		// however, a screen-space method would be more useful
		// gahh
		// what to do??


		return null;
	}

	public List<Entity> intersects(Vec2 start, Vec2 end) {

		return null;
	}



	public void setLevel(Level level) {
		this.level = level;
	}
}
