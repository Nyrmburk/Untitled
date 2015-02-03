package main;

import entity.Entity;

import java.util.ArrayList;

import world.Zone;

/**
 * A global class that has the index of all the Entities. It controls updating
 * the Entities and multithreading.
 * 
 * @author Christopher Dombroski
 * 
 */
public class Manager {

	static ArrayList<String> entityID = new ArrayList<String>();
	public static ArrayList<Entity> entityList = new ArrayList<Entity>();
	public static ArrayList<Zone> zoneList = new ArrayList<Zone>();

	static int processors = Runtime.getRuntime().availableProcessors();
//	static int processors = 64;
	static Thread[] threads = new Thread[processors];
	static UpdateThread[] runnables = new UpdateThread[processors];
	
	final static Object[] lock = new Object[processors];

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
	
	public static void addZone(Zone zone) {
		
		zoneList.add(zone);
	}

	public static void render() {

		for (Entity entity : entityList) {
			
			entity.draw();
		}
		
		for (Zone zone : zoneList) {
			
			zone.draw();
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

//		for (int i = 0; i < threads.length; i++) {
//
//			if (threads[i] == null) {
//				lock[i] = new Object();
//				runnables[i] = new UpdateThread(entityList, delta, i,
//						processors);
//				threads[i] = new Thread(runnables[i], "Update Thread " + i);
//				threads[i].setPriority(Thread.MAX_PRIORITY);
//				threads[i].start();
//			} else {
//
//				runnables[i].updateDelta(delta);
//			}
//		}
//
//		for (int i = 0; i < threads.length; i++) {
//
//			synchronized (lock[i]) {
//
//				lock[i].notify();
//			}
//		}
	}
}

class UpdateThread implements Runnable {

	ArrayList<Entity> entityList;
	int delta;
	int index;
	int total;
	int workload;
	int start;
	int end;

	public UpdateThread(ArrayList<Entity> entityList, int delta, int index,
			int total) {

		this.entityList = entityList;
		this.delta = delta;
		this.index = index;
		this.total = total;
	}

	@Override
	public void run() {

		synchronized (Manager.lock[index]) {

			while (!Engine.isClosing) {

				try {
					Manager.lock[index].wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				workload = (int) Math.ceil((float) entityList.size() / total);
				start = index * workload;
				end = index * workload + workload;

				if (start > entityList.size())
					start = entityList.size();
				if (end > entityList.size())
					end = entityList.size();

				for (int i = start; i < end; i++) {

					entityList.get(i).update(delta);
				}
			}
		}
	}
	
	public synchronized void updateDelta(int delta) {
		
		updateData(delta, index, total);
	}

	public synchronized void updateData(int delta, int index, int total) {

		this.delta = delta;
		this.index = index;
		this.total = total;
	}
}
