package main;

/**
 * Created by Nyrmburk on 7/18/2016.
 */
public class Timer {

	public static final long NANOS_PER_SECOND = 1_000_000_000;
	private static final int NANOS_PER_MILLI = 1_000_000;

	private long lastTick;

	public Timer() {

		tick();
	}

	public long tick() {

		long tick = System.nanoTime();
		long delta = tick - lastTick;
		lastTick = tick;
		return delta;
	}

	public void sync(float frequency) {

		if (frequency == 0)
			return;

		int delta = (int) (System.nanoTime() - lastTick);

		delta = (int) ((float) NANOS_PER_SECOND / frequency) - delta;
		if (delta < 0)
			delta = 0;

		try {
			Thread.sleep(delta / NANOS_PER_MILLI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long endTime = lastTick + (long) (NANOS_PER_SECOND / frequency);
		while (System.nanoTime() < endTime)
			;
	}

	public static float nanosToSeconds(long nanos) {

		return (float) nanos / NANOS_PER_SECOND;
	}

	public static float deltaToFPS(float delta) {

		return 1f / delta;
	}
}
