package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.Message;
import communication.thread.Server;
import main.gui.GUI;
import main.job.JobWorth;
import main.route.CommandCenter;
import movement.Movement.move;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;

public class WarehouseFloor {

	private HashSet<Robot> robots;

	private HashMap<Robot, Optional<Job>> assigment;

	private HashMap<String, Message> messageQueues;

	private HashMap<Integer, Job> jobList;

	private Graph<Location> floor;

	private Logger log;
	
	private boolean server;

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
	public WarehouseFloor(Graph<Location> floor, ArrayList<Job> jobs, Logger log, boolean server) {

		this.server = server;
		this.log = log;
		this.assigment = new HashMap<Robot, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.robots = new HashSet<Robot>();
		this.messageQueues = new HashMap<String, Message>();
		
		Robot keith = new Robot("Keith", "0016530FDDAE", new Location(2, 0), new Location(1,0));
		//Robot cell = new Robot("Cell", "0016531AFA0B", new Location(0, 1), new Location(1, 0));
		//Robot charmander = new Robot("Charmander", "0016531AF6D6", new Location(0, 1), new Location(2, 0));
		this.robots.add(keith);
		// this.robots.add(cell);
		// this.robots.add(charmander);

		for (Job j : jobs) {
			jobList.put(j.getJobID(), j);
		}
		Message[] tempArr = new Message[robots.size()];
		int i = 0;
		for (Robot r : robots) {
			assigment.put(r, Optional.empty());
			Message temp = new Message(new ArrayList<move>(), command.Wait);
			tempArr[i++] = temp;
			messageQueues.put(r.getName(), temp);
		}

		HashSet<Robot> robots = this.getRobots();
		JobWorth jobWorth = new JobWorth(jobs, robots);
		HashMap<Integer, Float> temp = jobWorth.getReward();

		Integer maxkey = -1;
		for (Integer key : temp.keySet()) {
			if (maxkey == -1) {
				maxkey = key;
			}
		}
		
		
		//this.assign("Cell", jobList.get(maxkey));
		
		temp.remove(maxkey);
		
		maxkey = -1;
		
		for (Integer key: temp.keySet()){
			if(maxkey == -1){
				maxkey = key;
			}
			if(temp.get(key) > temp.get(maxkey)){
				maxkey = key;
			}
		}

		this.assign("Keith", jobList.get(maxkey));

		// temp.remove(maxkey);
		//
		// maxkey = -1;
		//
		// for (Integer key: temp.keySet()){
		// if(maxkey == -1){
		// maxkey = key;
		// }
		// if(temp.get(key) > temp.get(maxkey)){
		// maxkey = key;
		// }
		// }
		//
		// this.assign("Keith", jobList.get(maxkey));
		//

		this.floor = floor;
		log.debug("Creating Server");
		Server s = new Server(new Robot[] { keith}  , tempArr, log);
		if (server) s.launch();
		log.debug("Server launched succesfully, warehousefloor constructed");
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

		log.debug("Assigned job size: " + assignedJobs.size());
		for (Job j : assignedJobs.values()) {

			log.debug("Job id: " + j.getJobID());
			log.debug("Item .... Quantitiy .... Location");
			for (Task t : j.getTasks()) {
				log.debug(
						t.getItem().getItemName() + " .... " + t.getQuantity() + " .... " + t.getItem().getLocation());
			}
		}
		HashMap<Robot, ArrayList<ArrayList<move>>> routes = CommandCenter.generatePaths(assignedJobs);

		for (Robot r : routes.keySet()) {
			GUI.displayPath(CommandCenter.getPathLocations().get(r));
		}

		givePaths(routes);

	}

	private void givePaths(HashMap<Robot, ArrayList<ArrayList<move>>> routes) {
		if (!server) return;
		RobotHelper[] help = new RobotHelper[robots.size()];
		int i = 0;
		for (Robot robot : robots) {
			help[i++] = new RobotHelper(messageQueues.get(robot.getName()), routes.get(robot));
		}
		for (int j = 0; j < help.length; j++) {
			help[j].start();
		}
		for (int j = 0; j < help.length; j++) {
			try {
				help[j].join();
			} catch (InterruptedException e) {
				// shouldn't happen
				log.error("Robot helper was interupted", e);
			}
		}
	}

	public boolean assign(String name, Job j) {

		log.debug(j.getJobID() + " added to " + name);

		if (!assigment.get(getRobot(name)).isPresent()) {
			assigment.remove(name);
			assigment.put(getRobot(name), Optional.of(j));
			return true;
		} else {
			Job c = assigment.get(getRobot(name)).get();
			if (c.isCompleted() || c.isCanceled()) {
				assigment.remove(name);
				assigment.put(getRobot(name), Optional.of(j));
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

	public Robot getRobot(String name) {
		for (Robot r : robots) {
			if (r.getName().equals(name)) {
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