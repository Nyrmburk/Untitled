package graphics;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import main.Vector;

/**
 * The class that loads and is referred to for model data including vertex, 
 * normal, texture, and others.
 * @author Christopher Dombroski
 *
 */
public class Model {

	public ArrayList<float[]> verticesList = new ArrayList<float[]>(5000);
	public ArrayList<float[]> normalsList = new ArrayList<float[]>(5000);
	public ArrayList<float[]> surfNormList = new ArrayList<float[]>(5000);
	public ArrayList<float[]> textureCoordsList = new ArrayList<float[]>(5000);
	public ArrayList<float[]> paramVerticesList = new ArrayList<float[]>(500);
	public ArrayList<float[]> colorAmbientList = new ArrayList<float[]>(100);
	public ArrayList<float[]> colorDiffuseList = new ArrayList<float[]>(100);
	public ArrayList<float[]> colorSpecularList = new ArrayList<float[]>(100);
	public ArrayList<int[]> indicesList = new ArrayList<int[]>(500);
	public ArrayList<Integer> shadingList = new ArrayList<Integer>(100);
	public ArrayList<Integer> illuminationList = new ArrayList<Integer>(100);
	public ArrayList<Float> materialDissolveList = new ArrayList<Float>(100);
	public ArrayList<String> materialList = new ArrayList<String>(20);
	public ArrayList<String> objectList = new ArrayList<String>(10);
	public ArrayList<String> groupList = new ArrayList<String>(10);

	public float[] vertices;
	public float[] normals;
	public float[] textureCoords;
	public float[] paramVertices;
	public float[] colorAmbient;
	public float[] colorDiffuse;
	public float[] colorSpecular;
	public int[] indices;

	Texture texture;

	byte indicesFormat = -1;
	byte indicesStride = 3;
	byte vertexStride;
	byte normalStride = 3;
	byte textureStride;

	final byte V = 0;
	final byte VN = 1;
	final byte VT = 2;
	final byte VTN = 3;
	
	/**
	 * Open and read the file with the supplied filename.
	 * @param inputFileName
	 */

	public void load(String inputFileName) {
		File f = new File("res\\models\\" + inputFileName + ".obj");
		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(f);
			read = new BufferedReader(reader);

			long time = System.currentTimeMillis();
			while ((currentLine = read.readLine()) != null) {
				fileParser(currentLine);
			}

			initAll();
			
			System.out.println(System.currentTimeMillis() - time + 
					" milliseconds to load " + verticesList.size() + 
					" vertices and " + indicesList.size() + 
					" faces");

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
	}

	/**
	 * Parse through a .obj file.
	 * 
	 * @param fileText
	 */
	private void fileParser(String fileText) {

		String[] line = fileText.split(" ");
		line = removeEmpty(line);

		if (line.length == 0) {
			return;
		}

		switch (line[0].toLowerCase()) {

		case "#":
			break;
		case "o":
			if (line.length > 1) {
				objectList.add(line[1]);
			} else {
				objectList.add("object");
			}
			break;
		case "v":
			vertexStride = (byte) (line.length - 1);
			verticesList.add(floats(line));
			break;
		case "vt":
			textureStride = (byte) (line.length - 1);
			textureCoordsList.add(floats(line));
			break;
		case "vn":
			normalStride = (byte) (line.length - 1);
			normalsList.add(floats(line));
			break;
		case "vp":
			paramVerticesList.add(floats(line));
			break;
		case "f":
			indicesList.add(face(line));
			break;
		case "g":
			if (line.length > 1) {
				groupList.add(line[1]);
			} else {
				groupList.add("group");
			}
			break;
		case "newmtl":
			materialList.add(line[1]);
			break;
		case "ka":
			colorAmbientList.add(floats(line));
			break;
		case "kd":
			colorDiffuseList.add(floats(line));
			break;
		case "ks":
			colorSpecularList.add(floats(line));
			break;
		case "s":
			if (line[1].equals("off") == false) {
				shadingList.add(Integer.valueOf(line[1]));
			} else {
				shadingList.add(-1);
			}
			break;
		case "d":
			materialDissolveList.add(Float.parseFloat(line[1]));
			break;
		case "tr":
			materialDissolveList.add(Float.parseFloat(line[1]));
			break;
		case "illum":
			illuminationList.add(Integer.valueOf(line[1]));
			break;
		}
	}

