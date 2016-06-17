package graphics;

import java.awt.*;

/**
 * Created by Nyrmburk on 6/16/2016.
 */
public class Display {

	private Dimension resolution;
	private int refreshRate;

	public Display(Dimension resolution, int refreshRate) {

		this.resolution = resolution;
		this.refreshRate = refreshRate;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public Dimension getResolution() {
		return resolution;
	}
}
