package main.job;

import java.util.ArrayList;

import utils.Job;
import utils.Robot;

public class Assignment extends Thread{

	private Robot robot;
	private Selection selector;
	private boolean run;
	private ArrayList<Job> jobs;
	private ArrayList<JobWorth> assignJobs;
	private boolean finishedAssignedJobs;
	private boolean jobCancelled;
	private boolean jobFinished;
	
	public Assignment(Robot robot, ArrayList<Job> trainJobs){
		
		this.robot=robot;
		this.assignJobs=new ArrayList<>();
		this.finishedAssignedJobs=false;
		this.jobCancelled=false;		
		this.jobFinished=false;
		
		//cancellation
		//
		
		this.start();
	}
	
	public void run(){
		
		this.run=true;
		//JobWorth jobToBeAssigned;
	}
}
