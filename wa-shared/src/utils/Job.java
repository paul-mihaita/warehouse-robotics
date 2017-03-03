package utils;

import java.util.ArrayList;

public class Job {

	int jobID;
	ArrayList<Task> tasks = new ArrayList<Task>();

	public Job(int jobID, ArrayList<Task> tasks) {
		this.jobID = jobID;
		this.tasks = tasks;
	}

	public float sumOfWeight() {

		float sumOfWeight = 0;
		for (Task task : tasks) {
			sumOfWeight += task.getTaskItem().getWeight()*task.getQuantity();
		}
		return sumOfWeight;
	}

	// GET
	public ArrayList<Task> geTasks() {
		return tasks;
	}

	public int getJobID() {
		return jobID;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	// SET
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

}
