package graphics;

import java.awt.image.BufferedImage;

import entity.Camera;
import graphics.RenderContext.InstancedModel;
import gui.Container;

public abstract class RenderEngine {

	protected RenderContext context;
	
	public RenderEngine(RenderContext context) {
		
		setContext(context);
	}
	
	public abstract int render();
	public abstract void look(Camera camera);
	public abstract void showWindow(int width, int height);
	
	public void setContext(RenderContext context) {
		
		for (InstancedModel model : context.models) {
			
			removeModel(model);
		}
		
		this.context = context;
		context.setEngine(this);
		
		for (InstancedModel model : context.models) {
			
			addModel(model);
		}
	}
	
	public RenderContext getContext() {
		
		return context;
	}
	
	public void addModel(ModelLoader model, InstanceAttributes attributes) {
		
		context.addModel(model, attributes);
	}
	
	public void removeModel(ModelLoader model) {
		
		context.removeModel(model);
	}
	
	public abstract void renderUI(Container view);
	
	protected abstract void addModel(InstancedModel model);
	protected abstract void removeModel(InstancedModel model);
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	//public boolean sizeChanged()
	
	public abstract String getRendererVersion();
	public abstract String getRenderDevice();
	
	//public int polyCount();
	//public int drawCalls();
	
	//public String[] settingsNames();
	//public String settingOptions(int setting);
	//public void setSetting(int setting, String option);
}
