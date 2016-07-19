package physics;

import matrix.Vec2;

import java.util.*;

/**
 * Created by Nyrmburk on 3/7/2016.
 */
public class Polygon {

	public static Vec2[] convexHull(Vec2[] vertices) {

		List<Vec2> convexPoints = new LinkedList<>();

		Vec2 previous = vertices[vertices.length - 2];
		Vec2 current = vertices[vertices.length - 1];

		for (int i = 0; i < vertices.length; i++) {
			Vec2 next = vertices[i];

			if (!isCCW(previous, current, next)) {

				convexPoints.add(current);
			} else {

				current = next;
				continue;
			}

			previous = current;
			current = next;
		}

		return convexPoints.toArray(new Vec2[0]);
	}

	public static List<Vec2[]> approximateDecomposition(Vec2[] vertices) {

		if (!isWindingCCW(vertices))
			reverseWinding(vertices);

		return mp3Decomposition(new ArrayList<>(), vertices, new JumpTable(vertices.length), vertices.length, 0);
	}

	private static List<Vec2[]> mp3Decomposition(List<Vec2[]> polygons, Vec2[] vertices, JumpTable jumpTable, int size, int startIndex) {

		// if the number of vertices is less than required to make a triangle, it cannot be a polygon
		if (size < 3)
			return polygons;

		// This is a list of the current decomposition
		// Because it is an iterative process, it is used to store temporary info
		List<Vec2> decomposedVertices = new ArrayList<>();

		// nextIndex is used to reset after checking for vertices inside the polygon
		int nextIndex = startIndex;

		jumpTable.setIndex(startIndex);
		Vec2 v0 = vertices[jumpTable.getIndex()];
		Vec2 v1 = vertices[jumpTable.next()];

		Vec2 previous = v0;
		Vec2 current = v1;
		Vec2 next = null;

		decomposedVertices.add(v0);
		decomposedVertices.add(v1);

		boolean converged = false;

		for (int i = 2; i < size && !converged; i++) {

			nextIndex = jumpTable.getIndex();
			next = vertices[jumpTable.next()];

			// do not add next vertex to the decomposed polygon if it is a notch
			if (isNotch(v0, v1, previous, current, next))
				break;

			decomposedVertices.add(next);

			// check for vertices inside the decomposed polygon so far
			while (startIndex != jumpTable.peek() && decomposedVertices.size() != 2) {

				Vec2 insideDecomposition = vertices[jumpTable.next()];

				if (!insidePolygon(decomposedVertices, insideDecomposition))
					continue;

				// vertex is inside current decomposition
				// remove last vertex
				decomposedVertices.remove(decomposedVertices.size() - 1);
				Vec2 toRemove = decomposedVertices.get(decomposedVertices.size() - 1);

				// keep removing vertices until convexity is reached
				while (isCCW(v0, insideDecomposition, toRemove))
					toRemove = decomposedVertices.remove(decomposedVertices.size() - 1);

				converged = true;
			}

			jumpTable.setIndex(nextIndex);
			jumpTable.next();

			previous = current;
			current = next;
		}

		// push new polygon and recursively start next segment
		int polySize = decomposedVertices.size();
		if (polySize > 2) {

			Vec2[] decomposedVerticesArray = new Vec2[polySize];
			decomposedVertices.toArray(decomposedVerticesArray);
			polygons.add(decomposedVerticesArray);
		}

		jumpTable.setIndex(startIndex);
		jumpTable.setJumpTo(nextIndex);
		nextIndex = jumpTable.next();
		size -= polySize - 2;
		mp3Decomposition(polygons, vertices, jumpTable, size, nextIndex);

		return polygons;
	}

	private static boolean isNotch(Vec2 v0, Vec2 v1, Vec2 previous, Vec2 current, Vec2 next) {

		return !isCCW(previous, current, next) || !isCCW(current, next, v0) || !isCCW(next, v0, v1);

	}

	private static boolean insidePolygon(List<Vec2> poly, Vec2 point) {

		Vec2 previous = poly.get(poly.size() - 1);
		for (Vec2 polyVec : poly) {

			if ((point.y - previous.y) * (polyVec.x - previous.x) - (point.x - previous.x) * (polyVec.y - previous.y) < 0)
				return false;

			previous = polyVec;
		}

		return true;
	}

	public static List<Vec2[]> decompose(Vec2[] vertices) {

		Vec2[] notches = getNotches(vertices);
		VisibilityPair[] pairs = getVisibilityPairs(vertices);
		getBaseTriangles(pairs);
		return null;
	}

