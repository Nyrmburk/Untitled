package graphics;

import graphics.ModelLoader.FloatList;
import graphics.ModelLoader.IntList;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.Resource;
import main.Vector;

import org.lwjgl.BufferUtils;

public class ModelLoader extends Resource {

	public FloatList vertices = new FloatList();
	public FloatList normals = new FloatList();
	public FloatList surfNorm = new FloatList();
	public FloatList textureCoords = new FloatList();
	public ArrayList<Integer> color = new ArrayList<>();
	public IntList indices = new IntList();
	public IntList shading = new IntList();
	public IntList illumination = new IntList();
	public FloatList materialDissolve;
	public FloatList material;
	public FloatList object;
	public FloatList group;

	public Texture texture;

	String name;

	public ModelLoader() {
	}

	public ModelLoader(File file) {
		load(file);
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public void save(Path path) {

	}

	@Override
	public void load(Path path) throws IOException {

		load(path.toFile());
		name = path.getFileName().toString();
	}

	public void load(File file) {

		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(file);
			read = new BufferedReader(reader);

			// long time = System.currentTimeMillis();

			LoadOBJ objLoader = new LoadOBJ(this);

			while ((currentLine = read.readLine()) != null) {
				if (currentLine.isEmpty())
					continue;
				objLoader.parseLine(currentLine);
			}

			generateNormals();

			// System.out.println(System.currentTimeMillis() - time +
			// " milliseconds to load " + vertices.size() +
			// " vertices and " + indices.size() +
			// " faces");

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

	public void generateNormals() {

		normals.setSize(indices.getSize(), 3);

		for (int i = 0; i < indices.getSize(); i++) {

			int[] tri = indices.get(i);

			float[] a = vertices.get(tri[0]);
			float[] b = vertices.get(tri[1]);
			float[] c = vertices.get(tri[2]);

			b = Vector.cSubVector(b, a);
			c = Vector.cSubVector(c, a);

			float[] normal = Vector.normalize(Vector.cCrossVector(b, c));
			surfNorm.put(i, normal);

			float angleA = Vector.cAngle(b, c);

			b = vertices.get(tri[1]);
			c = vertices.get(tri[2]);

			c = Vector.cSubVector(c, b);
			a = Vector.cSubVector(a, b);

			float angleB = Vector.cAngle(c, a);

			c = vertices.get(tri[2]);
			a = vertices.get(tri[0]);

			a = Vector.cSubVector(a, c);
			b = Vector.cSubVector(b, c);

			float angleC = Vector.cAngle(a, b);// (float) (Math.PI - (angleA +
												// angleB));

			normals.put(
					tri[0],
					Vector.cAddVector(normals.get(tri[0]),
							Vector.cScaleVector(normal, angleA)));
			normals.put(
					tri[1],
					Vector.cAddVector(normals.get(tri[1]),
							Vector.cScaleVector(normal, angleB)));
			normals.put(
					tri[2],
					Vector.cAddVector(normals.get(tri[2]),
							Vector.cScaleVector(normal, angleC)));

			// normals.put(tri[0], Vector.cScaleVector(normal, 1));
			// normals.put(tri[1], Vector.cScaleVector(normal, 1));
			// normals.put(tri[2], Vector.cScaleVector(normal, 1));
		}

		for (int i = 0; i < normals.getSize(); i++) {

			normals.put(i, Vector.normalize(normals.get(i)));
		}
	}

	public void addFace(int... indices) {
		triangulate(indices);
	}

	/**
	 * Input an n-sided convex polygon and turn it into a bunch of triangles.
	 * This is useful for when drawing triangles is the only option.
	 * 
	 * @param sides
	 *            The indices for the given polygon
	 */
	private void triangulate(int[] sides) {
		boolean[] discard = new boolean[sides.length];
		int index = 0;
		for (int i = 0; i < sides.length - 2; i++) {
			int[] tri = new int[3];

			for (int j = 0; j < 3; j++) {
				if (index < sides.length) {

					if (!discard[index]) {
						tri[j] = sides[index];
					} else {
						j--;
					}
					if (j == 1)
						discard[index] = true;
					if (j != 2)
						index++;

				} else {
					index = 0;
					j--;
				}
			}
			indices.put(tri);
		}
	}

	public class FloatList {

		private int stride;

		private float[] data;

		private int size = 0;

		public FloatList() {
		}

		public FloatList(int stride) {

			this(stride, 10);
		}

		public FloatList(int stride, int size) {

			if (stride == 0)
				throw new IllegalArgumentException("stride must be 1 or greater");

			this.stride = stride;
			initArray(size);
		}

		private void initArray(int size) {

			data = new float[size * stride];
		}

		public void put(float... values) {

			put(size++, values);
		}

		public void put(int index, float... values) {

			if (data == null) {
				this.stride = values.length;
				initArray(10);
			} else if (values.length != stride)
				throw new IllegalArgumentException("input does not match stride");

			int totalLength = data.length / stride;
			if (index >= totalLength)
				setSize(index + totalLength / 2, stride);

			if (index > size)
				size = index;

			System.arraycopy(values, 0, data, index * stride, stride);
		}

		public float[] get(int index) {

			float[] array = new float[stride];

			System.arraycopy(data, index * stride, array, 0, stride);

			return array;
		}

		public FloatBuffer toFloatBuffer() {

			trim();
			FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
			buffer.put(data);
			buffer.rewind();

			return buffer;
		}

		public float[] toArray() {

			trim();
			return data;
		}

		public int getSize() {

			if (data == null)
				return 0;
			return data.length / stride;
		}

		public void setSize(int size, int stride) {

			this.stride = stride;

			float[] tempArray = new float[size * stride];

			if (data != null)
				System.arraycopy(data, 0, tempArray, 0,
						Math.min(data.length, size * stride));
			data = tempArray;
			tempArray = null;
		}

		public int getStride() {

			return stride;
		}

		public boolean isEmpty() {

			return (data == null || data.length == 0);
		}

		public void trim() {

			setSize(size, stride);
		}

		public void addAll(Collection<float[]> collection) {

			for (float[] floats : collection)
				put(floats);
		}
	}

	public class IntList {

		private int stride;

		private int[] data;

		private int size = 0;

		public IntList() {
		}

		public IntList(int stride) {

			this(stride, 10);
		}

		public IntList(int stride, int size) {

			if (stride == 0)
				throw new IllegalArgumentException("stride must be 1 or greater");

			this.stride = stride;
			initArray(size);
		}

		private void initArray(int size) {

			data = new int[size * stride];
		}

		public void put(int... values) {

			put(size++, values);
		}

		public void put(int index, int... values) {

			if (data == null) {
				this.stride = values.length;
				initArray(10);
			} else if (values.length != stride)
				throw new IllegalArgumentException("input does not match stride");

			int totalLength = data.length / stride;
			if (index >= totalLength)
				setSize(index + totalLength / 2, stride);

			if (index > size)
				size = index;

			System.arraycopy(values, 0, data, index * stride, stride);
		}

		public int[] get(int index) {

			int[] array = new int[stride];

			System.arraycopy(data, index * stride, array, 0, stride);

			return array;
		}

		public IntBuffer toIntBuffer() {

			trim();
			IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
			buffer.put(data);
			buffer.flip();

			return buffer;
		}

		public int[] toArray() {

			trim();
			return data;
		}

		public int getSize() {

//			return data.length / stride;
			return size;
		}

		public void setSize(int size, int stride) {

			this.stride = stride;

			int[] tempArray = new int[size * stride];

			if (data != null)
				System.arraycopy(data, 0, tempArray, 0,
						Math.min(data.length, size * stride));
			data = tempArray;
			tempArray = null;
		}

		public int getStride() {

			return stride;
		}

		public boolean isEmpty() {

			return (data == null || data.length == 0);
		}

		public void trim() {

			setSize(size, stride);
		}

		public void addAll(Collection<int[]> collection) {

			for (int[] ints : collection)
				put(ints);
		}
	}

	public static class LoadOBJ {

		ModelLoader model;

		public LoadOBJ(ModelLoader model) {

			this.model = model;
		}

		public void parseLine(String line) {
			// http://www.martinreddy.net/gfx/3d/OBJ.spec

			String[] arguments = line.split("\\s+");

			// find out what the type definition is
			switch (arguments[0]) {

				case "#":// comment
					break;
				// vertex data
				case "v":// vertex
					putFloats(model.vertices, arguments, 1);
					break;
				case "vt":// texture vertex
					putFloats(model.textureCoords, arguments, 1);
					break;
				case "vn":// vertex normal
					putFloats(model.normals, arguments, 1);
					break;
				// elements
				case "p":// point
					break;
				case "l":// line
					break;
				case "f":// face
					// TODO do face parsing
					int[] verts = new int[arguments.length - 1];
					int[] uvs = new int[arguments.length - 1];
					int[] norms = new int[arguments.length - 1];

					@SuppressWarnings("unused")
					boolean useUVs = false;
					@SuppressWarnings("unused")
					boolean useNorms = false;

					for (int i = 0; i < arguments.length - 1; i++) {

						String[] face = arguments[i + 1].split("/");
						if (!face[0].isEmpty())
							verts[i] = Integer.parseInt(face[0]) - 1;

						if (face.length < 2)
							continue;
						if (!face[1].isEmpty()) {

							uvs[i] = Integer.parseInt(face[1]) - 1;
							useUVs = true;
						}

						if (face.length < 3)
							continue;
						if (!face[2].isEmpty()) {

							norms[i] = Integer.parseInt(face[2]) - 1;
							useNorms = true;
						}
					}

					model.addFace(verts);
					// if (useUVs) model.textureCoords.put(uvs);
					// if (useNorms) model.normals.put(norms);
					break;
				// grouping
				case "g":// group name
					break;
				case "s":// smoothing group
					break;
				case "o":// object name
					break;
				// display/render attributes
				case "bevel":// bevel interpolation
					break;
				case "c_interp":// color interpolation
					break;
				case "d_interp":// dissolve interpolation
					break;
				case "lod":// level of detail
					break;
				case "usemtl":// material name
					break;
				case "mtllib":// material library
					break;
				case "shadow_obj":// shadow casting
					break;
			}
		}

		private void putFloats(FloatList dest, String[] data, int offset) {

			float[] floats = new float[data.length - offset];

			for (int i = 0; i < data.length - offset; i++) {
				floats[i] = Float.parseFloat(data[i + offset]);
			}

			dest.put(floats);
		}

		@SuppressWarnings("unused")
		private void putInts(IntList dest, String[] data, int offset) {

			int[] ints = new int[data.length - offset];

			for (int i = 1; i < data.length; i++) {
				ints[i] = Integer.parseInt(data[1]);
			}

			dest.put(ints);
		}
	}
}