package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.*;

/**
 * A way to externalize different parts of the engine to a jsr 223 compliant
 * script. The engine compiles the script prior to execution and is great for
 * mods.
 * 
 * @author Christopher Dombroski
 */
public class Script {
	
	public final static String LANGUAGE = "JavaScript";
	public final static String HEADER = "'use strict';"
			+ "load('nashorn:mozilla_compat.js');";
	
	private static ScriptEngineManager factory;
	private static Compilable compEngine;
	
	File file;
	CompiledScript compiledScript;
	StringBuilder errorlog = new StringBuilder();
	
	public Script() {
	}
	
	public Script(File file) {
		if (factory == null) factory = new ScriptEngineManager();
		if (compEngine == null) {
			javax.script.ScriptEngine engine = factory
					.getEngineByName(LANGUAGE);
			// engine.getContext().setWriter(writer);
			compEngine = (Compilable) engine;
		}
		
		this.file = file;
		String code = HEADER + load(file);
		compile(code);
	}
	
	private String load(File file) {
		
		StringBuilder scriptCode = new StringBuilder();
		BufferedReader read = null;
		
		try {
			String currentLine;
			FileReader reader = new FileReader(file);
			read = new BufferedReader(reader);
			
			while ((currentLine = read.readLine()) != null) {
				scriptCode.append(currentLine + System.lineSeparator());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (read != null) read.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return scriptCode.toString();
	}
	
	private void compile(String code) {
		
		try {
			
			this.compiledScript = compEngine.compile(code);
		} catch (ScriptException e) {
			String message = "Error in script \"" + file.getName() + "\":\r\n	"
					+ e.getCause();
			System.err.println(message);
			errorlog.append(message);
		}
	}
	
	public void reload() {
		
		String code = HEADER + load(file);
		compile(code);
	}
	
	public void eval() {
		
		if (compiledScript == null) {
			String message = "Script not compliled: cannot run";
			System.err.println(message);
			errorlog.append(message);
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					compiledScript.eval();
				} catch (ScriptException e) {
					
					String message = "Error in script \"" + file.getName()
							+ "\":\r\n	" + e.getCause();
					System.err.println(message);
					errorlog.append(message);
				}
			}
			
		}).start();
	}
}