	/**
	 * A worker method to parse a String[] into a float[]
	 * @param line A String[] containing several numbers
	 * @return A float[] from the supplied String[]
	 */
	private float[] floats(String[] line) {
		float[] f = new float[line.length - 1];
		for (byte i = 0; i < f.length; i++) {
			f[i] = Float.parseFloat(line[i + 1]);
		}
		return f;
	}

	/**
	 * Because a .obj file has 4 different formats for indices, face must figure 
	 * out which one it is and parse through it. The face information represents 
	 * the index of vertices that form a polygon on the model.
	 * @param line A String[] of integers containing face info
	 * @return An int[] of face info
	 */
	private int[] face(String[] line) {
		int sides = line.length - 1;
		String[] working;

		if (indicesFormat == -1) {
			if (line[1].contains("//")) {
				indicesFormat = VN;
			} else if (line[1].contains("/")) {
				working = line[1].split("/");
				if (working.length == 2) {
					indicesFormat = VT;
				} else if (working.length == 3) {
					indicesFormat = VTN;
				}

			} else {
				indicesFormat = V;
			}
		}

		int[] indices = new int[line.length-1];

		switch (indicesFormat) {
		case (V):
			for (int i = 0; i < sides; i++) {
				indices[i] = Integer.parseInt(line[i + 1]) - 1;
			}
			triangulate(indices);
			break;
		case (VN):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("//");
//				indices[i * 2] = Integer.parseInt(working[0]) - 1;
//				indices[i * 2 + 1] = Integer.parseInt(working[1]) - 1;
			}
			triangulate(indices);
			break;
		case (VT):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("/");
				indices[i] = Integer.parseInt(working[0]) - 1;
//				indices[i * 2] = Integer.parseInt(working[0]) - 1;
//				indices[i * 2 + 1] = Integer.parseInt(working[1]) - 1;
			}
			triangulate(indices);
			break;
		case (VTN):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("/");
				indices[i] = Integer.parseInt(working[0]) - 1;
//				indices[i * 3] = Integer.parseInt(working[0]) - 1;
//				indices[i * 3 + 1] = Integer.parseInt(working[1]) - 1;
//				indices[i * 3 + 2] = Integer.parseInt(working[2]) - 1;
			}
			triangulate(indices);
			break;
		}

		return indices;
	}
	
	/**
	 * For a large number of models there is no normal data so it must be 
	 * generated. It finds the surface normals and adds them up to find the 
	 * vertex normal. Normal info is necessary for shading.
	 * @return An ArrayList<float[]> containing the normals
	 */
	private void genNormals() {

		ArrayList<ArrayList<float[]>> normalBuffer = new ArrayList<ArrayList<float[]>>(verticesList.size());
		
		while (normalBuffer.size() <= verticesList.size()) {
			normalBuffer.add(new ArrayList<float[]>());
		}

		for( int i = 0; i < indicesList.size(); i++ ) {
		  // get the three vertices that make the faces
		  float[] p1 = verticesList.get(indicesList.get(i)[0]);
		  float[] p2 = verticesList.get(indicesList.get(i)[1]);
		  float[] p3 = verticesList.get(indicesList.get(i)[2]);

		  float[] v1 = Vector.cSubVector(p2, p1);
		  float[] v2 = Vector.cSubVector(p3, p1);
		  float[] normal = Vector.cCrossVector(v1, v2);

		  normal = Vector.normalize(normal);

		  // Store the face's normal for each of the vertices that make up the face.
		  normalBuffer.get(indicesList.get(i)[0]).add(normal);
		  normalBuffer.get(indicesList.get(i)[1]).add(normal);
		  normalBuffer.get(indicesList.get(i)[2]).add(normal);
		  
		  //Store the surface normal
		  surfNormList.add(normal);
		}

		// Now loop through each vertex vector, and average out all the normals stored.

		for( int i = 0; i < verticesList.size(); i++) {
			float[] normal = new float[3];
			for( int j = 0; j < normalBuffer.get(i).size(); j++ ) {
				normal = Vector.cAddVector(normal, normalBuffer.get(i).get(j));
			}
			
			normal = Vector.normalize(normal);
			
			normalsList.add(i, normal);
				
		}
	}
	
	/**
	 * Input an n-sided convex polygon and turn it into a bunch of triangles. 
	 * This is useful for when drawing triangles is the only option.
	 * @param sides The indices for the given polygon
	 */
	private void triangulate(int[] sides) {
		boolean[] discard = new boolean[sides.length];
		int index = 0;
		for (int i = 0; i < sides.length-2; i++) {
			int[] tri = new int[3];
			
			for (int j = 0; j < 3; j++) {
				if (index < sides.length){

					if (!discard[index]) {
						tri[j] = sides[index];
					} else {
						j--;
					}
					if (j == 1) discard[index] = true;
					if (j != 2) index++;

				} else {
					index = 0;
					j--;
				}
				
			}
			
			indicesList.add(tri);
			
		}
		
	}
	
