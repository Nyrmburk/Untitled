package gui.css;

import main.Resource;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

/**
 * Created by Nyrmburk on 2/24/2016.
 */
public class CSSDocument extends Resource {

	String name;
	Document document;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void save(Path path) {
	}

	@Override
	public void load(Path path) throws IOException {

		DocumentSource docSource = new DocumentSource(null){

			BufferedInputStream reader = null;

			@Override
			public URL getURL() {

				return null;
			}

			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public InputStream getInputStream() throws IOException {

				reader = new BufferedInputStream(new FileInputStream(path.toFile()));
				return reader;
			}

			@Override
			public void close() throws IOException {

				reader.close();
			}
		};

		DOMSource parser = new DefaultDOMSource(docSource);
		try {
			document = parser.parse();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		docSource.close();
	}
}
