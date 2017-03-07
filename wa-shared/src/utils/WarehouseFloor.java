package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import student_solution.Graph;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	private HashMap<String, Optional<Job>> assigment;
	
	private HashMap<Integer, Job> jobList;

	private Graph<Location> floor;

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * 
	 */
	public WarehouseFloor(Graph<Location> floor, ArrayList<Job> jobs) {
		this.assigment = new HashMap<String, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.robots = new HashSet<Robot>();
		this.robots.add(new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0)));
		
		for (Job j: jobs){
			jobList.put(j.getJobID(), j);
		}

		for (Robot r : robots) {
			assigment.put(r.getName(), Optional.empty());
		}

		this.floor = floor;
	}

	public boolean assign(Robot r, Job j) {
		String name = r.getName();
		if (!assigment.get(name).isPresent()) {
			assigment.put(name, Optional.of(j));
			return true;
		} else {
			Job c = assigment.get(name).get();
			if (c.isCompleted() || c.isCanceled()) {
				assigment.put(name, Optional.of(j));
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}
	
	public HashMap<Integer, Job> getJobs(){
		return jobList;
	}

	/**
	 * 
	 * @return Graph of floor
	 */
	public Graph<Location> getFloorGraph() {
		return floor;
	}

}