	private static Vec2[] getNotches(Vec2[] vertices) {

		List<Vec2> notches = new LinkedList<>();

		Vec2 previous = vertices[vertices.length - 2];
		Vec2 current = vertices[vertices.length - 1];

		for (int i = 0; i < vertices.length; i++) {
			Vec2 next = vertices[i];

			if (isCCW(previous, current, next))
				notches.add(current);

			previous = current;
			current = next;
		}

		return notches.toArray(new Vec2[0]);
	}

	private static VisibilityPair[] getVisibilityPairs(Vec2[] vertices) {

		List<VisibilityPair> pairs = new LinkedList<>();

		for (int i = 0; i < vertices.length - 1; i++) {

			for (int j = i + 2; j < vertices.length; j++) {

				if (isVisible(vertices[i], vertices[j], vertices))
					pairs.add(new VisibilityPair(vertices[i], vertices[j]));
			}
		}

		return (VisibilityPair[]) pairs.toArray();
	}

	private static void getBaseTriangles(VisibilityPair[] pairs) {

//		Arrays.sort(pairs);
//		BaseTriangle[] baseTriangle = new BaseTriangle[];
//
//		for (int i = 0; i < pairs.length; i++) {
//
//			int j = 0;
//			int k = 0;
//			for (int l = i + 1; l < i; l++) {
//
//				if (true && true) {
//
//
//				}
//			}
//		}
	}

	private static boolean isVisible(Vec2 lineStart, Vec2 lineEnd, Vec2[] vertices) {

		Vec2 previous = vertices[vertices.length - 1];

		for (int i = 0; i < vertices.length; i++) {
			Vec2 current = vertices[i];

			if (intersection(lineStart, lineEnd, previous, current))
				return false;

			previous = current;
		}

		return true;
	}

	private static boolean intersection(Vec2 a, Vec2 b, Vec2 c, Vec2 d) {

		return isCCW(a, c, d) != isCCW(b, c, d) &&
				isCCW(a, b, c) != isCCW(a, b, d);
	}

	private static boolean isCCW(Vec2 a, Vec2 b, Vec2 c) {

		return (b.x - a.x) * (c.y - a.y) >
				(b.y - a.y) * (c.x - a.x);
	}

	public static boolean isWindingCCW(Vec2[] vertices) {

		float sum = 0;

		Vec2 previous = vertices[vertices.length - 1];
		for (Vec2 vertex : vertices) {

			sum += (vertex.x - previous.x) * (vertex.y + previous.y);
			previous = vertex;
		}

		return sum < 0;
	}

	public static void reverseWinding(Vec2[] vertices) {

		int halfLength = vertices.length / 2;
		for(int i = 0; i < halfLength; i++) {
			Vec2 temp = vertices[i];
			vertices[i] = vertices[vertices.length - i - 1];
			vertices[vertices.length - i - 1] = temp;
		}
	}

	private static class JumpTable {

		int index = 0;
		int nextJump = 0;
		int[] table;

		public JumpTable(int size) {

			table = new int[size];
			Arrays.fill(table, 1);
		}

		public JumpTable(int[] table) {

			this.table = table;
		}

		public int next() {

			index += nextJump;
			index %= table.length;
			if (index < 0)
				index += table.length;
			nextJump = table[index];
			return index;
		}

		public int peek() {

			return (index + nextJump) % table.length;
		}

		public int getIndex() {

			return index;
		}

		public void setJump(int jump) {

			nextJump = jump;
			table[index] = jump;
		}

		public void setJumpTo(int destination) {

			setJump(destination - getIndex());
		}

		public void addJump(int jump) {

			setJump(getJump() + jump);
		}

		public int getJump() {

			return nextJump;
		}

		public void setIndex(int index) {

			this.index = index;
			nextJump = table[index];
		}
	}

	private static class VisibilityPair implements Comparable<VisibilityPair> {

		public Vec2 a;
		public Vec2 b;

		public VisibilityPair(Vec2 a, Vec2 b) {

			this.a = a;
			this.b = b;
		}

		@Override
		public int compareTo(VisibilityPair o) {
			return 0;
		}
	}

	private static class Bounds {

		float top = Float.MIN_NORMAL;
		float left = Float.MAX_VALUE;
		float right = Float.MIN_NORMAL;
		float bottom = Float.MAX_VALUE;

		public void grow(Vec2 vec) {

			if (vec.x > right)
				right = vec.x;
			if (vec.x < left)
				left = vec.x;
			if (vec.y > top)
				top = vec.y;
			if (vec.y < bottom)
				bottom = vec.y;
		}

		public boolean inside(Vec2 vec) {

			return (vec.x < right && vec.x > left && vec.y < top && vec.y > bottom);
		}
	}
}
