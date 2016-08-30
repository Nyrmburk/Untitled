package graphics;

import main.Resource;
import matrix.Vec2;
import matrix.Vec3;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;

/**
 * Created by Nyrmburk on 5/13/2016.
 */
public class Model extends Resource {

	private static final int VERTEX_STRIDE = 3;
	private static final int NORMAL_STRIDE = 3;
	private static final int TEXTURE_STRIDE = 2;

	private int elementCapacity;
	private FloatBuffer vertices;
	private FloatBuffer normals;
	private FloatBuffer texCoords;

	public Texture texture;

	private String name;

	public Model(int capacity) {
		this.setElementCapacity(capacity);
	}

	public Model() {
		this(10);
	}

	public void rewindBuffers() {

		vertices.rewind();
		normals.rewind();
		texCoords.rewind();
	}

	public static Model concatenate(Model... models) {

		int newSize = 0;
		for (Model model : models) newSize += model.elementCapacity;

		Model newModel = new Model(newSize);

		for (Model model : models) {

			model.rewindBuffers();

			// copy over other data
			for (int i = 0; i < model.elementCapacity * VERTEX_STRIDE; i++)
				newModel.vertices.put(model.vertices.get());
			for (int i = 0; i < model.elementCapacity * NORMAL_STRIDE; i++)
				newModel.normals.put(model.normals.get());
			for (int i = 0; i < model.elementCapacity * TEXTURE_STRIDE; i++)
				newModel.texCoords.put(model.texCoords.get());
		}

		return newModel;
	}

	public int getElementCapacity() {
		return elementCapacity;
	}

	private void setElementCapacity(int elementCapacity) {
		this.elementCapacity = elementCapacity;

		vertices = resize(vertices, elementCapacity * VERTEX_STRIDE);
		normals = resize(normals, elementCapacity * NORMAL_STRIDE);
		texCoords = resize(texCoords, elementCapacity * TEXTURE_STRIDE);

		onModify();
	}

	private FloatBuffer resize(FloatBuffer source, int size) {

		FloatBuffer resized = ByteBuffer.allocateDirect(size * Float.BYTES).asFloatBuffer();

		if (source == null)
			return resized;

		source.rewind();
		source.limit(Math.min(source.limit(), size));
		resized.put(source);

		return resized;
	}

	public void generateNormals() {

		for (int i = 0; i < elementCapacity; i += 3) {

			Vec3 a = getVertex(i    );
			Vec3 b = getVertex(i + 1);
			Vec3 c = getVertex(i + 2);

			Vec3 p = b.subtract(a);
			Vec3 q = c.subtract(a);

			Vec3 normal = p.cross(q).normalized();

			float angleA = p.angle(q);

			p = c.subtract(b);
			q = a.subtract(b);

			float angleB = p.angle(q);

			p = a.subtract(c);
			q = b.subtract(c);

			float angleC = p.angle(q);

			setNormal(i    , getNormal(i    ).add(normal.multiply(angleA)));
			setNormal(i + 1, getNormal(i + 1).add(normal.multiply(angleB)));
			setNormal(i + 2, getNormal(i + 2).add(normal.multiply(angleC)));
		}

		for (int i = 0; i < elementCapacity; i++)
			setNormal(i, getNormal(i).normalized());
	}

	public Vec3 getVertex(int index) {

		index *= VERTEX_STRIDE;
		return new Vec3(
				vertices.get(index),
				vertices.get(index+1),
				vertices.get(index+2));
	}

	public Vec3 getNormal(int index) {

		index *= NORMAL_STRIDE;
		return new Vec3(
				normals.get(index),
				normals.get(index+1),
				normals.get(index+2));
	}

	public Vec2 getTexCoord(int index) {

		index *= TEXTURE_STRIDE;
		return new Vec2(
				texCoords.get(index),
				texCoords.get(index+1));
	}

	public void setVertex(int index, Vec3 vec) {

		if (index > elementCapacity)
			resize(vertices, Math.max(elementCapacity * 2, index));
		set3(vertices, index, vec);
	}

	public void setNormal(int index, Vec3 vec) {

		if (index > elementCapacity)
			resize(normals, Math.max(elementCapacity * 2, index));
		set3(normals, index, vec);
	}

	public void setTexCoord(int index, Vec2 vector) {

		if (index > elementCapacity)
			resize(texCoords, Math.max(elementCapacity * 2, index));
		texCoords.put(index, vector.x);
		texCoords.put(index+1, vector.y);
	}

	private static void set3(FloatBuffer buffer, int index, Vec3 vec) {

		index *= 3;
		buffer.put(index, vec.x);
		buffer.put(index+1, vec.y);
		buffer.put(index+2, vec.z);
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

		elementCapacity = temp.elementCapacity;
		vertices = temp.vertices;
		normals = temp.normals;
		texCoords = temp.texCoords;
	}
}
