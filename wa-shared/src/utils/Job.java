package utils;

import java.util.ArrayList;

public class Job {

	int jobID;
	ArrayList<Task> tasks = new ArrayList<Task>();

	public int getJobID() {
		return jobID;
	}

	public Job(int jobID, ArrayList<Task> tasks) {
		this.jobID = jobID;
		this.tasks = tasks;
	}

	public ArrayList<Task> geTasks() {
		return tasks;
	}

	public float sumOfWeight() {

		float sumOfWeight = 0;
		for (Task task : tasks) {
			sumOfWeight += task.getTaskItem().getWeight();
		}
		return sumOfWeight;
	}

}
