package main.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import bootstrap.Start;
import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import communication.thread.Converters;
import communication.thread.Server;
import lejos.util.Delay;
import main.job.JobWorth;
import main.route.Astar;
import main.route.CommandCenter;
import movement.Movement.move;
import rp.util.Rate;
import student_solution.Graph;
import utils.DropLocation;
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

	private HashMap<Robot, Message> messageQueues;

	private HashMap<Integer, Job> jobList;

	private ArrayList<Item> items;

	private Graph<Location> floor;

	private HashSet<ArrayList<Location>> activePaths;

	private Logger log;

	private boolean server;

	private ArrayList<DropLocation> dropLocations;

	/**
	 * Creates the Warehouse floor object, contains all the data about the
	 * warehouse floor.
	 * 
	 * @param Drop
	 *            Locations
	 * 
	 * @param Items
	 * 
	 * @param Floor
	 *            Graph of locations which contain the warehouse floor
	 * @param Jobs
	 *            List of jobs in preferential order
	 * @param Log
	 *            log4j logger object
	 */
	public WarehouseFloor(Graph<Location> floor, ArrayList<Job> jobs, ArrayList<Item> items,
			ArrayList<DropLocation> dropLocations, Logger log, boolean server) {

		this.server = server;
		this.log = log;
		this.assignment = new HashMap<Robot, Optional<Job>>();
		this.jobList = new HashMap<Integer, Job>();
		this.items = items;
		this.robots = new HashSet<Robot>();
		this.messageQueues = new HashMap<Robot, Message>();
		this.activePaths = new HashSet<ArrayList<Location>>();
		this.dropLocations = dropLocations;

		for (DropLocation d : dropLocations) {
			log.debug(d.toString());
		}

		Robot[] robos = Info.getRobotsPaul();
		this.robots.add(robos[0]); // squirtle
		this.robots.add(robos[1]); // bulbasaur
		this.robots.add(robos[2]); // charmander

		for (Job j : jobs) {
			jobList.put(j.getJobID(), j);
		}
		Message[] tempArr = new Message[robots.size()];
		int i = 0;
		for (Robot r : robots) {
			assignment.put(r, Optional.empty());
			Message temp = new Message(new ArrayList<move>(), command.Wait, new BasicJob(0, new Task("", 0)));
			tempArr[i++] = temp;
			messageQueues.put(r, temp);
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
			poller.put(r, new RobotHelper(messageQueues.get(r)));
		}
	}

	public void startRobots() {

		for (Robot r : robots) {
			assignment.get(r).ifPresent(new Consumer<Job>() {
				@Override
				public void accept(Job job) {
					if (job.isSelected()) {
						job.start();
						Robot temp = r.cloneRobot();
						HashMap<Robot, Job> give = new HashMap<Robot, Job>();
						give.put(temp, job);
						Astar.reset();
						HashMap<Robot, ArrayList<ArrayList<move>>> path = CommandCenter.generatePaths(give);
						ArrayList<Location> locPath = conc(CommandCenter.getPathLocations().get(temp));
						/*
						 * Gets a thread which terminates when the job is
						 * completed. Waits for that moment
						 */
						if (server) {
							new Thread() {
								public void run() {
									this.setName("Job thread: " + r.getName() + " " + job.getJobID());

									addToPaths(locPath);

									Thread p = givePath(r, path.get(temp), job);
									p.start();

									while (p.isAlive()) {
										new Rate(100).sleep();
									}

									if (p.isInterrupted()) {
										job.cancel();
									} else {
										job.completed();
									}
									removeFromPaths(locPath);
								}
							}.start();
						} else {
							new Thread() {
								public void run() {
									this.setName("Job thread: " + r.getName() + " " + job.getJobID());

									addToPaths(locPath);

									Delay.msDelay(1000);

									job.completed();
									r.setCurrentLocation(
											job.getTasks().get(job.getTasks().size() - 1).getItem().getLocation());
									removeFromPaths(locPath);
								}

							}.start();
						}

					}
				}

			});
		}

	}

	public Thread givePath(Robot r, ArrayList<ArrayList<move>> routes, Job job) {
		if (!server)
			return new Thread();
		RobotHelper p = poller.get(r);
		r.setOnPickup(true);
		r.setOnJob(true);
		p.overwriteRoutes(routes);
		messageQueues.get(r).setJob(Converters.toBasicJob(job));
		return p;
	}

	public synchronized void addToPaths(ArrayList<Location> path) {
		activePaths.add(path);
	}

	public synchronized void removeFromPaths(ArrayList<Location> path) {
		activePaths.remove(path);
	}

	public synchronized HashSet<ArrayList<Location>> getActivePaths() {
		return activePaths;
	}

	private ArrayList<Location> conc(ArrayList<ArrayList<Location>> totalPath) {
		ArrayList<Location> cPath = new ArrayList<Location>();
		for (ArrayList<Location> parts : totalPath) {

			for (Location l : parts) {
				cPath.add(l);
			}

		}
		return cPath;
	}

	public boolean assign(Robot r, Job j) {

		DropLocation drop = new DropLocation("Wating area");

		for (DropLocation d : dropLocations) {

			if (!d.isReserved()) {
				d.reserved(true);
				drop = d;
			}

		}

		log.debug(j.getJobID() + " added to " + r.getName() + " with drop-off " + drop.getName());

		ArrayList<Task> jobs = j.getTasks();

		// jobs.add(new Task("dd", 0));

		// j.setTasks(jobs);

		if (!assignment.get(r).isPresent()) {
			assignment.remove(r);
			assignment.put(r, Optional.of(j));
			j.select();
			return true;
		} else {
			Job c = assignment.get(r).get();
			if (c.isCompleted() || c.isCanceled()) {
				assignment.remove(r);
				assignment.put(r, Optional.of(j));
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
		ArrayList<Robot> unAssigned = new ArrayList<Robot>();
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

		if (unAssigned.isEmpty()) {
			return;
		}

		JobWorth selector = new JobWorth(validJobs, unAssigned);
		PriorityQueue<Job> queue = selector.getReward();

		for (Robot r : unAssigned) {
			this.assign(r, queue.poll());
		}

	}

}
