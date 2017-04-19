package input;

import main.INIWriter;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nyrmburk on 2/22/2016.
 */
public class Binding {

	// TODO refactor that const into here
	private static final File path = new File("res\\input");

	private static Map<String, Object> bindings = new HashMap<>();
	private static Map<String, InputInterface> interfaces = new HashMap<>();

	public static void delegate(Input input) {

		interfaces.get(input.getName()).addInput(input, bindings.get(input.getName()));
	}

	public static Object getBindingFromName(String name) {

		return bindings.get(name);
	}

	public static InputInterface getInterfaceFromName(String name) {

		return interfaces.get(name);
	}

	public static void load(File file) {

		Map<String, String> bindingMap = INIWriter.read(file);

		for (Map.Entry<String, String> entry : bindingMap.entrySet()) {

			String[] parts = entry.getValue().split(":");
			String interfaceName = parts[0];
			String bindingName = parts[1];

			Object binding = null;

			for (InputInterface inputInterface : InputInterface.getInterfaces()) {

				if (inputInterface.getName().equals(interfaceName)) {

					binding = inputInterface.getBinding(bindingName);
					interfaces.put(entry.getKey(), inputInterface);
					break;
				}
			}

			bindings.put(entry.getKey(), binding);
		}

		System.out.println(bindings);
	}

	public static void save() {

//		for (InputGroup group : InputGroup.getGroups()) {
//
//
//		}
	}

	public static Map<String, Object> getBindings() {

		return Collections.unmodifiableMap(bindings);
	}
}
