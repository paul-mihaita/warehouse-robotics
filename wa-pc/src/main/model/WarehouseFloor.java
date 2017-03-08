package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.Message;
import main.route.CommandCenter;
import movement.Movement.move;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.Robot;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	private HashMap<Robot, Optional<Job>> assigment;

	private HashMap<String, Message> messageQueues;

	private HashMap<Integer, Job> jobList;

	private Graph<Location> floor;

	private Logger log;

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * @param Jobs
	 *            List of jobs in preferential order
	 * @param Log
	 *            log4j logger object
	 */
	public WarehouseFloor(Graph<Location> floor, ArrayList<Job> jobs, Logger log) {

		this.log = log;
		this.assigment = new HashMap<Robot, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.robots = new HashSet<Robot>();
		this.messageQueues = new HashMap<String, Message>();

		this.robots.add(new Robot("Keith", "0016530FDDAE", new Location(0, 0), new Location(0, 0)));
		this.robots.add(new Robot("Cell", "0016531AFA0B", new Location(0, 0), new Location(1, 0)));

		for (Job j : jobs) {
			jobList.put(j.getJobID(), j);
		}

		for (Robot r : robots) {
			assigment.put(r, Optional.empty());
			messageQueues.put(r.getName(), new Message(new ArrayList<move>(), command.Wait));
		}

		this.floor = floor;
		
	}

	public void startRobots() {

		HashMap<Robot, Job> assignedJobs = new HashMap<Robot, Job>();

		for (Robot r : assigment.keySet()) {
			if (assigment.get(r).isPresent()) {
				Job j = assigment.get(r).get();
				j.start();
				assignedJobs.put(r, j);
				log.info(r.getName() + " was started on job id: " + j.getJobID());
			} else {
				
				log.info(r.getName() + " attempted to start, but has no assigned job");
			}
		}

		HashMap<Robot, ArrayList<ArrayList<move>>> routes = CommandCenter.generatePaths(assignedJobs);
		
		for (Robot r : routes.keySet()){
			givePath(r, routes.get(r));
		}

	}

	public boolean assign(Robot r, Job j) {
		String name = r.getName();
		if (!assigment.get(name).isPresent()) {
			assigment.remove(name);
			assigment.put(r, Optional.of(j));
			return true;
		} else {
			Job c = assigment.get(name).get();
			if (c.isCompleted() || c.isCanceled()) {
				assigment.remove(name);
				assigment.put(r, Optional.of(j));
				return true;
			}
		}
		return false;
	}

	private void givePath(Robot robot, ArrayList<ArrayList<move>> arrayList) {
		

	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}
	
	public Robot getRobot(String name){
		for (Robot r : robots){
			if (r.getName().equals(name)){
				return r;
			}
		}
		
		return null;
	}

	public Optional<Job> getJob(Robot robot) {
		return assigment.get(robot);
	}

	public HashMap<Integer, Job> getJobs() {
		return jobList;
	}

	/**
	 * 
	 * @return Graph of floor
	 */
	public Graph<Location> getFloorGraph() {
		return floor;
	}

	public void cancelJob(Robot r) {
		if (getJob(r).isPresent()) {
			getJob(r).get().cancel();
		}
	}

}