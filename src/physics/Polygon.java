package physics;

import main.Vec2;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nyrmburk on 3/7/2016.
 */
public class Polygon {

	private Vec2[] vertices;

	public Polygon(Vec2[] vertices) {

		setVertices(vertices);
	}

	public Vec2[] getVertices() {
		return vertices;
	}

	public void setVertices(Vec2[] vertices) {
		this.vertices = vertices;
	}

	public Polygon convexHull() {

		List<Vec2> convexPoints = new LinkedList<>();

		Vec2[] vertices = getVertices();

		Vec2 previous = vertices[vertices.length-2];
		Vec2 current = vertices[vertices.length-1];

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


		return new Polygon(convexPoints.toArray(new Vec2[0]));
	}

	public List<Polygon> decompose () {

		Vec2[] notches =  getNotches(this);
		VisibilityPair[] pairs = getVisibilityPairs(this);
		getBaseTriangles(pairs);
		return null;
	}

	private static Vec2[] getNotches(Polygon polygon) {

		List<Vec2> notches = new LinkedList<>();

		Vec2[] vertices = polygon.getVertices();

		Vec2 previous = vertices[vertices.length-2];
		Vec2 current = vertices[vertices.length-1];

		for (int i = 0; i < vertices.length; i++) {
			Vec2 next = vertices[i];

			if (isCCW(previous, current, next))
				notches.add(current);

			previous = current;
			current = next;
		}

		return notches.toArray(new Vec2[0]);
	}

	private static VisibilityPair[] getVisibilityPairs(Polygon polygon) {

		List<VisibilityPair> pairs = new LinkedList<>();

		Vec2[] vertices = polygon.getVertices();

		for (int i = 0; i < vertices.length - 1; i++) {

			for (int j = i + 2; j < vertices.length; j++) {

				if (isVisible(vertices[i], vertices[j], polygon))
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

	private static boolean isVisible(Vec2 lineStart, Vec2 lineEnd, Polygon polygon) {

		Vec2[] vertices = polygon.getVertices();

		Vec2 previous = vertices[vertices.length-1];

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

		return (b.getX() - a.getX())*(c.getY() - a.getY()) >
				(b.getY() - a.getY())*(c.getX() - a.getX());
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
}
