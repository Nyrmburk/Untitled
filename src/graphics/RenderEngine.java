package graphics;


import java.awt.*;

public interface RenderEngine {

	void createWindow(Rectangle viewport);

	void start();

	void render(RenderContext renderContext);

	Display[] getDisplays();
	Display getCurrentDisplay();

	Rectangle getViewport();
	void setViewport(Rectangle viewport);
	void addViewportChangedListener(ViewportChangedListener listener);

	Rectangle getWindow();
	void setWindow(Rectangle window);
	
	//public boolean sizeChanged()
	
	String getRendererVersion();
	String getRenderDevice();
	
	//public int polyCount();
	//public int drawCalls();
	
	//public String[] settingsNames();
	//public String settingOptions(int setting);
	//public void setSetting(int setting, String option);

	abstract class ViewportChangedListener {

		public RenderEngine renderEngine;

		public abstract void onActionPerformed();
	}
}
