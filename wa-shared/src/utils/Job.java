package utils;

import java.util.ArrayList;

public class Job {

	private static enum Status {
		ACTIVE, INACTIVE, COMPLETED, CANCELED
	}

	private Status status;

	private int jobID;
	private ArrayList<Task> tasks = new ArrayList<Task>();

	public Job(int jobID, ArrayList<Task> tasks) {
		this.jobID = jobID;
		this.tasks = tasks;
		this.status = Status.INACTIVE;
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
		this.status = Status.ACTIVE;
	}

	public void completed() {
		this.status = Status.COMPLETED;
	}

	public void cancel() {
		this.status = Status.CANCELED;
	}

	public String getStatus() {
		return status.name();
	}
	
	public float getJobReward(){
		float reward = 0;
		for(Task t : tasks)
			reward += t.getTaskReward();
			return reward;
	}

	public boolean isActive() {
		return status.equals(Status.ACTIVE);
	}

	public boolean isCanceled() {
		return status.equals(Status.CANCELED);
	}

	public boolean isInactive() {
		return status.equals(Status.INACTIVE);
	}

	public boolean isCompleted() {
		return status.equals(Status.COMPLETED);
	}

}
