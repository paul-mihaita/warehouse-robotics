package main.job;

import java.util.ArrayList;

import utils.*;

public class JobWorthTest {
	
	public static void main(String[] argv){
		

		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		
		Robot r = new Robot("Robot1");
		
		ArrayList<Robot> robots = new ArrayList();
		robots.add(r);
		ArrayList<Job> jobs = in.getJobsArray();


		
		
	}
	

}
