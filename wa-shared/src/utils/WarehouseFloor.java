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
	 * @param Robots
	 *            HashSet of robots
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * 
	 */
	public WarehouseFloor(HashSet<Robot> robots, Graph<Location> floor) {
		this.robots = robots;
		this.floor = floor;
	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}
	
	public Graph<Location> getFloorGraph(){
		return floor;
	}

}