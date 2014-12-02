package main;

import graphics.Model;
import graphics.Shader;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import script.Script;

/**
 * Load all the assets before the game starts. TODO multithread this for
 * concurrent loading of assets
 * 
 * @author Christopher Dombroski
 */
public class AssetManager {
	
	private static final String ROOT = "res" + File.separator;
	private static final String MODEL_ROOT = ROOT + "models" + File.separator;
	private static final String TEXTURE_ROOT = ROOT + "textures"
			+ File.separator;
	private static final String SHADER_ROOT = ROOT + "shaders" + File.separator;
	private static final String SCRIPT_ROOT = ROOT + "scripts" + File.separator;
	
	static HashMap<String, Model> modelMap = new HashMap<String, Model>();
	static HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
	static HashMap<String, Shader> shaderMap = new HashMap<String, Shader>();
	static HashMap<String, Script> scriptMap = new HashMap<String, Script>();
	
	static ExecutorService executor = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors() * 2);
	
	public static Model getModel(String name) {
		
		Model model = modelMap.get(name);
		
		if (model == null) {
			return new Model();
		} else {
			return model;
		}
	}
	
	public static Texture getTexture(String name) {
		
		Texture texture = textureMap.get(name);
		
		if (texture == null) {
			
			try {
				texture = BufferedImageUtil.getTexture("missing_texture",
						generateMissingImage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return texture;
	}
	
	public static Shader getShader(String name) {
		
		Shader shader = shaderMap.get(name);
		
		if (shader == null) {
			return new Shader();
		} else {
			return shader;
		}
	}
	
	public static Script getScript(String name) {
		
		Script script = scriptMap.get(name);
		
		if (script == null) {
			return new Script();
		} else {
			return script;
		}
	}
	
	public static void loadAll() {
		
		long time = System.currentTimeMillis();
		
		File modelPath = new File(MODEL_ROOT);
		File texturePath = new File(TEXTURE_ROOT);
		File shaderSubPath = new File(SHADER_ROOT);
		File scriptPath = new File(SCRIPT_ROOT);
		
		if (modelPath.exists() && modelPath.isDirectory()) {
			String[] models = modelPath.list();
			
			for (String model : models) {
				
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						
						loadModel(model);
					}
				});
				
				// loadModel(model);
			}
		} else {
			modelPath.mkdir();
		}
		
		if (scriptPath.exists() && scriptPath.isDirectory()) {
			String[] scripts = scriptPath.list();
			
			for (String script : scripts) {
				
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						
						loadScript(script);
					}
				});
				
				// loadScript(script);
			}
		} else {
			scriptPath.mkdir();
		}
		
		if (texturePath.exists() && texturePath.isDirectory()) {
			String[] textures = texturePath.list();
			
			for (String texture : textures) {
				
				loadTexture(texture);
			}
		} else {
			texturePath.mkdir();
		}
		
		if (shaderSubPath.exists() && shaderSubPath.isDirectory()) {
			String[] shaderPaths = shaderSubPath.list();
			
			for (String shaderPath : shaderPaths) {
				
				for (String shader : new File(shaderSubPath + File.separator
						+ shaderPath).list()) {
					
					loadShader(shaderPath, shader);
				}
			}
		} else {
			shaderSubPath.mkdir();
		}
		
		executor.shutdown();
		
		try {
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("total load time: "
				+ (System.currentTimeMillis() - time) + "ms");
	}
	
	public static void loadModel(String fileName) {
		
		File file = new File(MODEL_ROOT + fileName);
		
		if (file.exists() && file.isFile()) {
			
			long time = System.currentTimeMillis();
			modelMap.put(fileName, new Model(file));
			System.out.println("loaded " + file + ", "
					+ (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	public static void loadTexture(String fileName) {
		
		File file = new File(TEXTURE_ROOT + fileName);
		
		if (file.exists() && file.isFile()) {
			
			long time = System.currentTimeMillis();
			
			Texture texture = null;
			
			try {
				
				BufferedImage image = javax.imageio.ImageIO.read(file);
				texture = BufferedImageUtil.getTexture(fileName, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			textureMap.put(fileName, texture);
			System.out.println("loaded " + file + ", "
					+ (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	public static void loadShader(String parentDirectory, String fileName) {
		
		File file = new File(SHADER_ROOT + parentDirectory + File.separator
				+ fileName);
		
		if (file.exists() && file.isFile()) {
			
			long time = System.currentTimeMillis();
			shaderMap.put(fileName, new Shader(file,
					getShaderType(parentDirectory)));
			System.out.println("loaded " + file + ", "
					+ (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	public static void loadScript(String fileName) {
		
		File file = new File(SCRIPT_ROOT + fileName);
		
		if (file.exists() && file.isFile()) {
			
			long time = System.currentTimeMillis();
			scriptMap.put(fileName, new Script(file));
			System.out.println("loaded " + file + ", "
					+ (System.currentTimeMillis() - time) + "ms");
		}
	}
	
	private static int getShaderType(String directory) {
		
		final String VERTEX = "vertex";
		final String TESSELATION_CONTROL = "tesselation control";
		final String TESSELATION_EVALUATION = "tesselation evaluation";
		final String GEOMETRY = "geometry";
		final String FRAGMENT = "fragment";
		final String COMPUTE = "compute";
		
		int type = 0;
		
		switch (directory) {
		case VERTEX:
			type = GL20.GL_VERTEX_SHADER;
			break;
		case TESSELATION_CONTROL:
			type = GL40.GL_TESS_CONTROL_SHADER;
			break;
		case TESSELATION_EVALUATION:
			type = GL40.GL_TESS_EVALUATION_SHADER;
			break;
		case GEOMETRY:
			type = GL32.GL_GEOMETRY_SHADER;
			break;
		case FRAGMENT:
			type = GL20.GL_FRAGMENT_SHADER;
			break;
		case COMPUTE:
			type = GL43.GL_COMPUTE_SHADER;
			break;
		}
		
		return type;
	}
	
	private static BufferedImage generateMissingImage() {
		
		BufferedImage image = new BufferedImage(32, 32,
				BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics2D g = image.createGraphics();
		g.setColor(java.awt.Color.MAGENTA);
		g.fillRect(0, 0, 16, 16);
		g.fillRect(16, 16, 32, 32);
		g.dispose();
		
		return image;
	}
}
