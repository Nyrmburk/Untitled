package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import entity.Camera2;
import graphics.RenderContext.InstancedModel;
import main.Engine;

public class SoftwareRenderEngine extends RenderEngine {

	private static JFrame frmMain;
	private static Canvas display;
	private static Graphics graphics;

	public SoftwareRenderEngine(RenderContext context) {
		super(context);
	}

	@SuppressWarnings("unused")
	@Override
	public int render() {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, display.getWidth(), display.getWidth());

		for (InstancedModel iModel : context.models) {

			ModelLoader model = iModel.model;
			
			for (int i = 0; i < model.indices.getSize(); i++) {
				
				int index = model.indices.get(i)[0];
				float[] vertices = model.vertices.get(index);
				
				//transform vertices
				//project vertices
				//fragment for all pixel within triangle
				//depth test on fragment
				//render fragment color based on lighting and angle
				
			}

			// I would need to transform each triangle by a projection matrix
			// and then a modelview matrix and then create my own fragment shader
			// that supports a depth test.
			// graphics.fillPolygon(xPoints, yPoints, nPoints);

		}
		return 0;
	}

	@Override
	public void look(Camera2 camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showWindow(int width, int height) {

		frmMain = new JFrame();
		display = new Canvas();
		display.setBackground(Color.BLACK);
		display.setSize(width, height);
		frmMain.add(display);
		frmMain.pack();
		frmMain.setTitle("Untitled");

		frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Engine.isClosing = true;
			}
		});

		try {
			Display.setDisplayMode(new DisplayMode(0, 0));
			Display.create();
			Display.setResizable(true);
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		frmMain.setVisible(true);

		graphics = display.getGraphics();
	}

	@Override
	public void renderUI(UIRenderContext renderContext) {

		Rectangle bounds;
		BufferedImage image;

		for (int index : renderContext.indices) {

			bounds = renderContext.map.get(index).getBounds();
			image = ((SoftwareTexture) renderContext.textures.get(index)).texture;

			graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
	}

	@Override
	public TextureInterface getTextureFromImage(BufferedImage image) {

		return new SoftwareTexture(image);
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
	public int getWidth() {
		
		return display.getWidth();
	}
	
	@Override
	public int getHeight() {
		
		return display.getHeight();
	}

	@Override
	public String getRendererVersion() {

		return "0.1";
	}

	@Override
	public String getRenderDevice() {

		String renderDevice = "CPU";

		if (Runtime.getRuntime().availableProcessors() > 1)
			renderDevice = Runtime.getRuntime().availableProcessors() + "x " + renderDevice;

		return renderDevice;
	}

}

class SoftwareTexture implements TextureInterface {

	public BufferedImage texture;
	
	SoftwareTexture(BufferedImage texture) {
		
		this.texture = texture;
	}
	
	@Override
	public void release() {
		
		texture = null;
	}
}
