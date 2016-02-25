package main;

import java.io.File;

/**
 * Created by Nyrmburk on 2/22/2016.
 */
public interface Resource {

	String getName();

	Resource save(File file);
	Resource load(File file);
}
