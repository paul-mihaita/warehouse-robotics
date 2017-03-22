package main.job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import utils.Job;
import utils.Robot;
import utils.Route;
import utils.Task;

public class JobWorth {

	private Job job;
	private Route route;
	private Task quantity;

	private ArrayList<Robot> robots;
	private ArrayList<Job> jobs;

	private static JobComparator jc = new JobComparator();
	private PriorityQueue<Job> jobsQueue = new PriorityQueue<>(jc);

	/////////////////////////////////////////////////
	public static HashMap<Job, Float> bella = new HashMap<>();

	/////////////////////////////////////////////////

	public JobWorth(ArrayList<Job> jobs, ArrayList<Robot> unAssigned) {
		this.robots = unAssigned;
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

	public PriorityQueue<Job> getReward() {

		manhattanDistance(robots, jobs);

		return jobsQueue;

	}

	public void manhattanDistance(ArrayList<Robot> robots, ArrayList<Job> jobs) {

		float tempAvgRevForRobot = 0f;
		float sumTaskRev = 0f;
		float valueOfJob = 0f;

		for (Job job : jobs) {
			for (Task task : job.getTasks()) {
				for (Robot robot : robots) {
					int x = Math.abs(robot.getCurrentLocation().getX() - task.getItem().getLocation().getX());
					int y = Math.abs(robot.getCurrentLocation().getY() - task.getItem().getLocation().getY());
					tempAvgRevForRobot = (x + y);
				}
				sumTaskRev += (tempAvgRevForRobot / robots.size()) * Math.abs(50 / task.getQuantity());
				tempAvgRevForRobot = 0;
			}
			valueOfJob = sumTaskRev / job.getTasks().size();
			sumTaskRev = 0;
			bella.put(job, valueOfJob);
		}

		for (Job job : jobs)
			jobsQueue.offer(job);

	}

	public static void printMap(HashMap<Integer, Float> map) {
		for (Integer key : map.keySet()) {
			String k = key.toString();
			String value = map.get(key).toString();
			System.out.println("JobID: " + k + "\t worth: " + value);
		}

	}

	public static class JobComparator implements Comparator<Job> {
		@Override
		public int compare(Job j1, Job j2) {
			if (bella.get(j1) < bella.get(j2))
				return -1;
			else if (bella.get(j1) == bella.get(j2))
				return 0;
			else
				return 1;
		}
	}

}
