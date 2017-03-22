package communication;

import utils.Task;

public class BasicJob {
	private int id;
	private Task task;

	public BasicJob(int jobID, Task task) {
		this.setId(jobID);
		this.task = task;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}
}
