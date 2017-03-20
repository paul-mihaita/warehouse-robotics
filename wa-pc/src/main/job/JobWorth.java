package main.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import java.util.Comparator;

import main.route.CommandCenter;
import movement.Movement.move;
import utils.Job;
import utils.Robot;
import utils.Route;
import utils.Task;

public class JobWorth {

	private Job job;
	private Route route;
	private Task quantity;

	HashSet<Robot> robots = new HashSet<Robot>();
	ArrayList<Job> jobs = new ArrayList<Job>();
	HashMap<Robot, Job> map;
	HashMap<Integer, Float> jobsWithValue = new HashMap<>();
	HashMap<Integer, Float> finalJobsWithValue = new HashMap<>();

	public JobWorth(ArrayList<Job> jobs, HashSet<Robot> robots) {
		this.robots = robots;
		this.jobs = jobs;
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

	public HashMap<Integer, Float> getReward() {

		for (Job job : jobs) {
			map = new HashMap<Robot, Job>();

			for (Robot robot : robots) {
				map.put(robot, job);
			}
			int jobPathCost = 0;

			HashMap<Robot, ArrayList<ArrayList<move>>> paths = CommandCenter.generatePaths(map);
			Iterator<ArrayList<ArrayList<move>>> it1 = paths.values().iterator();
			while (it1.hasNext()) {
				ArrayList<ArrayList<move>> routes = new ArrayList<>();
				routes = it1.next();
				for (ArrayList<move> route : routes) {
					jobPathCost += route.size();
				}
			}
			jobsWithValue.put(job.getJobID(),
					job.getJobReward() / ((jobPathCost / robots.size()) * (job.sumOfWeight() / 50)));

		}

		finalJobsWithValue = sortByValue(jobsWithValue);
		printMap(finalJobsWithValue);
		return finalJobsWithValue;
	}

	public static HashMap<Integer, Float> sortByValue(HashMap<Integer, Float> jobsWithValue) {

		List<HashMap.Entry<Integer, Float>> list = new LinkedList<HashMap.Entry<Integer, Float>>(
				jobsWithValue.entrySet());
		// sorting based on values
		Collections.sort(list, new Comparator<HashMap.Entry<Integer, Float>>() {
			public int compare(Entry<Integer, Float> o1, Entry<Integer, Float> o2) {

				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		HashMap<Integer, Float> sortedJobsWithValue = new HashMap<Integer, Float>();
		for (HashMap.Entry<Integer, Float> entry : list) {
			sortedJobsWithValue.put(entry.getKey(), entry.getValue());
		}
		return sortedJobsWithValue;
	}


	public static void printMap(HashMap<Integer, Float> map) {
		for (Integer key : map.keySet()) {
			String k = key.toString();
			String value = map.get(key).toString();
			System.out.println("JobID: " + k + "\t worth: " + value);
		}

	}

}
