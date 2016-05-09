package graphics;

import gui.Container;

public interface RenderEngine {
	
	void showWindow(int width, int height);

	int render(RenderContext renderContext);
	void renderUI(Container view);

	//TODO remove and replace with a screen object that has resolution, fullscreen, and refresh rate.
	int getWidth();
	int getHeight();
	
	//public boolean sizeChanged()
	
	String getRendererVersion();
	String getRenderDevice();
	
	//public int polyCount();
	//public int drawCalls();
	
	//public String[] settingsNames();
	//public String settingOptions(int setting);
	//public void setSetting(int setting, String option);
}
