package utils;

import java.util.HashSet;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	// TODO Paul needs to put info about the map of the location floor in here

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param List
	 *            of robots
	 */
	public WarehouseFloor(HashSet<Robot> robots) {
		this.robots = robots;
	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}

}