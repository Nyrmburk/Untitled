package graphics;


import java.awt.*;

public interface RenderEngine {

	void createWindow(Rectangle viewport);

	void start();

	int render(RenderContext renderContext);

	Display[] getDisplays();
	Display getCurrentDisplay();

	Rectangle getViewport();
	void setViewport(Rectangle viewport);

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
}
