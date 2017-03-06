package main.job;

import java.util.ArrayList;

import utils.Job;
import utils.Location;
import utils.Robot;

public class Selection extends Thread{

	private Robot robot;
	private boolean run;
	private ArrayList<Job> copyJobs;
	private Location startLocation;
	
	public Selection(Robot robot, ArrayList<Job> jobs){
		
		this.robot=robot;
		this.startLocation=this.robot.getCurrentLocation();
		this.copyJobs=new ArrayList<Job>();
		
		for(Job j:jobs){
			this.copyJobs.add(j);
		}
		
		this.start();
	}
	
	public void run(){
		
		this.run=true;
		
		//JobWorth bestJob;
		while(run){
			if(this.copyJobs.size()<=0){
				
				break;
			}
		}
	}
}
