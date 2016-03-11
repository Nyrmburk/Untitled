package main;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Nyrmburk on 3/9/2016.
 */
public class ResourceManager {

	private static final Path RESOURCE_PATH = Paths.get("res");

	private static HashMap<String, Resource> resources = new HashMap<>();
	private static HashMap<String, Path> resourcePaths = new HashMap<>();

	private static int staleTime = 5000;
	private static HashMap<Resource, Integer> staleResources = new HashMap<>();

	private static final int THREADS = Runtime.getRuntime().availableProcessors();
	private static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	private static BlockingQueue<UnloadedResource> unloaded = new LinkedBlockingQueue<>();
	private static ConcurrentHashMap<String, List<AsyncLoad>> currentRequests = new ConcurrentHashMap<>();
	private static ConcurrentLinkedQueue<LoadedResource> loaded = new ConcurrentLinkedQueue<>();

	static {

		try {
			Files.walkFileTree(RESOURCE_PATH, new ResourceVisitor());
		} catch (IOException e) {
			e.printStackTrace();
		}

		executor.execute(new LoadLoop());
	}

	public static void getResource(String name, AsyncLoad async, Class<? extends Resource> resourceClass) {

		Resource resource = resources.get(name);

		if (resource != null) {

			//if the resource was already loaded, immediately call the onLoad function
			async.onLoad(resource);
		} else {

			//else have the other threads handle it
			unloaded.add(new UnloadedResource(name, async, resourceClass));
		}
	}

	public static void update(int delta) {

//		updateStaleResources(delta);
		finishAsyncLoads();
	}

	private static void updateStaleResources(int delta) {

		// check for stale resources
		for (Resource resource : resources.values()) {

			if (resource.getReferenceCount() == 0) {
				if (!staleResources.containsKey(resource))
					staleResources.put(resource, 0);
			}
		}

		// if a stale resource is no longer stale, remove it from the stale resources
		Iterator<Resource> itNotStale = staleResources.keySet().iterator();
		while (itNotStale.hasNext()) {

			if (itNotStale.next().getReferenceCount() > 0)
				itNotStale.remove();
		}

		// update the time spent stale. if it has exceeded a certain amount of time it get removed
		Iterator<Map.Entry<Resource, Integer>> itTime = staleResources.entrySet().iterator();
		while (itTime .hasNext()) {
			Map.Entry<Resource, Integer> stale = itTime.next();
			int time = stale.getValue() + delta;

			if (time > staleTime) {

				System.out.println("unloading " + stale.getKey().getName());
				stale.getKey().release();
				itTime.remove();
			} else {
				staleResources.put(stale.getKey(), time);
			}
		}
	}

	private static void finishAsyncLoads() {

		while (!loaded.isEmpty()) {
			LoadedResource resource = loaded.poll();
			resources.put(resource.name, resource.resource);

			List<AsyncLoad> loadTasks = currentRequests.get(resource.name);

			for (AsyncLoad async : loadTasks)
				async.onLoad(resource.resource);

			currentRequests.remove(resource.name);
		}
	}

	private static class LoadLoop implements Runnable {

		@Override
		public void run() {

			// have threads constantly waiting for more input;
			while (true) {

				UnloadedResource unloaded = null;
				try {
					// block until there is a resource to load
					unloaded = ResourceManager.unloaded.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (unloaded == null)
					continue;

				List<AsyncLoad> loadTasks = currentRequests.get(unloaded.name);

				if (loadTasks == null) {
					loadTasks = new LinkedList<AsyncLoad>();
					currentRequests.put(unloaded.name, loadTasks);
					loadTasks.add(unloaded.async);
				} else {
					loadTasks.add(unloaded.async);
					continue;
				}

				Path path = resourcePaths.get(unloaded.name);
				Resource resource = null;
				try {
					// create a new instance of the desired type
					resource = unloaded.resourceClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}

				if (resource == null)
					continue;
				long time = System.currentTimeMillis();
				try {
					// load the desired resource
					resource.load(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("loaded " + resource.getName() + ": " + (System.currentTimeMillis() - time) + " ms");

				// pass the resource onto the main thread and have the
				// main thread call the async onLoad function
				loaded.add(new LoadedResource(unloaded.name, resource));
			}
		}
	}

	private static class UnloadedResource {

		public String name;
		public AsyncLoad async;
		public Class<? extends Resource> resourceClass;

		public UnloadedResource(String name, AsyncLoad async, Class<? extends Resource> resourceClass) {

			this.name = name;
			this.async = async;
			this.resourceClass = resourceClass;
		}
	}

	private static class LoadedResource {

		public String name;
		public Resource resource;

		public LoadedResource(String name, Resource resource) {

			this.name = name;
			this.resource = resource;
		}
	}

	public interface AsyncLoad {

		void onLoad(Resource loadedResource);
	}

	private static class ResourceVisitor implements FileVisitor<Path> {

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

//			System.out.println(file);
			resourcePaths.put(file.getFileName().toString(), file);

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
}
