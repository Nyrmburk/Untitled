package graphics;

import java.awt.image.BufferedImage;

import entity.Camera2;
import graphics.RenderContext.InstancedModel;

public abstract class RenderEngine {
	
	//TODO add a method to return a texture/texture reference given a bufferedimage 

	protected RenderContext context;
	
	public RenderEngine(RenderContext context) {
		
		setContext(context);
	}
	
	public abstract int render();
	public abstract void look(Camera2 camera);	
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
	
	public int addModel(ModelLoader model, InstanceAttributes attributes) {
		
		return context.addModel(model, attributes);
	}
	
	public void removeModel(int id) {
		
		context.removeModel(id);
	}
	
	public abstract void renderUI(UIRenderContext renderContext);
	
	public abstract TextureInterface getTextureFromImage(BufferedImage image);
	
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
