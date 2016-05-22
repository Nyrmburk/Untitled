package graphics.opengl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import graphics.*;
import graphics.ModelGroup.InstancedModel;
import gui.Container;
import gui.ContextBox;
import gui.GUIElement;
import matrix.Mat4;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import main.Engine;
import main.Settings;

public class SimpleOpenGL3_0RenderEngine implements RenderEngine {

	private int polys = 0;
	private int drawCalls = 0;

	private static JFrame frmMain;
	private static Canvas display;

	public void init() {

		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);

		FloatBuffer ambient = toFloatBuffer(new float[] {0.2f, 0.2f, 0.2f, 1f});
		FloatBuffer light = toFloatBuffer(new float[] {0.5f, 0.5f, 0.5f, 1f});

		glShadeModel(GL_FLAT);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_RESCALE_NORMAL);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, ambient);
		glLight(GL_LIGHT0, GL_DIFFUSE, light);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glLight(GL_LIGHT0, GL_POSITION,
				toFloatBuffer(new float[] {127, 127, 500, 1}));

		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glClearColor(0.25f, 0.25f, 0.25f, 1f);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}

	@Override
	public void start() {

		// Clear the screen and depth buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public int render(RenderContext renderContext) {

//		System.out.println(getCurrentMatrix().toString());
		long time = Engine.getTime();

		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(toFloatBuffer(renderContext.getCamera().getProjection().m));

		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(toFloatBuffer(renderContext.getCamera().getTransform().m));

		polys = 0;

		if (Display.wasResized()) {
			// wasResized = true
		} else {
			// wasResized = false
		}

		setColor(Color.WHITE);

		for (InstancedModel iModel : renderContext.getModelGroup().getIModels()) {

			ModelLoader model = iModel.model;
			List<InstanceAttributes> instanceAttributes = iModel.attributes;

			OpenGLModelData openGLModelData = OpenGLModelData.getOpenGLModelData(model);

			glBindVertexArray(openGLModelData.VAOID);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, openGLModelData.VBOIID);

			if (openGLModelData.normals)
				glEnableClientState(GL_NORMAL_ARRAY);
			if (openGLModelData.colors)
				glEnableClientState(GL_COLOR_ARRAY);
			if (openGLModelData.textures) {
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				glBindTexture(GL_TEXTURE_2D, OpenGLTextureData.getOpenGLTextureData(model.texture).id);
			}

			for (InstanceAttributes instanceAttribute : instanceAttributes) {

				glPushMatrix();

				//load the model translation matrix
				float[] m = instanceAttribute.getTransform().m;

				glMultMatrix(toFloatBuffer(m));

				// Draw the vertices
				glDrawElements(GL_TRIANGLES, model.indices.getSize() * model.indices.getStride(),
						GL_UNSIGNED_INT, 0);

				polys += model.indices.getSize();

				glPopMatrix();
			}

			// Put everything back to default (deselect)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindVertexArray(0);

			glDisableClientState(GL_NORMAL_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);

			glBindTexture(GL_TEXTURE_2D, 0);
		}

		// System.out.println(polys + " polygons");

		GLErrorHelper.checkError();
		return (int) (Engine.getTime() - time);
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

		init();
	}

	@Override
	public void renderUI(Container view) {

		renderElement(view);
//		System.out.println();
	}

	private void renderElement(GUIElement element) {

//		System.out.println(element);

		//render box
		renderBox(element.getBox());

		// recursively render children
		if (element instanceof Container) {

			for (GUIElement child : ((Container) element).getChildren()) {

				renderElement(child);
			}
		}
	}

	private void renderBox(ContextBox box) {

		if (box == null)
			return;

		if (box.color != null) {
			setColor(box.color);

			if (box.color.getAlpha() != 255) {
				glEnable(GL_BLEND);
			} else {
				glDisable(GL_BLEND);
			}
		}

		if (box.texture != null) {
			drawTexture(box, box.texture);
		} else if (box.color != null) {

			drawRect(box);
		}

		if (box.texts != null) {

			for (ContextBox.Text text : box.texts) {

				GraphicsFont font = text.font;
				Color fontColor = text.color;
				setColor(fontColor);

				drawString(text.x, text.y, font, text.text);
			}
		}

		if (box.subBoxes != null) {

			for (ContextBox subBox : box.subBoxes)
				renderBox(subBox);
		}
	}

	private void drawRect(Rectangle bounds) {

		glBegin(GL_QUADS);
		glVertex2i(bounds.x, bounds.y);
		glVertex2i(bounds.x, bounds.y + bounds.height);
		glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
		glVertex2i(bounds.x + bounds.width, bounds.y);
		glEnd();
	}

	private void drawTexture(Rectangle bounds, Texture texture) {

		glEnable(GL_BLEND);
		OpenGLTextureData textureData = OpenGLTextureData.getOpenGLTextureData(texture);
		textureData.bind();

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2i(bounds.x, bounds.y);
		glTexCoord2f(0, texture.getHeightRatio());
		glVertex2i(bounds.x, bounds.y + bounds.height);
		glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
		glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
		glTexCoord2f(texture.getWidthRatio(), 0);
		glVertex2i(bounds.x + bounds.width, bounds.y);
		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private void drawString(int x, int y, GraphicsFont font, String text) {

		FontMetrics metrics = font.getFontMetrics();
		Texture atlas = font.getAtlas();
		OpenGLTextureData textureData = OpenGLTextureData.getOpenGLTextureData(font.getAtlas());
		textureData.bind();

		Rectangle placement;
		int advance = x;

		Rectangle[] allBounds = font.getBounds();

		glEnable(GL_BLEND);
		for (int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);

			if (c > 255)
				c = ' ';

			Rectangle bounds = allBounds[c];

			if (bounds != null) {

				placement = bounds.getBounds();
				placement.x = advance;
				placement.y = y;

				glBegin(GL_QUADS);

				glTexCoord2f(atlas.xRatio(bounds.x), atlas.yRatio(bounds.y));
				glVertex2i(placement.x, placement.y);

				glTexCoord2f(atlas.xRatio(bounds.x), atlas.yRatio(bounds.y + bounds.height));
				glVertex2i(placement.x, placement.y + placement.height);

				glTexCoord2f(atlas.xRatio(bounds.x + bounds.width), atlas.yRatio(bounds.y + bounds.height));
				glVertex2i(placement.x + placement.width, placement.y + placement.height);

				glTexCoord2f(atlas.xRatio(bounds.x + bounds.width), atlas.yRatio(bounds.y));
				glVertex2i(placement.x + placement.width, placement.y);

				glEnd();
			}

			advance += metrics.charWidth(c);
		}
		glDisable(GL_BLEND);
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

		return "opengl " + glGetString(GL_VERSION);
	}

	@Override
	public String getRenderDevice() {
		return glGetString(GL_RENDERER);
	}

	private void setColor(Color color) {

		glColor4f(((float) color.getRed())/255,
				((float) color.getGreen())/255,
				((float) color.getBlue())/255,
				((float) color.getAlpha())/255);
	}

	private FloatBuffer toFloatBuffer(float[] floatArray) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
		buffer.put(floatArray);
		buffer.flip();

		return buffer;
	}

	//for debug
	private Mat4 getCurrentMatrix(int glMatrix) {

		FloatBuffer matrix = BufferUtils.createFloatBuffer(Mat4.TOTAL_SIZE);
		glGetFloat(glMatrix, matrix);

		float[] array = new float[Mat4.TOTAL_SIZE];
		matrix.get(array);

		return new Mat4(array);
	}

	public static void screenshot() {

		ByteBuffer array = BufferUtils.createByteBuffer(3
				* Settings.windowWidth * Settings.windowHeight);
		glReadPixels(0, 0, Settings.windowWidth, Settings.windowHeight,
				GL_RGB, GL_UNSIGNED_BYTE, array);

		new Thread(new ScreenshotRunnable(array.slice())).start();
	}

	private static class OpenGLModelData {

		int VAOID;
		int VBOID;
		int NBOID;
		int CBOID;
		int VBOIID;
		int TBOID;

		boolean normals;
		boolean textures;
		boolean colors;

		OpenGLModelData(ModelLoader model) {

			VAOID = glGenVertexArrays();
			glBindVertexArray(VAOID);

			glEnableClientState(GL_VERTEX_ARRAY);

			if (!model.normals.isEmpty()) {
				normals = true;
				glEnableClientState(GL_NORMAL_ARRAY);
			} else {
				glDisableClientState(GL_NORMAL_ARRAY);
			}
			if (!model.color.isEmpty()) {
				colors = true;
				glEnableClientState(GL_COLOR_ARRAY);
			} else {
				glDisableClientState(GL_COLOR_ARRAY);
			}
			if (!model.textureCoords.isEmpty() && model.texture != null) {
				textures = true;
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			} else {
				glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			}

			FloatBuffer verticesBuffer = model.vertices.toFloatBuffer();
			VBOID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, VBOID);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexPointer(3, GL_FLOAT, 0, 0);

			if (normals) {
				FloatBuffer normalsBuffer = model.normals.toFloatBuffer();
				NBOID = glGenBuffers();
				glBindBuffer(GL_ARRAY_BUFFER, NBOID);
				glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
				glNormalPointer(GL_FLOAT, 0, 0);
			}

			if (colors) {
				ByteBuffer colorBuffer = ByteBuffer.allocateDirect(model.color.size() * 4);
				IntBuffer intBuffer = colorBuffer.asIntBuffer();
				for (int i = 0; i < model.color.size(); i++)
					intBuffer.put(model.color.get(i));
				colorBuffer.rewind();
				CBOID = glGenBuffers();
				glBindBuffer(GL_ARRAY_BUFFER, CBOID);
				glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
				glColorPointer(4, GL_UNSIGNED_BYTE, 0, 0);
			}

			if (textures) {
				glEnable(GL_TEXTURE_2D);
				FloatBuffer textureBuffer = model.textureCoords.toFloatBuffer();
				TBOID = glGenBuffers();
				glBindBuffer(GL_ARRAY_BUFFER, TBOID);
				glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
				glTexCoordPointer(2, GL_FLOAT, 0, 0);
			}

			// Deselect (bind to 0) the VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			// Deselect (bind to 0) the VAO
			glBindVertexArray(0);

			IntBuffer indicesBuffer = model.indices.toIntBuffer();
			VBOIID = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VBOIID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}

		private static OpenGLModelData getOpenGLModelData(ModelLoader model) {

			OpenGLModelData data;

			if (model.getUserData() instanceof OpenGLModelData) {

				data =  (OpenGLModelData) model.getUserData();
			} else {

				data = new OpenGLModelData(model);
				model.setUserData(data);
				model.setReleaseListener(data::release);
			}

			return data;
		}

		void release() {

			glDeleteBuffers(VBOID);
			glDeleteBuffers(VBOIID);
			glDeleteVertexArrays(VAOID);
		}
	}

	private static class OpenGLTextureData {

		private int id = 0;

		OpenGLTextureData(Texture texture) {

			ByteBuffer buffer = texture.getTexture();

			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);//GL_LINEAR_MIPMAP_LINEAR when mipmaps are made
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.getActualWidth(), texture.getActualHeight(), 0, GL_RGBA,
					GL_UNSIGNED_BYTE, buffer);
			// build mipmaps somehow
		}

		void bind() {

			glBindTexture(GL_TEXTURE_2D, id);
		}

		void release() {

			glDeleteTextures(id);
		}

		private static OpenGLTextureData getOpenGLTextureData(Texture texture) {

			OpenGLTextureData data;

			if (texture.getUserData() instanceof OpenGLTextureData) {

				data =  (OpenGLTextureData) texture.getUserData();
			} else {

				data = new OpenGLTextureData(texture);
				texture.setUserData(data);
				texture.setReleaseListener(data::release);//fancy lambda expression
			}

			return data;
		}
	}

	//temporarily move into here.
	private static class ScreenshotRunnable implements Runnable {

		ByteBuffer array;

		ScreenshotRunnable(ByteBuffer array) {

			this.array = array;
		}

		@Override
		public void run() {

			BufferedImage image = new BufferedImage(Settings.windowWidth,
					Settings.windowHeight, BufferedImage.TYPE_INT_RGB);

			int x = 0;
			int y = Settings.windowHeight - 1;
			while (array.hasRemaining()) {
				short r = (short) (array.get() & 0xFF);
				short g = (short) (array.get() & 0xFF);
				short b = (short) (array.get() & 0xFF);
				int color = 0;
				color |= b;
				color |= g << 8;
				color |= r << 16;
				image.setRGB(x, y, color);
				x++;
				if (x == Settings.windowWidth) {
					x = 0;
					y--;
				}
			}

			File screenshotFolder = new File("screenshots");
			if (!screenshotFolder.exists()) screenshotFolder.mkdir();

			java.util.Calendar now = java.util.Calendar.getInstance();
			File file = new File("screenshots" + File.separator
					+ String.format("%04d", now.get(Calendar.YEAR)) + "-"
					+ String.format("%02d", now.get(Calendar.MONTH)) + "-"
					+ String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + "T"
					+ String.format("%02d", now.get(Calendar.HOUR)) + "."
					+ String.format("%02d", now.get(Calendar.MINUTE)) + "."
					+ String.format("%02d", now.get(Calendar.SECOND)) + ".png");
			System.out.println("screenshot: " + file);

			try {
				ImageIO.write(image, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}