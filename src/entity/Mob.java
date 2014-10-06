package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import main.Engine;

import org.lwjgl.opengl.GL11;

import ai.*;
import graphics.MapMesh;
import graphics.Model;

public class Mob extends Entity {
	
	int[] coord;
	int[] nextCoord;
	int[] destCoord;
	
	float speed = 3f;
	
	Faction faction;
	
	PriorityQueue<Action> actionQueue = new PriorityQueue<Action>();
	Action currentAction;
	
	ArrayList<int[]> pathSteps;
	
	public Mob(String name, float[] location, Model model) {
		this(name, new Faction(), location, model);
	}

	public Mob(String name, Faction faction, float[] location, Model model) {
		super(name, types.MOB, location, model);
		this.coord = locationToCoord(this.location);
		this.faction = faction;
	}
	
	@Override
	public void draw() {
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		super.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		
		if (pathSteps != null) {
			GL11.glLineWidth(2);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			
			for (int[] step : pathSteps) {
				GL11.glVertex3f(step[0], step[1], Engine.world.getWorldHeight()[step[0]][step[1]]);
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glLineWidth(1);
		}
	}
	
	@Override
	public void update(int delta) {
		
//		delta = 100;
		
		coord = locationToCoord(location);
		
		if (currentAction == null || currentAction.isComplete()) {
			
			if (currentAction != null && currentAction.chainedAction != null) {
				
				actionQueue.add(currentAction.chainedAction);
				
			} else if (actionQueue.isEmpty()) {
				
				actionQueue.add(new ActionWander(coord));
			}
			
			currentAction = actionQueue.poll();
		}
		
		currentAction.update(coord);
		
//		if (!currentAction.destinations.isEmpty() && (pathSteps == null || pathSteps.isEmpty())) {
//			
//			destCoord = currentAction.destinations.poll();
//		}
		
		if (currentAction.destination != null && (pathSteps == null || pathSteps.isEmpty())) {

			destCoord = currentAction.destination;
		}
		
//		int[] destCoord = new int[location.length];
		
		if (pathSteps != null && !pathSteps.isEmpty()) {
			
			nextCoord = pathSteps.get(0).clone();
		} else {
			
			nextCoord = coord.clone();
		}
		
//		System.out.println("current: " + Arrays.toString(coord) + 
//				", next: " + Arrays.toString(nextCoord) + 
//				", destination: " + Arrays.toString(destCoord));
		
		if (!Arrays.equals(destCoord, nextCoord) && (pathSteps == null || pathSteps.isEmpty())) {
			
			navigateTo(destCoord);
		}
		
		moveTo(nextCoord, delta);
		
		if (Arrays.equals(coord,  nextCoord) && pathSteps != null
				&& !pathSteps.isEmpty()) {

			pathSteps.remove(0);
		}
	}
	
	private void moveTo(int[] nextCoord, int delta) {

		float dx = (nextCoord[0] - location[0]);
		float dy = (nextCoord[1] - location[1]);

		float distance = (float) ((speed * delta / 1000) / Math.sqrt(dx * dx
				+ dy * dy));
		if (Float.isInfinite(distance))
			distance = 0;

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

//		location[2] = Engine.world.getWorldHeight()[coord[0]][coord[1]] + 2;
		
		location[2] = ((MapMesh) Engine.worldEntity.mdl).getHeight(location) + 0.8f;
	}
	
	private void navigateTo(int[] destCoord) {
		
		pathSteps = ai.AStarPathFinding.findPath(Engine.world, coord, destCoord);
	}
	
	private int[] locationToCoord(float[] location) {
		int[] coord = new int[2];
		
		coord[0] = (int)Math.rint(location[0]);
		coord[1] = (int)Math.rint(location[1]);
		
		return coord;
	}
}
