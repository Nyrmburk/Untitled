package entity;

import java.util.Arrays;

import world.World;
import graphics.Model;

public class Mob extends Entity {
	
	Faction faction;
	
	public Mob(String name, float[] location, Model model) {
		this(name, new Faction(), location, model);
	}

	public Mob(String name, Faction faction, float[] location, Model model) {
		super(name, Entity.MOB, location, model);
		this.faction = faction;
	}
}
