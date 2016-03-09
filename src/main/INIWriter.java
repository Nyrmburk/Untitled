package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nyrmburk on 2/20/2016.
 */
public class INIWriter {

	//TODO remove and replace with java.utils.Properties

	public static void write(Map<?, ?> map, File file) {

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<?, ?> entry :  map.entrySet()) {

				writer.write(entry.getKey() + "=" + entry.getValue() + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static Map<String, String> read(File file) {

		BufferedReader reader = null;
		Map<String, String> map = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			map = new HashMap<String, String>();

			while (reader.ready()) {

				String line = reader.readLine();
				int equals = line.indexOf('=');
				map.put(line.substring(0, equals).trim(), line.substring(equals+1, line.length()).trim());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return map;
	}
}
