package entity;

import input.State;

import java.util.Iterator;
import java.util.Stack;
import java.util.TreeMap;

import uuid.IDGenerator;

public class Actor extends Entity {

	private IDGenerator generator;
	protected TreeMap<Integer, Stack<State>> states;

	public Actor(String name) {

		super(name);
		generator = new IDGenerator();
		states = new TreeMap<Integer, Stack<State>>();
	}
	
//	public void handleCommand(Intent command) {
//		
//		Iterator<Stack<State>> it = states.values().iterator();
//		while (it.hasNext()) {
//			State state = it.next().peek();
//			state.handleInput(command);
//		}
//	}
	
	public void onUpdate(int delta) {
		
		Iterator<Stack<State>> it = states.values().iterator();
		while (it.hasNext()) {
			State state = it.next().peek();
			state.update(delta);
		}
	}
	
	public int addStateMachine() {
		
		int id = generator.genID();
		states.put(id, new Stack<State>());
		return id;
	}

	public void pushState(int UUID, State state) {

		this.states.get(UUID).push(state);
	}

	public State popState(int UUID) {

		return this.states.get(UUID).pop();
	}
	
	public State peekState(int UUID) {

		return this.states.get(UUID).peek();
	}
}
