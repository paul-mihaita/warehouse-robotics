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
import communication.thread.Converters;
import communication.thread.Server;
import main.gui.GUI;
import main.job.JobWorth;
import main.route.CommandCenter;
import movement.Movement.move;
import rp.util.Rate;
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

		Robot squirtle = new Robot(Info.RobotNames[0], Info.RobotAddresses[0], new Location(11, 6),
				new Location(11, 7));
		//this.robots.add(squirtle);

		Robot bulbasaur = new Robot(Info.RobotNames[1], Info.RobotAddresses[1], new Location(1, 7), new Location(0, 7));
		//this.robots.add(bulbasaur);

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
		Server s = new Server(robots.toArray(new Robot[robots.size()]), tempArr, log);
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
						/*
						 * Gets a thread which terminates when the job is
						 * completed. Waits for that moment
						 */
						new Thread() {
							public void run() {

								Thread p = givePath(r, path.get(r), t);
								p.start();

								while (p.isAlive()) {
									new Rate(100).sleep();
								}

								if (p.isInterrupted()) {
									t.cancel();
								} else {
									t.completed();
								}
							};
						}.start();
					}
				}
			});
		}

	}

	public Thread givePath(Robot r, ArrayList<ArrayList<move>> routes, Job job) {
		if (!server)
			return new Thread() {
			};
		RobotHelper p = poller.get(r);
		r.setOnPickup(true);
		r.setOnJob(true);
		p.overwriteRoutes(routes);
		messageQueues.get(r).setJob(Converters.toBasicJob(job));
		return p;
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

		if (robotArray.isEmpty()) {
			return;
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
