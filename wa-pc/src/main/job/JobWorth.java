package main.job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import main.route.Astar;
import main.route.CommandCenter;
import movement.Movement.move;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Route;
import utils.Task;

public class JobWorth {

	private Job job;
	private Route route;
	private Task quantity;

	private ArrayList<Robot> robots;
	private ArrayList<Job> jobs;
	private HashMap<Robot, Job> map;
	private HashMap<Integer, Float> jobsWorth;

	JobComparator jc = new JobComparator();
	PriorityQueue<Job> jobsQueue = new PriorityQueue<>(jc);

	public JobWorth(ArrayList<Job> jobs, ArrayList<Robot> unAssigned) {
		this.robots = unAssigned;
		this.jobs = jobs;
		this.jobsWorth = new HashMap<>();
	}

	public Route getRoute() {
		return this.route;
	}

	public Job getJob() {

		return this.job;
	}

	public Task getQuantity() {
		return this.quantity;
	}

	public PriorityQueue<Job> getReward() {

		for (Job job : jobs) {
			map = new HashMap<Robot, Job>();

			for (Robot robot : robots) {
				map.put(robot.cloneRobot(), job);
			}
			int jobPathCost = 0;
			Astar.reset();
			HashMap<Robot, ArrayList<ArrayList<move>>> paths = CommandCenter.generatePaths(map);
			Iterator<ArrayList<ArrayList<move>>> it1 = paths.values().iterator();
			while (it1.hasNext()) {
				ArrayList<ArrayList<move>> routes = new ArrayList<>();
				routes = it1.next();
				for (ArrayList<move> route : routes) {
					jobPathCost += route.size();
				}
			}
			
			jobsWorth.put(job.getJobID(),
					job.getJobReward() / ((jobPathCost / robots.size()) * (job.sumOfWeight() / 50)));

		}

		for (Job job : jobs) {
			jobsQueue.offer(job);
		}

		return jobsQueue;

	}

	// public static HashMap<Integer, Float> sortByValue(HashMap<Integer, Float>
	// jobsWithValue) {
	//
	// List<HashMap.Entry<Integer, Float>> list = new
	// LinkedList<HashMap.Entry<Integer, Float>>(
	// jobsWithValue.entrySet());
	// // sorting based on values
	// Collections.sort(list, new Comparator<HashMap.Entry<Integer, Float>>() {
	// public int compare(Entry<Integer, Float> o1, Entry<Integer, Float> o2) {
	//
	// return (o1.getValue()).compareTo(o2.getValue());
	// }
	// });
	// HashMap<Integer, Float> sortedJobsWithValue = new HashMap<Integer,
	// Float>();
	// for (HashMap.Entry<Integer, Float> entry : list) {
	// sortedJobsWithValue.put(entry.getKey(), entry.getValue());
	// }
	// return sortedJobsWithValue;
	// }

	public static void printMap(HashMap<Integer, Float> map) {
		for (Integer key : map.keySet()) {
			String k = key.toString();
			String value = map.get(key).toString();
			System.out.println("JobID: " + k + "\t worth: " + value);
		}

	}

	public class JobComparator implements Comparator<Job> {
		@Override
		public int compare(Job j1, Job j2) {
			if (jobsWorth.get(j1.getJobID()) < jobsWorth.get(j2.getJobID()))
				return -1;
			else if (jobsWorth.get(j1.getJobID()) > jobsWorth.get(j2.getJobID()))
				return 0;
			else
				return 0;
		}
	}

}
