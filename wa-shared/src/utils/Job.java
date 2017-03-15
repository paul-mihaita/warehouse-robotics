package utils;

import java.util.ArrayList;

public class Job {

	public static enum Status {
		// ACTIVE, INACTIVE, COMPLETED, CANCELED
		NOT_SELECTED, SELECTED, ACTIVE, CANCELED, COMPLETED
	}

	private Status status;

	private int jobID;
	private ArrayList<Task> tasks = new ArrayList<Task>();

	public Job(int jobID, ArrayList<Task> tasks) {
		this.jobID = jobID;
		this.tasks = tasks;
		this.status = Status.NOT_SELECTED;
	}

	public float sumOfWeight() {

		float sumOfWeight = 0;
		for (Task task : tasks) {
			sumOfWeight += task.getItem().getWeight() * task.getQuantity();
		}
		return sumOfWeight;
	}

	// GET
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

	public void start() {
		if (!status.equals(Status.CANCELED)) {
			this.status = Status.ACTIVE;
		}
	}

	public void select() {
		if (!status.equals(Status.CANCELED)) {
			status = Status.SELECTED;
		}
	}

	public void completed() {
		this.status = Status.COMPLETED;
	}

	public void cancel() {
		this.status = Status.CANCELED;
	}

	public Status getStatus() {
		return status;
	}

	public float getJobReward() {
		float reward = 0;
		for (Task t : tasks)
			reward += t.getTaskReward();
		return reward;
	}

	public boolean isActive() {
		return status.equals(Status.ACTIVE);
	}

	public boolean isCanceled() {
		return status.equals(Status.CANCELED);
	}

	public boolean isSelected() {
		return status.equals(Status.SELECTED);
	}

	public boolean isNotSelected() {
		return status.equals(Status.NOT_SELECTED);
	}

	public boolean isCompleted() {
		return status.equals(Status.COMPLETED);
	}

}
