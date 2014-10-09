package main;

import graphics.Model;
import graphics.Shader;
import graphics.Texture;

import java.io.File;
import java.util.HashMap;

public class AssetManager {
	
	private static final String ROOT = "res" + File.separator;
	private static final String MODEL_ROOT = ROOT + "models" + File.separator;
	private static final String TEXTURE_ROOT = "textures" + File.separator;
	private static final String SHADER_ROOT = "shaders" + File.separator;
	
	enum assetType{
		MODEL,
		TEXTURE,
		SHADER
	}
	
	private static HashMap<String, Model> modelMap = new HashMap<String, Model>();
	private static HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
	private static HashMap<String, Shader> shaderMap = new HashMap<String, Shader>();
	
	public static Model getModel(String name) {
		
		Model model = modelMap.get(name);
		
		if (model == null) {
			return new Model();
		} else {
			return model;
		}
	}
	
	public static void loadAll() {
		
		File modelPath = new File(MODEL_ROOT);
		File texturePath = new File(TEXTURE_ROOT);
		File shaderPath = new File(SHADER_ROOT);
		
		if (modelPath.exists() && modelPath.isDirectory()) {
			String[] models = modelPath.list();
			
			for(String model : models) {
				
				loadAsset(assetType.MODEL, model);
			}
		} else {
			modelPath.mkdir();
		}
		
		if (texturePath.exists() && texturePath.isDirectory()) {
			String[] textures = texturePath.list();
			
			for(String texture : textures) {
				
				loadAsset(assetType.MODEL, texture);
			}
		} else {
			modelPath.mkdir();
		}
		
		if (shaderPath.exists() && shaderPath.isDirectory()) {
			String[] shaders = shaderPath.list();
			
			for(String shader : shaders) {
				
				loadAsset(assetType.MODEL, shader);
			}
		} else {
			modelPath.mkdir();
		}
	}
	
	public static void loadAsset(assetType type, String fileName) {
		
		File file;
		
		switch (type) {
		case MODEL:
			file = new File(MODEL_ROOT + fileName);
			
			if (file.exists()) {
				
				modelMap.put(fileName,new Model(file));
			}
			break;
		case TEXTURE:
			file = new File(TEXTURE_ROOT + fileName);

			if (file.exists()) {

				textureMap.put(fileName, new Texture(file));
			}
			break;
		case SHADER:
			file = new File(SHADER_ROOT + fileName);

			if (file.exists()) {

				shaderMap.put(fileName, new Shader(file));
			}
			break;
		}
	}
}
