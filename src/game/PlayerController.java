package game;

import entity.Entity;
import input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Player controller
 * It needs to work on any entity (because why not?)
 * maybe I could go more abstract and have it control pawns?
 * 		seems a lot harder and unnecessary atm
 * should the controller itself directly read the inputs?
 * I say no because an ai controller... idk anymore.
 * how far should I decouple?
 * 		decouple entity from controller from inputs
 * methods
 * 		moveLateral
 * 		jump
 * 		changeLayer
 * how do I add more controls?
 * will I need more controls?
 * 		I don't think so.
 * 		But what if I want some javascript to add controls
 * 		They should be able to do that I think.
 * 		They would have to be more coupled or something
 *
 * Created by Nyrmburk on 6/15/2016.
 */
public class PlayerController {

	private Entity pawn;
	public List<Input> inputs = new ArrayList<>();

	public void setPawn(Entity pawn) {

		this.pawn = pawn;
	}

	public Entity getPawn() {

		return pawn;
	}
}
