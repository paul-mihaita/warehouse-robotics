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
	
	private boolean jobFinished;
	
	public Assignment(Robot robot){
		
		this.robot=robot;
		this.assignJobs=new ArrayList<>();
		this.finishedAssignedJobs=false;		
		this.jobFinished=false;
		
		
		this.start();
	}
	
	public void run(){
		
		this.run=true;
		
	}
}
