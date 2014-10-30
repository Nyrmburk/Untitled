package entity;

import java.util.*;

import main.Engine;

import org.lwjgl.opengl.GL11;

import ai.*;
import graphics.*;

/**
 * Mob - short for mobile, contains pathfinding, moment speed, and what faction
 * the mob belongs to.
 * 
 * @author Christopher Dombroski
 *
 */
public class Mob extends Entity implements Selectable, Movable {

	int[] coord;
	int[] nextCoord;
	// int[] lastCoord;
	int[] destCoord;

	float speed = 3f;

	int selectionPriority = 3;
	boolean selected = false;

	Faction faction;

	ShaderProgram shaderProgram;

	private PriorityQueue<Action> actionQueue = new PriorityQueue<Action>();
	private Action currentAction;

	ArrayList<int[]> pathSteps;

	public Mob(String name, float[] location, Model model) {
		this(name, new Faction(), location, model);
	}

	public Mob(String name, Faction faction, float[] location, Model model) {
		super(name, types.MOB, location, model);
		super.addSelectable(this);
		this.coord = locationToCoord(this.location);
		this.faction = faction;
	}

	@Override
	public void draw() {

		if (selected) {
			GL11.glColor3f(0.5f, 0.5f, 0.5f);
		} else {
			GL11.glColor3f(0.8f, 0.8f, 0.8f);
		}

		GL11.glShadeModel(GL11.GL_SMOOTH);
		super.draw();
		GL11.glShadeModel(GL11.GL_FLAT);

		GL11.glColor3f(0.8f, 0.8f, 0.8f);

		if (pathSteps != null) {
			GL11.glLineWidth(2);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glBegin(GL11.GL_LINE_STRIP);

			GL11.glVertex3f(location[0], location[1],
					((MapMesh) Engine.worldEntity.mdl).getHeight(location));
			for (int[] step : pathSteps) {
				GL11.glVertex3f(step[0], step[1],
						Engine.world.getWorldHeight()[step[0]][step[1]]);
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glLineWidth(1);
		}
	}

	@Override
	public void update(int delta) {

		coord = locationToCoord(location);

		if (currentAction == null || currentAction.isComplete()) {

			if (currentAction != null && currentAction.chainedAction != null) {

				actionQueue.offer(currentAction.chainedAction);

			} else if (actionQueue.isEmpty()) {

				actionQueue.offer(new ActionWander(coord));
			}

			currentAction = actionQueue.poll();
			destCoord = currentAction.destination;
			pathSteps = null;
		}

		currentAction.update(coord);

		if (!Arrays.equals(destCoord, nextCoord)
				&& (pathSteps == null || pathSteps.isEmpty())
				&& destCoord != null) {

			navigateTo(destCoord);
		}

		if (pathSteps != null && !pathSteps.isEmpty()) {

			nextCoord = pathSteps.get(0).clone();

			// if (lastCoord != null) {
			// Engine.world.clearMovementSpeed(lastCoord);
			// }
			// Engine.world.setMovementSpeed(coord, 0.2f);
			//
			// lastCoord = coord;
		} else {

			nextCoord = coord.clone();
		}

		moveTo(coord, nextCoord, delta);

		if (Math.round(location[0]) == nextCoord[0]
				&& Math.round(location[1]) == nextCoord[1] && pathSteps != null
				&& !pathSteps.isEmpty()) {

			Engine.world.incrementDesirePath(coord);
			pathSteps.remove(0);
		}
	}

	private void moveTo(int[] coord, int[] nextCoord, int delta) {

		float dx = (nextCoord[0] - location[0]);
		float dy = (nextCoord[1] - location[1]);

		float distance = (float) ((speed * delta / 1000) / Math.sqrt(dx * dx
				+ dy * dy));
		if (Float.isInfinite(distance))
			distance = 0;

		distance *= Engine.world.getMovementSpeed(coord);

		if (Math.abs(dx) <= Math.abs(distance * dx)) {
			location[0] = nextCoord[0];
		} else {
			location[0] += distance * dx;
		}

		if (Math.abs(dy) <= Math.abs(distance * dy)) {
			location[1] = nextCoord[1];
		} else {
			location[1] += distance * dy;
		}

		location[2] = ((MapMesh) Engine.worldEntity.mdl).getHeight(location) + 0.8f;

		lookAt(nextCoord);
	}

	private void lookAt(int[] coord) {

		float[] location = { coord[0], coord[1], 0 };
		location[2] = ((MapMesh) Engine.worldEntity.mdl).getHeight(location) + 0.8f;
		lookAt(location);
	}

	private void lookAt(float[] location) {

		if (Arrays.equals(location, this.location))
			return;
		location[0] -= this.location[0];
		location[1] -= this.location[1];
		location[2] -= this.location[2];

		rotation[2] = -(float) Math.toDegrees(Math.atan2(location[0],
				location[1]));

		// todo later
		// float longSide = (float) Math.sqrt(location[0] * location[0] +
		// location[1] * location[1]);
		// float pitch = (float) Math.toDegrees(Math.atan2(location[2],
		// longSide));
		// rotation[1] = pitch * (float) Math.tan(Math.toRadians(rotation[2]));
		// rotation[0] = pitch * (float) Math.tan(Math.toRadians(90 -
		// rotation[2]));
	}

	private void navigateTo(int[] destCoord) {

		pathSteps = ai.AStarPathFinding
				.findPath(Engine.world, coord, destCoord);
	}

	private int[] locationToCoord(float[] location) {
		int[] coord = new int[2];

		coord[0] = (int) Math.rint(location[0]);
		coord[1] = (int) Math.rint(location[1]);

		return coord;
	}

	public void addAction(Action action) {

		actionQueue.offer(action);

		if (currentAction.compareTo(action) >= 0) {

			if (!currentAction.isTemporary()) {

				actionQueue.offer(currentAction);
			}
			currentAction = null;
		}
	}

	@Override
	public boolean isAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getMoveSpeedPercent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pickUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelected() {

		return selected;
	}

	@Override
	public void selected(boolean selected) {

		this.selected = selected;
	}
}
