package main;

import matrix.Vec2;

import java.awt.*;

/**
 * Created by Nyrmburk on 5/3/2016.
 */
public class Line {

	private int length;
	private boolean loop;

	private Vec2[] vertices;
	private float[] width;
	private int[] color;

	public Line(int length, boolean loop) {

		this.length = length;

		vertices = new Vec2[length];
		width = new float[length];
		color = new int[length];
		setLoop(loop);
	}

	public void setData (int index, Vec2 vertex, float width, int color) {

		this.vertices[index] = vertex;
		this.width[index] = width;
		this.color[index] = color;
	}

	public void setData (int index, Vec2 vertex, float width, Color color) {

		setData(index, vertex, width, color.getRGB());
	}

	public int getLength() {
		return length;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public Vec2[] getVertices() {
		return vertices;
	}

	public float[] getWidth() {
		return width;
	}

	public int[] getColor() {
		return color;
	}
}
