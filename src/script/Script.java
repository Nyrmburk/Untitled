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
 * <a href=http://docs.oracle.com/javase/7/docs/technotes/guides/scripting/programmer_guide/>
 * http://docs.oracle.com/javase/7/docs/technotes/guides/scripting/programmer_guide/</a>
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
	String code;

	public Script() {
	}

	public Script(File file) {
		if (factory == null)
			factory = new ScriptEngineManager();
		if (compEngine == null) {
			javax.script.ScriptEngine engine = factory
					.getEngineByName(LANGUAGE);
			// engine.getContext().setWriter(writer);
			compEngine = (Compilable) engine;
		}

		this.file = file;
		String code = HEADER + load(file);
		this.code = code;
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
				if (read != null)
					read.close();
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

	/**
	 * Evaluate the script. Use <code>threaded = true</code> for scripts that do
	 * not rely on synchronization or return values. In order to get a return
	 * value, the script must be ran non-multithreaded;
	 * <code>threaded = false</code>. It is preferred for performance reasons to
	 * run <code>threaded = true</code> by default. Bindings are variables that
	 * the script has access to. It is very expensive to bind variables, costing
	 * tens of milliseconds. It is not recommended to use in a game loop because
	 * every call will cause a stutter. Use
	 * <code>Bindings bindings = new SimpleBindings()</code> to create one and
	 * <code>bindings.put("name", Object);</code> to add values to it. Bound
	 * variables are accessed in the script by <code>name.Foo()</code> where
	 * <code>name</code> is the object given previously. <code>bindings</code>
	 * can be <code>null</code> when no bindings are needed.
	 * 
	 * @param threaded
	 *            Whether or not to use a separate thread for execution
	 * @param bindings
	 *            A map of variables to insert into the script
	 * @return null for failure, null for multithreaded, and whatever object the
	 *         script returns when not multithreaded
	 */
	public Object eval(boolean threaded, final Bindings bindings) {

		if (compiledScript == null) {
			String message = "Script not compliled: cannot run";
			System.err.println(message);
			errorlog.append(message);
			return null;
		}

		if (threaded) {
			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
//						ScriptContext newContext = new SimpleScriptContext();
//				        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
//				        engineScope.put("uuid", 13545432545689456l);
						compiledScript.eval(bindings);
					} catch (ScriptException e) {

						String message = "Error in script \"" + file.getName()
								+ "\":\r\n	" + e.getCause();
						System.err.println(message);
						errorlog.append(message);
					}
				}

			}, file.getName()).start();
		} else {

			try {
				return compiledScript.eval(bindings);
			} catch (ScriptException e) {

				String message = "Error in script \"" + file.getName()
						+ "\":\r\n	" + e.getCause();
				System.err.println(message);
				errorlog.append(message);
			}
		}

		return null;
	}
}
