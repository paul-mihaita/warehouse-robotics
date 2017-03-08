package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.Message;
import movement.Movement.move;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.Robot;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	private HashMap<String, Optional<Job>> assigment;

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
		this.assigment = new HashMap<String, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.robots = new HashSet<Robot>();
		this.messageQueues = new HashMap<String, Message>();

		this.robots.add(new Robot("Keith", "0016530FDDAE", new Location(0, 0), new Location(0, 0)));
		this.robots.add(new Robot("Cell", "0016531AFA0B", new Location(0, 0), new Location(1, 0)));

		for (Job j : jobs) {
			jobList.put(j.getJobID(), j);
		}

		for (Robot r : robots) {
			assigment.put(r.getName(), Optional.empty());
			messageQueues.put(r.getName(), new Message(new ArrayList<move>(), command.Wait));
		}

		this.floor = floor;
	}

	public void startRobots() {
		for (Robot r : robots) {
			if (assigment.get(r.getName()).isPresent()) {

				Job j = assigment.get(r.getName()).get();

				j.start();

				givePath(r, j);
				messageQueues.get(r.getName()).setCommand(command.Start);
				log.info(r.getName() + " was started on job id: " + j.getJobID());
			} else {
				log.info(r.getName() + " attempted to start, but has no assigned job");
			}
		}
	}

	public boolean assign(Robot r, Job j) {
		String name = r.getName();
		if (!assigment.get(name).isPresent()) {
			assigment.remove(name);
			assigment.put(name, Optional.of(j));
			this.givePath(r, j);
			return true;
		} else {
			Job c = assigment.get(name).get();
			if (c.isCompleted() || c.isCanceled()) {
				assigment.remove(name);
				assigment.put(name, Optional.of(j));
				this.givePath(r, j);
				return true;
			}
		}
		return false;
	}

	private void givePath(Robot robot, Job job) {

	}

	/**
	 * 
	 * @return List of robots
	 */
	public HashSet<Robot> getRobots() {
		return robots;
	}

	public Optional<Job> getJob(Robot robot) {
		return assigment.get(robot.getName());
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