package graphics;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import gui.css.*;
import org.fit.cssbox.layout.ElementBox;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entity.Camera;
import graphics.RenderContext.InstancedModel;
import main.Engine;
import main.Settings;
import sun.awt.image.ByteInterleavedRaster;
import sun.awt.image.IntegerInterleavedRaster;

public class SimpleOpenGL3_0RenderEngine extends RenderEngine {

	HashSet<RenderAttributes> renderAttributes;

	int polys = 0;
	int drawCalls = 0;

	private static JFrame frmMain;
	private static Canvas display;

	public SimpleOpenGL3_0RenderEngine(RenderContext context) {

		super(context);
		renderAttributes = new HashSet<RenderAttributes>();
		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}

	@Override
	public int render() {

		long time = Engine.getTime();

		polys = 0;

		if (Display.wasResized()) {
			// wasResized = true
		} else {
			// wasResized = false
		}

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		for (RenderAttributes renderAttribute : renderAttributes) {

			ModelLoader model = renderAttribute.iModel.model;
			Collection<InstanceAttributes> instanceAttributes = renderAttribute.iModel.attributes.values();

			GL30.glBindVertexArray(renderAttribute.VAOID);
			GL20.glEnableVertexAttribArray(0);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, renderAttribute.VBOIID);

			for (InstanceAttributes instanceAttribute : instanceAttributes) {

				GL11.glPushMatrix();

				GL11.glTranslatef(instanceAttribute.location[0], instanceAttribute.location[1],
						instanceAttribute.location[2]);
				GL11.glRotatef(instanceAttribute.rotation[0], 1, 0, 0);
				GL11.glRotatef(instanceAttribute.rotation[1], 0, 1, 0);
				GL11.glRotatef(instanceAttribute.rotation[2], 0, 0, 1);

				// Draw the vertices
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.indices.getSize() * model.indices.getStride(),
						GL11.GL_UNSIGNED_INT, 0);

				GL11.glPopMatrix();

				polys += model.indices.getSize();
			}

