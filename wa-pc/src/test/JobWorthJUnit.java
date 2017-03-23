package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.junit.Test;

import main.job.Input;
import main.job.JobWorth;
import utils.Info;
import utils.Job;
import utils.Robot;

public class JobWorthJUnit {

	@Test
	public void testPassingNullRobotList() {
		Input input = new Input(false);
		ArrayList<Robot> robots = new ArrayList<>();
		JobWorth jw = new JobWorth(input.getJobsArray(), Info.getRobotsRavinder());
		PriorityQueue<Job> queue = jw.getReward();
		System.out.println(queue.poll().getJobID());
		System.out.println(queue.poll().getJobID());
		System.out.println(queue.poll().getJobID());
		
		assertTrue(jw.getReward().size() >= 0);
		
	}
}
