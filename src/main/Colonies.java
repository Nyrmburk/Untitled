package main;

import java.io.IOException;

/**
 * The main method responsible for stating the game and stuff.
 * 
 * @author Christopher Dombroski
 *
 */
public class Colonies implements Runnable {
	
	Colonies() {
		Thread t = new Thread(this, ENGINE_THREAD_NAME);
		t.start();
	}
	
	private static final String ENGINE_THREAD_NAME = "Engine";
	
	public static void main(String[] args) {
		
		new Colonies();
	}

	@Override
	public void run() {
		Engine gameEngine = new Engine();
		
		try {
			gameEngine.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
