package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.job.Input;
import main.job.JobWorth;

public class JobWorthJUnit {

	@Test
	public void testPassingNullRobotList() {
		Input input = new Input(false);
		input.initializeListOfJobs("1", "2", "3");
		JobWorth jw = new JobWorth(input.getJobsArray(), null);
		assertTrue(jw.getReward().size() > 0);
	}
}
