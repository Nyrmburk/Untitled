package graphics;

import java.awt.image.BufferedImage;

import entity.Camera;
import graphics.RenderContext.InstancedModel;
import gui.Container;
import gui.GUIAppearance;

public class AndroidRenderEngine extends RenderEngine {

	public AndroidRenderEngine(RenderContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int render() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void look(Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showWindow(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addModel(InstancedModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeModel(InstancedModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderUI(Container renderContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextureInterface getTextureFromImage(BufferedImage image) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getWidth() {
		
		return 0;
	}
	
	@Override
	public int getHeight() {
		
		return 0;
	}

	@Override
	public String getRendererVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRenderDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
