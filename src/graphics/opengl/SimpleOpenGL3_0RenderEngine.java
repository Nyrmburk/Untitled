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
import org.lwjgl.opengl.DisplayMode;

public class SimpleOpenGL3_0RenderEngine implements RenderEngine {

	private int polys = 0;
	private int drawCalls = 0;

	private JFrame frmMain;
	private Canvas display;
	private Rectangle viewport;

	public void init() {

		Thread.setDefaultUncaughtExceptionHandler(new Thread.
				UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
		});

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
	public void render(RenderContext renderContext) {

//		System.out.println(getCurrentMatrix().toString());

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
	}

	private graphics.Display convertDisplay(DisplayMode displayMode) {

		Dimension resolution = new Dimension(displayMode.getWidth(), displayMode.getHeight());
		return new graphics.Display(resolution, displayMode.getFrequency());
	}

	@Override
	public graphics.Display[] getDisplays() {

		graphics.Display[] displays = null;
		try {

			DisplayMode[] displayModes = Display.getAvailableDisplayModes();
			displays = new graphics.Display[displayModes.length];

			for (int i = 0; i < displayModes.length; i++)
				displays[i] = convertDisplay(displayModes[i]);

		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		return displays;
	}

	@Override
	public graphics.Display getCurrentDisplay() {

		return convertDisplay(Display.getDisplayMode());
	}

	@Override
	public Rectangle getViewport() {

		return viewport;
	}

	@Override
	public void setViewport(Rectangle viewport) {

		this.viewport = viewport;
	}

	@Override
	public Rectangle getWindow() {

		return frmMain.getBounds();
	}

	@Override
	public void setWindow(Rectangle window) {

		frmMain.setBounds(window);
	}

	@Override
	public void createWindow(Rectangle viewport) {

		setViewport(viewport);
		frmMain = new JFrame();
		display = new Canvas();
		display.setBackground(Color.BLACK);
		display.setSize(viewport.getSize());
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

	public void screenshot() {

		ByteBuffer array = BufferUtils.createByteBuffer(3
				* viewport.width * viewport.height);
		glReadPixels(0, 0, viewport.width, viewport.height,
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
			glDeleteBuffers(NBOID);
			glDeleteBuffers(CBOID);
			glDeleteBuffers(TBOID);
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
	private class ScreenshotRunnable implements Runnable {

		ByteBuffer array;

		ScreenshotRunnable(ByteBuffer array) {

			this.array = array;
		}

		@Override
		public void run() {

			BufferedImage image = new BufferedImage(viewport.width, viewport.height, BufferedImage.TYPE_INT_RGB);

			int x = 0;
			int y = viewport.height - 1;
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
				if (x == viewport.width) {
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