			// Put everything back to default (deselect)
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		}

		// System.out.println(polys + " polygons");

		return (int) (Engine.getTime() - time);
	}

	private class RenderAttributes {

		int VAOID;
		int VBOID;
		int NBOID;
		int CBOID;
		int VBOIID;

		boolean normals;
		boolean textures;
		boolean colors;

		InstancedModel iModel;

		RenderAttributes(InstancedModel iModel) {

			this.iModel = iModel;
			ModelLoader model = iModel.model;

			VAOID = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(VAOID);

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

			if (!model.normals.isEmpty()) {
				normals = true;
				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
			} else {
				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
			}
			if (!model.colorAmbient.isEmpty()) {
				colors = true;
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			} else {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}
			if (!model.textureCoords.isEmpty()) {
				textures = true;
			} else {
				// GL11.glDisable(GL11.GL_TEXTURE_ARRAY); //something like that
			}

			FloatBuffer verticesBuffer = model.vertices.toFloatBuffer();
			VBOID = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
			// GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

			if (normals) {
				FloatBuffer normalsBuffer = model.normals.toFloatBuffer();
				NBOID = GL15.glGenBuffers();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, NBOID);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
				GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
			}

			if (colors) {
				FloatBuffer colorBuffer = model.colorAmbient.toFloatBuffer();
				CBOID = GL15.glGenBuffers();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CBOID);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
				GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
			}

			if (textures) {
				// TODO
			}

			// Deselect (bind to 0) the VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			// Deselect (bind to 0) the VAO
			GL30.glBindVertexArray(0);

			IntBuffer indicesBuffer = model.indices.toIntBuffer();
			VBOIID = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBOIID);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}

	@Override
	public void look(Camera camera) {
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
			if (Settings.fullscreen) {

				Display.setFullscreen(true);
			} else {

				Display.setParent(display);
				frmMain.setVisible(true);
			}

			Display.create();

		} catch (LWJGLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public TextureInterface getTextureFromImage(BufferedImage image) {

		if (image == null)
			return null;

		return new OpenGLTexture(image);
	}

	@Override
	protected void addModel(InstancedModel model) {

		renderAttributes.add(new RenderAttributes(model));
	}

	@Override
	protected void removeModel(InstancedModel model) {

		renderAttributes.remove(model);
	}

	@Override
	public void renderUI(UIRenderContext renderContext) {

		Rectangle bounds;

		// Camera.UI();

		GL11.glEnable(GL11.GL_BLEND);

		Iterator<Rectangle> quads = renderContext.quads.iterator();
		Iterator<TextureInterface> textures = renderContext.textures.iterator();

		while (textures.hasNext()) {

			bounds = quads.next();
			OpenGLTexture texture = (OpenGLTexture) textures.next();
			texture.bind();

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2i(bounds.x, bounds.y);
			GL11.glTexCoord2f(0, texture.getHeightRatio());
			GL11.glVertex2i(bounds.x, bounds.y + bounds.height);
			GL11.glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
			GL11.glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
			GL11.glTexCoord2f(texture.getWidthRatio(), 0);
			GL11.glVertex2i(bounds.x + bounds.width, bounds.y);
			GL11.glEnd();
		}

//		for (int index : renderContext.indices) {
//
//			bounds = renderContext.quads.get(index);
//
//			OpenGLTexture texture = ((OpenGLTexture) renderContext.textures.get(index));
//			texture.bind();
//
//			GL11.glBegin(GL11.GL_QUADS);
//			GL11.glTexCoord2f(0, 0);
//			GL11.glVertex2i(bounds.x, bounds.y);
//			GL11.glTexCoord2f(0, texture.getHeightRatio());
//			GL11.glVertex2i(bounds.x, bounds.y + bounds.height);
//			GL11.glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
//			GL11.glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
//			GL11.glTexCoord2f(texture.getWidthRatio(), 0);
//			GL11.glVertex2i(bounds.x + bounds.width, bounds.y);
//			GL11.glEnd();
//		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	public void renderUI2(CSSComposite composite) {

//		for (CSSButton button : composite.buttons) {
//
//			if (!composite.getBounds().intersects(button))
//				continue;
//
//			setColor(Color.LIGHT_GRAY);
//
//			GL11.glBegin(GL11.GL_QUADS);
//			GL11.glVertex2i(button.x, button.y);
//			GL11.glVertex2i(button.x, button.y + button.height);
//			GL11.glVertex2i(button.x + button.width, button.y + button.height);
//			GL11.glVertex2i(button.x + button.width, button.y);
//			GL11.glEnd();
//		}

		for (CSSElementBox elementBox : composite.elementBoxes) {

			if (!composite.getBounds().intersects(elementBox))
				continue;

			Color color = elementBox.getElementBox().getBgcolor();
			setColor(color);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(elementBox.x, elementBox.y);
			GL11.glVertex2i(elementBox.x, elementBox.y + elementBox.height);
			GL11.glVertex2i(elementBox.x + elementBox.width, elementBox.y + elementBox.height);
			GL11.glVertex2i(elementBox.x + elementBox.width, elementBox.y);
			GL11.glEnd();
		}

		for (CSSTextBox textBox : composite.textBoxes) {

			if (!composite.getBounds().intersects(textBox))
				continue;

			String text = textBox.getText();
			GraphicsFont font = textBox.getFont();

			setColor(textBox.getColor());

			GL11.glEnable(GL11.GL_BLEND);

			drawString(textBox.x, textBox.y, font, text);

//			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//
//			GL11.glBegin(GL11.GL_LINE_LOOP);
//			GL11.glVertex2i(textBox.x, textBox.y);
//			GL11.glVertex2i(textBox.x, textBox.y + textBox.height);
//			GL11.glVertex2i(textBox.x + textBox.width, textBox.y + textBox.height);
//			GL11.glVertex2i(textBox.x + textBox.width, textBox.y);
//			GL11.glEnd();
		}

		for (CSSImageBox image : composite.imageBoxes) {

			if (!composite.getBounds().intersects(image))
				continue;

			OpenGLTexture texture = (OpenGLTexture) image.getTexture(0);
			Rectangle bounds = image.getBounds();

			texture.bind();

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2i(bounds.x, bounds.y);
			GL11.glTexCoord2f(0, texture.getHeightRatio());
			GL11.glVertex2i(bounds.x, bounds.y + bounds.height);
			GL11.glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
			GL11.glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
			GL11.glTexCoord2f(texture.getWidthRatio(), 0);
			GL11.glVertex2i(bounds.x + bounds.width, bounds.y);
			GL11.glEnd();
		}
	}

	public void drawString(int x, int y, GraphicsFont font, String text) {

		FontMetrics metrics = font.fontMetrics;
		OpenGLTexture texture = (OpenGLTexture) font.atlas;
		texture.bind();

		Rectangle placement;
		int advance = x;
		int height = y;

		for (int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);

			if (c > 255)
				c = ' ';

			Rectangle bounds = font.bounds[c];

			if (bounds != null) {

				placement = bounds.getBounds();
				placement.x = advance;
				placement.y = height;

				GL11.glBegin(GL11.GL_QUADS);

				GL11.glTexCoord2f(texture.xRatio(bounds.x), texture.yRatio(bounds.y));
				GL11.glVertex2i(placement.x, placement.y);

				GL11.glTexCoord2f(texture.xRatio(bounds.x), texture.yRatio(bounds.y + bounds.height));
				GL11.glVertex2i(placement.x, placement.y + placement.height);

				GL11.glTexCoord2f(texture.xRatio(bounds.x + bounds.width), texture.yRatio(bounds.y + bounds.height));
				GL11.glVertex2i(placement.x + placement.width, placement.y + placement.height);

				GL11.glTexCoord2f(texture.xRatio(bounds.x + bounds.width), texture.yRatio(bounds.y));
				GL11.glVertex2i(placement.x + placement.width, placement.y);

				GL11.glEnd();
			}

			advance += metrics.charWidth(c);
		}
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

		return "OpenGL " + GL11.glGetString(GL11.GL_VERSION);
	}

	@Override
	public String getRenderDevice() {
		return GL11.glGetString(GL11.GL_RENDERER);
	}

	private void setColor(Color color) {

		GL11.glColor4f(((float) color.getRed())/255,
				((float) color.getGreen())/255,
				((float) color.getBlue())/255,
				((float) color.getAlpha())/255);
	}

	private class OpenGLTexture implements TextureInterface {

		private int id = 0;

		private int originalWidth = 0;
		private int originalHeight = 0;

		private int actualWidth = 0;
		private int actualHeight = 0;

		OpenGLTexture(BufferedImage texture) {

			originalWidth = texture.getWidth();
			originalHeight = texture.getHeight();

			actualWidth = getMinimumPowerOfTwo(originalWidth);
			actualHeight = getMinimumPowerOfTwo(originalHeight);

			ByteBuffer buffer = getNativeData(actualWidth, actualHeight, texture);
//			ByteBuffer buffer = null;

			int type = texture.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;

			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, actualWidth, actualHeight, 0, type,
					GL11.GL_UNSIGNED_BYTE, buffer);
		}

		public void bind() {

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		}

		public int getWidth() {

			return originalWidth;
		}

		public int getHeight() {

			return originalHeight;
		}

		public float getWidthRatio() {

			return ((float) originalWidth) / actualWidth;
		}

		public float getHeightRatio() {

			return ((float) originalHeight) / actualHeight;
		}

		public float xRatio(int x) {

			float coord = ((float) x) / getWidth() * getWidthRatio();
			return coord;
		}

		public float yRatio(int y) {

			return ((float) y) / getHeight() * getHeightRatio();
		}

		private int getMinimumPowerOfTwo(int x) {

			int powerOfTwo = 2;// opengl minimum texture size is 64x64?

			while (powerOfTwo < x)
				powerOfTwo += powerOfTwo; // addition is faster than multiplication

			return powerOfTwo;
		}

		private ByteBuffer getNativeData(int actualWidth, int actualHeight, BufferedImage texture) {
			// http://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image

			final int bytesPerPixel = texture.getColorModel().hasAlpha() ? 4 : 3;

			int height = texture.getHeight();
			int width = texture.getWidth();

			ByteBuffer texBuffer = BufferUtils.createByteBuffer(actualWidth * actualHeight * bytesPerPixel);


			if (texture.getRaster() instanceof ByteInterleavedRaster) {

				byte[] pixels = ((DataBufferByte) texture.getRaster().getDataBuffer()).getData();
				byte[] slice = new byte[width * bytesPerPixel];

				for (int y = 0; y < height; y++) {

					texBuffer.position(y * actualWidth * bytesPerPixel);
					System.arraycopy(pixels, y * width * bytesPerPixel, slice, 0, slice.length);
					texBuffer.put(slice);
				}

//			} else if (texture.getRaster() instanceof IntegerInterleavedRaster) {
//
//				// This is going to be a large speedup.
////				System.out.println("fast int image");
//
////				if (originalWidth == 800)
////					System.out.println(originalHeight);
//
//				int[] pixels = ((DataBufferInt) texture.getRaster().getDataBuffer()).getData();
//				int[] slice = new int[width];
//				IntBuffer intTexBuffer = texBuffer.asIntBuffer();
//
//				for (int y = 0; y < height; y++) {
//
//					texBuffer.position(y * actualWidth);
//					System.arraycopy(pixels, y * width, slice, 0, slice.length);
//					intTexBuffer.put(slice);
//				}

			} else {

//				System.err.println("slow image: " + texture.getRaster());

				int pixel = 0;

				for (int y = 0; y < height; y++) {

					texBuffer.position(y * actualWidth * bytesPerPixel);
					for (int x = 0; x < width; x++) {

						// the offending statement is right here
						pixel = texture.getRGB(x, y);
						texBuffer.put((byte) ((pixel & 0x00FF0000) >>> 16));
						texBuffer.put((byte) ((pixel & 0x0000FF00) >>> 8));
						texBuffer.put((byte) (pixel & 0x000000FF));
						if (bytesPerPixel == 4)
							texBuffer.put((byte) ((pixel & 0xFF000000) >>> 24));
					}
				}
			}

			texBuffer.rewind();

			return texBuffer;
		}

		@Override
		public void release() {

			GL11.glDeleteTextures(id);
		}
	}
}