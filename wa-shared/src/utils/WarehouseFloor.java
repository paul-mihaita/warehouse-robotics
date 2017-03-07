package utils;

import java.util.HashSet;

import student_solution.Graph;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	private Graph<Location> floor;

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * 
	 */
	public WarehouseFloor(Graph<Location> floor) {
		this.robots = new HashSet<Robot>();
		this.robots.add(new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0)));
		this.floor = floor;
	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}

	public Graph<Location> getFloorGraph() {
		return floor;
	}

}