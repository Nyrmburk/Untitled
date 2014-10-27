package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Helpful in debugging openGL. Prints out the type of exception that has 
 * happened.
 * @author Christopher Dombroski
 *
 */
public class GLErrorHelper {
	
	public static String getErrorType(int error) {
		String errorType;
		
		switch (error) {
		case GL11.GL_NO_ERROR:
			errorType = "no error";
			break;
		case GL11.GL_INVALID_ENUM:
			errorType = "invalid enum";
			break;
		case GL11.GL_INVALID_VALUE:
			errorType = "invalid value";
			break;
		case GL11.GL_INVALID_OPERATION:
			errorType = "invalid operation";
			break;
		case GL11.GL_STACK_OVERFLOW:
			errorType = "stack overflow";
			break;
		case GL11.GL_STACK_UNDERFLOW:
			errorType = "stack underflow";
			break;
		case GL11.GL_OUT_OF_MEMORY:
			errorType = "out of memory";
			break;
		case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
			errorType = "invalid framebuffer operation";
			break;
		case 0x8031: //should be GL12.GL_TABLE_TOO_LARGE but does not exist
			errorType = "table too large";
			break;
		default:
			errorType = "unknown error";
			break;
		}
		
		return errorType;
	}
	
	public static void checkError() {
		
		int error;
		while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR) {
			System.out.println("GL error: "
					+ String.format("%#X", error & 0x0FFFFF) + ", "
					+ GLErrorHelper.getErrorType(error));
		}
	}
}
