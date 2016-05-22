package graphics;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import graphics.ModelGroup.InstancedModel;
import main.Engine;

import gui.Container;

public class SoftwareRenderEngine implements RenderEngine {

	private static JFrame frmMain;
	private static Canvas display;
	private static Graphics graphics;

	@SuppressWarnings("unused")
	@Override
	public int render(RenderContext renderContext) {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, display.getWidth(), display.getWidth());

		for (InstancedModel iModel : renderContext.getModelGroup().getIModels()) {

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
	public void start() {

	}

	@Override
	public void renderUI(Container view) {

//		Rectangle bounds;
//		BufferedImage image;
//
//		Iterator<Rectangle> quads = renderContext.quads.iterator();
//		Iterator<Texture> textures = renderContext.textures.iterator();
//
//		while (textures.hasNext()) {
//
//			bounds = quads.next();
//			image = ((SoftwareTexture) textures.next()).texture;
//			graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
//		}
	}

	public void drawString(int x, int y, GraphicsFont font, String text) {

		graphics.setFont(font.getFont());
		graphics.drawString(text, x, y);
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