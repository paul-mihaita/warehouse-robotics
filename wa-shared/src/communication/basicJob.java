package communication;

import utils.Task;

public class basicJob {
	private int id;
	private Task task;

	public basicJob(int jobID, Task task) {
		this.setId(jobID);
		this.setTask(task);
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

	public void setTask(Task task) {
		this.task = task;
	}
}
