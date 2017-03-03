package utils;

import java.util.ArrayList;

public class Job {

	int jobID;
	ArrayList<Task> tasks = new ArrayList<Task>();

	public Job(int jobID, ArrayList<Task> tasks) {
		this.jobID = jobID;
		this.tasks = tasks;
	}

	public ArrayList<Task> geTasks() {
		return tasks;
	}

	public int sumOfWeight(int quantity, int weight) {

		int sumOfWeight = quantity * weight;
		return sumOfWeight;
	}

}
