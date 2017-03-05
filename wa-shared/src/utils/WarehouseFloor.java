package utils;

import java.util.HashSet;

public class WarehouseFloor {
	
	private HashSet<Robot> robots;
	
	// TODO Paul needs to put info about the map of the location floor in here
	
	public WarehouseFloor(HashSet<Robot> robots) {
		this.robots = robots;
	}
	
	public HashSet<Robot> getRobots(){
		return robots;
	}
	
}