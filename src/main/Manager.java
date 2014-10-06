package main;

import entity.Entity;

import java.util.ArrayList;

/**
 * A global class that has the index of all the Entities. It controls updating
 * the Entities and multithreading.
 * 
 * @author Christopher Dombroski
 * 
 */
public class Manager {

	static ArrayList<String> entityID = new ArrayList<String>();
	static ArrayList<Entity> entityList = new ArrayList<Entity>();


	public static void addEntity(Entity entity) {

		entityList.add(entity);
		entityID.add(entity.name);
	}

	/**
	 * Remove an Entity from the buffer.
	 * 
	 * @param name
	 *            The name of the Entity
	 */
	public static void removeEntity(String name) {

		int index;

		index = entityID.indexOf(name);
		entityID.remove(index);
		entityList.remove(index);

	}

	/**
	 * Return the Entity with the supplied name.
	 * 
	 * @param name
	 *            the name of the Entity
	 * @return The Entity
	 */
	public static Entity getEntity(String name) {

		int index;
		index = entityID.indexOf(name);

		return entityList.get(index);

	}

	public static void render() {

		for (Entity ntt : entityList) {
			ntt.draw();
		}
	}

	/**
	 * Update all the Entities. Updating includes orientation, sector mapping,
	 * and collision detection.
	 */
	public static void update(int delta) {

		for (int i = 0; i < entityList.size(); i++) {
			entityList.get(i).update(delta);
		}
	}
}
