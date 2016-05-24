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
	private Color[] color;

	public Line(int length, boolean loop) {

		this.length = length;

		vertices = new Vec2[length];
		width = new float[length];
		color = new Color[length];
		setLoop(loop);
	}

	public void setData (int index, Vec2 vertex, float width, Color color) {

		this.vertices[index] = vertex;
		this.width[index] = width;
		this.color[index] = color;
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

	public Color[] getColor() {
		return color;
	}
}
