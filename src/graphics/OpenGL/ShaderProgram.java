package graphics.opengl;

import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

/**
 * Link Shaders into a shader program.
 * @author Christopher Dombroski
 *
 */
public class ShaderProgram {
	
	int programReference;
	String log;
	
	public static ArrayList<ShaderProgram> programList = new ArrayList<ShaderProgram>();
	
	ArrayList<Shader> shaderList = new ArrayList<Shader>();
	
	public ShaderProgram() {
		
		programReference = GL20.glCreateProgram();
		programList.add(this);
	}
	
	public void attachShader(Shader shader) {
		
		shaderList.add(shader);
		GL20.glAttachShader(programReference, shader.shaderReference);
	}
	
	public void link() {
		
		GL20.glLinkProgram(programReference);
		log = GL20.glGetProgramInfoLog(programReference, GL20.GL_INFO_LOG_LENGTH);
		if (!log.isEmpty()) {
			
			System.out.println(log.trim());
		}
	}
	
	public void delete() {
		
		for (Shader shader : shaderList) {
			
			shader.delete();
		}
		
		GL20.glDeleteProgram(programReference);
	}
	
	public static void deleteAll() {
		
		for (ShaderProgram shaderProgram : programList) {
			
			shaderProgram.delete();
		}
	}
	
	public void use() {
		
		GL20.glUseProgram(programReference);
	}
	
	public void release() {
		
		GL20.glUseProgram(0);
	}
	
	public void rebuild() {
		
		for (Shader shader : shaderList) {
			
			GL20.glDetachShader(programReference, shader.shaderReference);
			shader.refresh();
			GL20.glAttachShader(programReference, shader.shaderReference);
		}
		
		link();
	}
}
