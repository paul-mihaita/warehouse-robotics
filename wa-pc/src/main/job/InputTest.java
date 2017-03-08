package main.job;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import utils.Job;
import utils.Task;

public class InputTest {

	private static final Logger LOG = Logger.getLogger(InputTest.class);

	public static void main(String[] args) throws Exception {
		ArrayList<Job> jobs;
		String file1 = "1.csv";
		String file2 = "2";
		String file3 = "3.csv";

		Input input = new Input(false);
		if (input.initializeListOfJobs(file1, file2, file3)) {
			
			
			LOG.debug("Start reading file");
			
			jobs = input.getJobsArray();
			for (Job j : jobs) {
				LOG.debug("JOB: " + j.getJobID() + " Sum of weights: " + j.sumOfWeight());
				for (Task t : j.getTasks()) {
					LOG.debug("New task");
					LOG.debug("Item name: " + t.getTaskItem().getItemName() + "     Quantity: "
							+ t.getQuantity() + "     Reward: " + t.getTaskItem().getReward() + "     Weight: "
							+ t.getTaskItem().getWeight());
				}
				LOG.debug("next task");
			}
		} else {
			LOG.debug("One or more file not found");
		}
	}

}
