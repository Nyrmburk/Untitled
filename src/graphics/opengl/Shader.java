package graphics.opengl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL20;

/**
 * Make an object containing information about a GLSL shader.
 * @author Christopher Dombroski
 *
 */
public class Shader {

	File file;
	String shader;
	int shaderType = -1;
	boolean ready = false;
	int shaderReference;
	String log;
	
	public Shader(){
	}
	
	public Shader(File file, int shaderType) {
		
		this.file = file;
		shader = load(file);
		this.shaderType = shaderType;
		
		createShader();
		setShaderSource();
		compileShader();
	}
	
	public void refresh() {
		
		shader = load(file);
		if (ready) {
			
			setShaderSource();
			compileShader();
		}
	}
	
	public void setType(int shaderType) {
		
		this.shaderType = shaderType;
		
	}
	
	private void createShader() {
		
		if (shaderType != -1) {
			
			createShader(shaderType);
		}
	}
	
	private void createShader(int shaderType) {
		
		this.shaderType = shaderType;
		this.shaderReference = GL20.glCreateShader(shaderType);
		if (shaderReference == 0) {
			ready = false;
		}
	}
	
	private void setShaderSource() {
		
		if (shader != null && !shader.isEmpty()) {
			
			setShaderSource(shader);
		}
	}
	
	private void setShaderSource(String shader) {
		
		this.shader = shader;
		if (shaderReference != 0) {
			
			GL20.glShaderSource(shaderReference, shader);
		}
	}
	
	private void compileShader() {
		
		if (shaderReference != 0) {
			
			GL20.glCompileShader(shaderReference);
			ready = true;
		}
		log = GL20.glGetShaderInfoLog(shaderReference, GL20.GL_INFO_LOG_LENGTH);
		if (!log.isEmpty()) {
			
			System.out.println(log.trim());
		}
	}
	
	public void delete() {
		
		GL20.glDeleteShader(shaderReference);
	}
	
	private String load(File f) {
		
		StringBuilder shaderBuilder = new StringBuilder();
		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(f);
			read = new BufferedReader(reader);

			while ((currentLine = read.readLine()) != null) {
				shaderBuilder.append(currentLine + System.lineSeparator());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (read != null)
					read.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return shaderBuilder.toString();
	}
}