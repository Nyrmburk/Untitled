package graphics;

import main.Resource;
import matrix.Vec2;
import matrix.Vec3;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

/**
 * Created by Nyrmburk on 5/13/2016.
 */
public class Model extends Resource {

	private static final int VERTEX_STRIDE = 3;
	private static final int NORMAL_STRIDE = 3;
	private static final int TEXTURE_STRIDE = 2;

	public int elementCount;
	private int elementCapacity;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	public FloatBuffer texCoords;
	public IntBuffer indices;

	public Texture texture;

	private String name;

	public Model(int capacity, int count) {

		this.elementCount = count;
		this.setElementCapacity(capacity);

		vertices = ByteBuffer.allocateDirect(capacity * VERTEX_STRIDE * Float.BYTES).asFloatBuffer();
		normals = ByteBuffer.allocateDirect(capacity * NORMAL_STRIDE * Float.BYTES).asFloatBuffer();
		texCoords = ByteBuffer.allocateDirect(capacity * TEXTURE_STRIDE * Float.BYTES).asFloatBuffer();
		indices = ByteBuffer.allocateDirect(capacity * Integer.BYTES).asIntBuffer();
	}

	public Model(int size) {

		this(size, size);
	}

	public void rewindBuffers() {

		vertices.rewind();
		normals.rewind();
		texCoords.rewind();
		indices.rewind();
	}

	// not usable until a modification listener is added

	// gets the max index and shortens the other buffers to that length
	// also shortens indices to count instead of capacity
	public void trim() {

		for (int i = 0; i < elementCount; i++) {

		}
	}

	public static Model concatenate(Model... models) {

		int newSize = 0;
		for (Model model : models) newSize += model.elementCount;

		Model newModel = new Model(newSize);

		int maxIndex = 0;
		int indexOffset = 0;
		for (Model model : models) {

			model.rewindBuffers();
			for (int i = 0; i < model.elementCount; i++) {

				// put new index
				int index = model.indices.get();
				maxIndex = Math.max(maxIndex, index);
				newModel.indices.put(index + indexOffset);
			}

			// copy over other data
			for (int i = 0; i < model.elementCount * VERTEX_STRIDE; i++)
				newModel.vertices.put(model.vertices.get());
			for (int i = 0; i < model.elementCount * NORMAL_STRIDE; i++)
				newModel.normals.put(model.normals.get());
			for (int i = 0; i < model.elementCount * TEXTURE_STRIDE; i++)
				newModel.texCoords.put(model.texCoords.get());

			indexOffset += maxIndex;
		}

		return newModel;
	}

	public int getElementCapacity() {
		return elementCapacity;
	}

	// not usable until a modification listener is added
	private void setElementCapacity(int elementCapacity) {
		this.elementCapacity = elementCapacity;

		Model replacement = new Model(elementCapacity);
		int copySize = Math.min(this.elementCapacity, elementCapacity);

		rewindBuffers();
		for (int i = 0; i < copySize; i++)
			replacement.indices.put(indices.get());

		for (int i = 0; i < copySize * VERTEX_STRIDE; i++) {

			replacement.vertices.put(vertices.get());
			replacement.normals.put(normals.get());
			replacement.texCoords.put(texCoords.get());
		}
	}

	public void generateNormals() {

		Vec3[] SurfaceNormals = new Vec3[elementCount];

		for (int i = 0; i < elementCount; i++) {

			//triangle
			int[] tri = getTriangleIndices(i);

			Vec3 a = getVertex(tri[0]);
			Vec3 b = getVertex(tri[1]);
			Vec3 c = getVertex(tri[2]);

			b = b.subtract(a);
			c = c.subtract(a);

			Vec3 normal = b.cross(c).normalized();
			SurfaceNormals[i] = normal;

			float angleA = b.angle(c);

			b = getVertex(tri[1]);
			c = getVertex(tri[2]);

			c = c.subtract(b);
			a = a.subtract(b);

			float angleB = c.angle(a);

			c = getVertex(tri[2]);
			a = getVertex(tri[0]);

			a = a.subtract(c);
			b = b.subtract(c);

			float angleC = a.angle(b);// (float) (Math.PI - (angleA +
			// angleB));

			setNormal(tri[0], getNormal(tri[0]).add(normal.multiply(angleA)));
			setNormal(tri[1], getNormal(tri[1]).add(normal.multiply(angleB)));
			setNormal(tri[2], getNormal(tri[2]).add(normal.multiply(angleC)));
		}

		for (int i = 0; i < elementCount; i++) {

			setNormal(i, getNormal(i).normalized());
		}
	}


	public Vec3 getVertex(int index) {

		return new Vec3(
				vertices.get(index),
				vertices.get(index+1),
				vertices.get(index+2));
	}

	public Vec3 getNormal(int index) {

		return new Vec3(
				normals.get(index),
				normals.get(index+1),
				normals.get(index+2));
	}

	public Vec2 getTexCoord(int index) {

		return new Vec2(
				texCoords.get(index),
				texCoords.get(index+1));
	}

	public void setVertex(int index, Vec3 vector) {

		vertices.put(index);
		vertices.put(index+1);
		vertices.put(index+2);
	}

	public void setNormal(int index, Vec3 vector) {

		normals.put(index);
		normals.put(index+1);
		normals.put(index+2);
	}

	public void setTexCoord(int index, Vec2 vector) {

		texCoords.put(index);
		texCoords.put(index+1);
	}

	public int[] getTriangleIndices(int index) {

		index *= 3; //vertices in a triangle
		return new int[]{
				indices.get(index),
				indices.get(index+1),
				indices.get(index+2)};
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public void save(Path path) throws IOException {

		throw new NotImplementedException();
	}

	@Override
	public void load(Path path) throws IOException {

		OBJModelLoader loader = new OBJModelLoader();
		Model temp = loader.load(path);

		elementCount = temp.elementCount;
		elementCapacity = temp.elementCapacity;
		vertices = temp.vertices;
		normals = temp.normals;
		texCoords = temp.texCoords;
		indices = temp.indices;
	}
}