/**
 * Take some random List of float[] and turn it into a float[] containing the 
 * data. Useful for when a List<float[]> is not the proper format.
 * @param list The data to be transformed.
 * @param stride How many places the data is before it goes onto the next 
 * List float[]
 * @return A float[]
 */
	private static float[] toArray(List<float[]> list, int stride) {
		if (stride < 1) {
			return null;
		}
		float[] output = new float[list.size() * stride];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < stride; j++) {
				output[i * stride + j] = list.get(i)[j];
			}
		}
		return output;
	}

	/**
	 * Same as toArray but for int[]
	 * @param list How many places the data is before it goes onto the next 
 * List int[]
	 * @param stride
	 * @return An int[]
	 */
	private static int[] toArrayI(List<int[]> list, int stride) {
		if (stride < 1) {
			return null;
		}
		int[] output = new int[list.size() * stride];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < stride; j++) {
				output[i * stride + j] = list.get(i)[j];
			}
		}
		return output;
	}

	/**
	 * Remove the empty values for a String[]
	 * @param line Some random String[]
	 * @return A new String[] without empty spaces
	 */
	private String[] removeEmpty(String[] line) {
		List<String> clean = new ArrayList<String>(line.length);
		for (int i = 0; i < line.length; i++) {
			if (!line[i].isEmpty()) {
				clean.add(line[i]);
			}
		}

		String[] output = clean.toArray(new String[0]);

		return output;
	}

	/**
	 * Do any operations needed such as transforming data for triangulating or 
	 * generating normals.
	 */
	protected void initAll() {
		
		verticesList.trimToSize();
		textureCoordsList.trimToSize();
		indicesList.trimToSize();
		normalsList.trimToSize();
		surfNormList.trimToSize();
		paramVerticesList.trimToSize();
		colorAmbientList.trimToSize();
		colorDiffuseList.trimToSize();
		colorSpecularList.trimToSize();
		
		if (verticesList.size() != 0) {
			vertices = toArray(verticesList, vertexStride);
		}
		if (textureCoordsList.size() != 0) {
			textureCoords = toArray(textureCoordsList, textureStride);
		}
		if (indicesList.size() != 0) {
			indices = toArrayI(indicesList, indicesStride);
		}
		if (normalsList.size() != 0) {
			normals = toArray(normalsList, normalStride);
		} else {
			genNormals();
			normals = toArray(normalsList, normalStride);;
		}
		if (paramVerticesList.size() != 0) {
			paramVertices = toArray(paramVerticesList,
					paramVerticesList.get(0).length);
		}
		if (colorAmbientList.size() != 0) {
			colorAmbient = toArray(colorAmbientList,
					colorAmbientList.get(0).length);
		}
		if (colorDiffuseList.size() != 0) {
			colorDiffuse = toArray(colorDiffuseList,
					colorDiffuseList.get(0).length);
		}
		if (colorSpecularList.size() != 0) {
			colorSpecular = toArray(colorSpecularList,
					colorSpecularList.get(0).length);
		}

	}
}
