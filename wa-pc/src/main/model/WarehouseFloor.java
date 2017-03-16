package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import communication.thread.Server;
import main.gui.GUI;
import main.job.JobWorth;
import main.route.CommandCenter;
import movement.Movement.move;
import student_solution.Graph;
import utils.Info;
import utils.Item;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;

public class WarehouseFloor {
	private HashMap<Robot, RobotHelper> poller = new HashMap<Robot, RobotHelper>();
	private HashSet<Robot> robots;

	private HashMap<Robot, Optional<Job>> assignment;

	private HashMap<String, Message> messageQueues;

	private HashMap<Integer, Job> jobList;

	private ArrayList<Item> items;

	private Graph<Location> floor;

	private Logger log;

	private boolean server;

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param arrayList
	 * 
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * @param Jobs
	 *            List of jobs in preferential order
	 * @param Log
	 *            log4j logger object
	 */
	public WarehouseFloor(Graph<Location> floor, ArrayList<Job> jobs, ArrayList<Item> items, Logger log,
			boolean server) {

		this.server = server;
		this.log = log;
		this.assignment = new HashMap<Robot, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.items = items;
		this.robots = new HashSet<Robot>();
		this.messageQueues = new HashMap<String, Message>();

		Robot keith = new Robot(Info.RobotNames[0], Info.RobotAddresses[0], new Location(2, 0), new Location(3, 0));
		this.robots.add(keith);

		Robot cell = new Robot(Info.RobotNames[1], Info.RobotAddresses[1], new Location(0, 0), new Location(1, 0));
		this.robots.add(cell);

		Robot charmander = new Robot(Info.RobotNames[2], Info.RobotAddresses[2], new Location(0, 1),
				new Location(0, 0));
		this.robots.add(charmander);

		for (Job j : jobs) {
			jobList.put(j.getJobID(), j);
		}
		Message[] tempArr = new Message[robots.size()];
		int i = 0;
		for (Robot r : robots) {
			assignment.put(r, Optional.empty());
			Message temp = new Message(new ArrayList<move>(), command.Wait, new BasicJob(0, new Task("", 0)));
			tempArr[i++] = temp;
			messageQueues.put(r.getName(), temp);
		}

		JobWorth jobWorth = new JobWorth(jobs, robots);
		HashMap<Integer, Float> temp = jobWorth.getReward();

		Integer maxkey = -1;
		for (Integer key : temp.keySet()) {
			if (maxkey == -1) {
				maxkey = key;
			}
		}

		reassignJobs();

		log.debug("creating threads for starting and stopping jobs");
		initalizePoller();
		log.debug("created");
		this.floor = floor;
		log.debug("Creating Server");
		Server s = new Server(Info.getRobots(), tempArr, log);
		if (server)
			s.launch();
		log.info("Server launched succesfully, warehousefloor constructed");
	}

	private void initalizePoller() {
		for (Robot r : robots) {
			poller.put(r, new RobotHelper(messageQueues.get(r.getName())));
		}
	}

	public void startRobots() {

		/*
		 * HashMap<Robot, Job> assignedJobs = new HashMap<Robot, Job>();
		 * 
		 * for (Robot r : assigment.keySet()) { if
		 * (assigment.get(r).isPresent()) { Job j = assigment.get(r).get();
		 * j.start(); assignedJobs.put(r, j); log.info(r.getName() +
		 * " was started on job id: " + j.getJobID()); } else {
		 * 
		 * log.info(r.getName() +
		 * " attempted to start, but has no assigned job"); } }
		 * 
		 * log.debug("Assigned job size: " + assignedJobs.size()); for (Job j :
		 * assignedJobs.values()) {
		 * 
		 * log.debug("Job id: " + j.getJobID());
		 * log.debug("Item .... Quantitiy .... Location"); for (Task t :
		 * j.getTasks()) { log.debug( t.getItem().getItemName() + " .... " +
		 * t.getQuantity() + " .... " + t.getItem().getLocation()); } }
		 * HashMap<Robot, ArrayList<ArrayList<move>>> routes =
		 * CommandCenter.generatePaths(assignedJobs);
		 * 
		 * for (Robot r : routes.keySet()) {
		 * GUI.displayPath(CommandCenter.getPathLocations().get(r)); }
		 * 
		 * givePaths(routes);
		 */

		for (Robot r : robots) {
			assignment.get(r).ifPresent(new Consumer<Job>() {
				@Override
				public void accept(Job t) {
					if (t.isSelected()) {
						t.start();
						HashMap<Robot, Job> give = new HashMap<Robot, Job>();
						give.put(r, t);
						HashMap<Robot, ArrayList<ArrayList<move>>> path = CommandCenter.generatePaths(give);
						GUI.displayPath(CommandCenter.getPathLocations().get(r));
						givePath(r, path.get(r));
					}
				}
			});
		}

	}

	public void givePath(Robot r, ArrayList<ArrayList<move>> routes) {
		if (!server)
			return;
		RobotHelper p = poller.get(r);
		p.overwriteRoutes(routes);
		p.start();
		try {
			p.join();
		} catch (InterruptedException e) {
			log.debug("robot cancelled");
		}
	}

	public boolean assign(String name, Job j) {

		log.debug(j.getJobID() + " added to " + name);

		if (!assignment.get(getRobot(name)).isPresent()) {
			assignment.remove(name);
			assignment.put(getRobot(name), Optional.of(j));
			j.select();
			return true;
		} else {
			Job c = assignment.get(getRobot(name)).get();
			if (c.isCompleted() || c.isCanceled()) {
				assignment.remove(name);
				assignment.put(getRobot(name), Optional.of(j));
				j.select();
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
		return assignment.get(robot);
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
			poller.get(r).interrupt();
		}
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void cancelJob(Job j) {
		if (j.isSelected()) {
			j.cancel();
			for (Robot r : assignment.keySet()) {
				assignment.get(r).ifPresent(new Consumer<Job>() {
					@Override
					public void accept(Job t) {
						if (t.equals(j)) {
							cancelJob(r);
						}
					}
				});
			}

			// TODO: Reselect Jobs

		} else if (j.isActive()) {
			j.cancel();

			// TODO: Interrupt job

		}

		j.cancel();
	}

	public void reassignJobs() {

		ArrayList<Job> validJobs = new ArrayList<Job>();
		HashSet<Robot> unAssigned = new HashSet<Robot>();
		for (Job j : jobList.values()) {
			if (j.isNotSelected()) {
				validJobs.add(j);
			}
		}

		for (Robot r : assignment.keySet()) {

			if (assignment.get(r).isPresent()) {
				Job j = assignment.get(r).get();
				if (j.isCanceled() || j.isCompleted()) {
					unAssigned.add(r);
				}
			} else {
				unAssigned.add(r);
			}

		}

		ArrayList<Robot> robotArray = new ArrayList<Robot>();
		for (Robot r : unAssigned) {
			robotArray.add(r);
		}

		JobWorth selector = new JobWorth(validJobs, unAssigned);

		int i = 0;
		for (Integer id : selector.getReward().keySet()) {
			jobList.get(id);
			if (i == robotArray.size()) {
				break;
			}

			assign(robotArray.get(i++).getName(), jobList.get(id));
		}

	}

}
