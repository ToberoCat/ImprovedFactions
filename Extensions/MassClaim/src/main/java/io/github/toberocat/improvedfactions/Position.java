package io.github.toberocat.improvedfactions;

import org.bukkit.Location;

public class Position {
	
	private Location pos1;
	private Location pos2;
	
	public Position(Location pos1, Location pos2) {
		super();
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	
	public Location getPos1() {
		return pos1;
	}
	public void setPos1(Location pos1) {
		this.pos1 = pos1;
	}
	public Location getPos2() {
		return pos2;
	}
	public void setPos2(Location pos2) {
		this.pos2 = pos2;
	}
	
	

}
