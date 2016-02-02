package main;

import java.io.IOException;

/**
 * The main method responsible for stating the game and stuff.
 * 
 * @author Christopher Dombroski
 *
 */
public class Untitled implements Runnable {
	
	Untitled() {
		Thread t = new Thread(this, ENGINE_THREAD_NAME);
		t.start();
	}
	
	private static final String ENGINE_THREAD_NAME = "Engine";
	
	public static void main(String[] args) {
		
		new Untitled();
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
